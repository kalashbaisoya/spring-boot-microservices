package io.codejava.PaymentService.entity;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="TRANSACTION_DETAILS")
public class TransactionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "ORDER_ID")
	private long orderId;
	
	@Column(name = "AMOUNT")
	private long paymentAmount;
	
	@Column(name = "MODE")
	private String paymentMode;
	
	@Column(name = "REFERENCE_NO")
	private String referenceNumber;
	
	@Column(name = "STATUS")
	private String paymentStatus;
	
	@Column(name = "PAYMENT_DATE")
	private Instant paymentDate;
	
	
	
}
