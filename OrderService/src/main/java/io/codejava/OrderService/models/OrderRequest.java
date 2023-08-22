package io.codejava.OrderService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

	private Long productId;
	private Long quantity;
	private Long amount;
	private PaymentMode paymentMode;
	
}
