package io.codejava.OrderService.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ORDER_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;
	@Column(name = "PRODUCT_ID")
	private Long productId;
	@Column(name = "QUANTITY")
	private Long quantity;
	@Column(name = "TOTAL_AMOUNT")
	private Long amount;
	@Column(name = "DATE")
	private Instant orderDate;
	@Column(name = "STATUS")
	private String orderStatus;
	
	
}
