package io.codejava.PaymentService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.codejava.PaymentService.entity.TransactionDetail;
import io.codejava.PaymentService.models.PaymentMode;
import io.codejava.PaymentService.models.PaymentRequest;
import io.codejava.PaymentService.models.PaymentResponse;
import io.codejava.PaymentService.repository.PaymentRepo;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepo paymentRepo;

	@Override
	public Long doPayment(PaymentRequest paymentRequest) {
		log.info("Recording Payment Details: {}", paymentRequest);

        TransactionDetail transactionDetail
                = TransactionDetail.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentAmount(paymentRequest.getAmount())
                .build();

        paymentRepo.save(transactionDetail);

        log.info("Transaction Completed with Id: {}", transactionDetail.getId());

        return transactionDetail.getId();

	}
	
	
	@Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting payment details for the Order Id: {}", orderId);

        TransactionDetail transactionDetail
                = paymentRepo.findByOrderId(Long.valueOf(orderId));

        PaymentResponse paymentResponse
                = PaymentResponse.builder()
                .paymentId(transactionDetail.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetail.getPaymentMode()))
                .paymentDate(transactionDetail.getPaymentDate())
                .orderId(transactionDetail.getOrderId())
                .status(transactionDetail.getPaymentStatus())
                .amount(transactionDetail.getPaymentAmount())
                .build();

        return paymentResponse;
    }
	
	
}
