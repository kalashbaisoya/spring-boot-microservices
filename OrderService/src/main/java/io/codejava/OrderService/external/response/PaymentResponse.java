package io.codejava.OrderService.external.response;

import java.time.Instant;

import io.codejava.OrderService.models.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

	private long orderId;
	private long amount;
	private long paymentId;
	private String referenceNumber;
	private String status;
	private PaymentMode paymentMode;
	private Instant paymentDate;
}
