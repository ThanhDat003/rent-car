package com.rental_car.RentalCar.service.impl;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentIntent;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentMethod;
import com.rental_car.RentalCar.repository.PaymentRepository;
import com.rental_car.RentalCar.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalServiceImpl implements PayPalService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(float total,
                                 String currency,
                                 PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent,
                                 String description,
                                 String cancelUrl,
                                 String successUrl){
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        apiContext.setMaskRequestId(true);
        try {
            return payment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refundPayment(String paymentId, float amount) throws PayPalRESTException {
        try {
            // Create a refund request
            RefundRequest refundRequest = new RefundRequest();
            refundRequest.setAmount(new Amount().setCurrency("USD").setTotal(String.format("%.2f", amount)));

            // Get the payment and find the sale transaction
            Payment payment = Payment.get(apiContext, paymentId);
            Transaction transaction = payment.getTransactions().get(0); // Assuming there's only one transaction
            RelatedResources relatedResources = transaction.getRelatedResources().get(0); // Assuming there's only one related resource
            Sale sale = relatedResources.getSale();

            // Create the refund
            DetailedRefund refund = sale.refund(apiContext, refundRequest);

            System.out.println("Refund ID: " + refund.getId());
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
            throw e;
        }
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution=new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        try {
            return payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }
}
