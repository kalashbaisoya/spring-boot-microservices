package io.codejava.PaymentService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejava.PaymentService.entity.TransactionDetail;

@Repository
public interface PaymentRepo extends JpaRepository<TransactionDetail, Long> {

	TransactionDetail findByOrderId(Long valueOf);

}
