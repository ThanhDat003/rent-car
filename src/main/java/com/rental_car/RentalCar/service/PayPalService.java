package com.rental_car.RentalCar.service;

import com.paypal.base.rest.PayPalRESTException;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentIntent;
import com.rental_car.RentalCar.config.enumPayPal.PaypalPaymentMethod;
import com.paypal.api.payments.Payment;

public interface PayPalService {
    Payment createPayment(float total, String currency,
                          PaypalPaymentMethod method,
                          PaypalPaymentIntent intent,
                          String description,
                          String cancelUrl,
                          String successUrl);
    void refundPayment(String paymentId, float amount) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId);

}
