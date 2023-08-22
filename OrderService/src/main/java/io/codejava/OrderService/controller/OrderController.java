package io.codejava.OrderService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.codejava.OrderService.models.OrderRequest;
import io.codejava.OrderService.models.OrderResponse;
import io.codejava.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/order")
@Log4j2
@EnableMethodSecurity
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PreAuthorize("hasAuthority('Customer')")
	@PostMapping("/placeOrder")
	public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
		
		Long id = orderService.placeOrder(orderRequest);
		log.info("Order Id: {}",id);
		
		return new ResponseEntity<>(id,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderDetailsByOrderId(@PathVariable long orderId) {
		OrderResponse orderResponse
        = orderService.getOrderDetailsByOrderId(orderId);
		
        log.info("OrderResponse object received successfully in"
        		+ " controller and returning the response back to http-request"
        		+ " on URI: http://ORDER-SERVICE/order/"+orderId);
        
        log.info("orderResponse: {}",orderResponse);

		return new ResponseEntity<>(orderResponse,HttpStatus.OK);
	}
}
