package com.rental_car.RentalCar.controller.customer;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentIntent;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentMethod;
import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.repository.*;
import com.rental_car.RentalCar.service.BookingService;
import com.rental_car.RentalCar.service.MailService;
import com.rental_car.RentalCar.service.PayPalService;
import com.rental_car.RentalCar.util.UtilsPaypal;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/rent-car/customer/booking")
public class PaymentController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private PayPalService paypalService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Car_OwnerRepository car_ownerRepository;
    @Autowired
    private MailService mailService;

    @PostMapping("/pay")
    public String paypal(Model model,
                       HttpServletRequest request,
                       @RequestParam("start_date_time") LocalDateTime start_date_time,
                       @RequestParam("end_date_time") LocalDateTime end_date_time,
                       @RequestParam("carId") Long carId,
                       @RequestParam("totalPriceUSD") float price,
                       @RequestParam("totalPrice") Long price1) throws MessagingException {
        Car car = carRepository.findById(carId).orElseThrow();
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Customer customer = customerRepository.findByEmail(email);

        String driver_information = customer.getName() + " " + customer.getDriving_license();
        Booking booking1 = bookingService.paymentPaypal(driver_information, start_date_time, end_date_time, car, customer, price1);

        request.getSession().setAttribute("bookingId", booking1.getId());
        request.getSession().setAttribute("carId", car.getId());

        String cancelUrl = UtilsPaypal.getBaseURL(request) + "/rent-car/customer/booking/" + URL_PAYPAL_CANCEL;
        String successUrl = UtilsPaypal.getBaseURL(request) + "/rent-car/customer/booking/" + URL_PAYPAL_SUCCESS;

        try{
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Payment for booking",
                    cancelUrl,
                    successUrl);
            for (Links link : ((Payment) payment).getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    System.out.println(link.getHref());
                    return "redirect:" + link.getHref();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "customer/rent-car/cancel";
    }
    @RequestMapping(method = RequestMethod.GET, value = URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,HttpServletRequest request) throws MessagingException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            Long bookingId = (Long) request.getSession().getAttribute("bookingId");
            com.rental_car.RentalCar.entity.Payment payment1 = new com.rental_car.RentalCar.entity.Payment();
            payment1.setPaymentMethod(com.rental_car.RentalCar.entity.Payment.PaymentMethod.PayPal);
            payment1.setPaymentDetail(paymentId);
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
            payment1.setBooking(booking);
            paymentRepository.save(payment1);
            long totalPrice = Math.round(booking.getTotal_price());
            mailService.sendBookingSuccessEmail(booking.getCustomer().getEmail(), booking.getCar().getName(), booking.getStartDateTime(), booking.getEndDateTime(), totalPrice);
            return "customer/rent-car/success";
        }
        return "redirect:/";
    }


    @PostMapping("/cash")
    public String cash(Model model,
                       @RequestParam("start_date_time") LocalDateTime start_date_time,
                       @RequestParam("end_date_time") LocalDateTime end_date_time,
                       @RequestParam("carId") Long carId,
                       @RequestParam("totalPrice") Long price) throws MessagingException {
        Car car = carRepository.findById(carId).orElseThrow();
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Customer customer = customerRepository.findByEmail(email);
        String driver_information = customer.getName() + " " + customer.getDriving_license();

        if (customer.getWallet() < price) {
            model.addAttribute("error", "You don't have enough money in your wallet to make this payment.");
            return "customer/rent-car/cancel";
        }else {
            customer.setWallet(customer.getWallet() - price);
            customerRepository.save(customer);
        }

        Booking booking = bookingService.paymentCash(driver_information, start_date_time, end_date_time, car, customer, price);

        com.rental_car.RentalCar.entity.Payment payment = new com.rental_car.RentalCar.entity.Payment();
        payment.setPaymentMethod(com.rental_car.RentalCar.entity.Payment.PaymentMethod.Cash);
        String cash = UUID.randomUUID().toString();
        System.out.println(cash);
        payment.setPaymentDetail(cash);
        payment.setBooking(booking);
        paymentRepository.save(payment);

        Car_Owner carOwner = car.getCarOwner();
        carOwner.setWallet(carOwner.getWallet() + price);
        car_ownerRepository.save(carOwner);

        mailService.sendBookingSuccessEmail(booking.getCustomer().getEmail(), booking.getCar().getName(), start_date_time, end_date_time, price);

        return "customer/rent-car/success";
    }

}
