package io.codejava.PaymentService.service;

import io.codejava.PaymentService.models.PaymentRequest;
import io.codejava.PaymentService.models.PaymentResponse;

public interface PaymentService {

	Long doPayment(PaymentRequest paymentRequest);
	

    public PaymentResponse getPaymentDetailsByOrderId(String orderId);

}
