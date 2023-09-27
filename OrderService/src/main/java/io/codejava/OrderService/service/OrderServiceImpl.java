package io.codejava.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.codejava.OrderService.entities.Order;
import io.codejava.OrderService.exception.CustomException;
import io.codejava.OrderService.external.client.PaymentService;
import io.codejava.OrderService.external.client.ProductService;
import io.codejava.OrderService.external.request.PaymentRequest;
import io.codejava.OrderService.external.response.PaymentResponse;
import io.codejava.OrderService.external.response.ProductResponse;
import io.codejava.OrderService.models.OrderRequest;
import io.codejava.OrderService.models.OrderResponse;
import io.codejava.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${microservices.product}")
	private String productServiceUrl;

	@Value("${microservices.payment}")
	private String paymentServiceUrl;

	@Override
	public Long placeOrder(OrderRequest orderRequest) {
		
		//blocking products to process orderRequest by customer
		productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
		
		log.info("Placing Order Request: {} with STATUS--> CREATED",orderRequest);
		
		// creating order with status CREATED from orderRequest object
		Order order = Order.builder()
				.amount(orderRequest.getAmount())
				.productId(orderRequest.getProductId())
				.quantity(orderRequest.getQuantity())
				.orderStatus("CREATED")
				.orderDate(Instant.now()).build();
		
		order = orderRepository.save(order);
		
		// creating paymentRequest object using orderRequest
		log.info("Calling Payment Service to complete the payment");
        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getAmount())
                .build();
        
        // making payment request and set status
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Oder status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        
        // finally save latest status of order
        orderRepository.save(order);
		
		log.info("Order placed successfully with orderId: {}",order.getOrderId());
		return order.getOrderId();
	}

	@Override
	public OrderResponse getOrderDetailsByOrderId(long orderId) {
		
		log.info("Get order details for Order Id : {}", orderId);
		log.info("ProductServiceUrl: {}",productServiceUrl);
		log.info("PaymentServiceUrl: {}",paymentServiceUrl);

        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                        "NOT_FOUND",
                        404));

        log.info("Invoking Product service to fetch the product for id: {}", order.getProductId());
        ProductResponse productResponse
                = restTemplate.getForObject(
                        productServiceUrl + order.getProductId(),
                ProductResponse.class
        );

		log.info("Product response received : {}",productResponse);

        log.info("Getting payment information from the payment Service");
        
        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        paymentServiceUrl+"order/" + order.getOrderId(),
                PaymentResponse.class
                );
        log.info("Payment response received : {}.",paymentResponse);
        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
				.price(productResponse.getPrice())
				.quantity(productResponse.getQuantity())
                .build();
        
        
        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();
        
        
		OrderResponse orderResponse = OrderResponse.builder()
				.amount(order.getAmount())
				.orderDate(order.getOrderDate())
				.orderId(order.getOrderId())
				.orderStatus(order.getOrderStatus())
				.productDetails(productDetails)
                .paymentDetails(paymentDetails)
				.build();
        log.info("Returning OrderResponse object !");

		return orderResponse;
	}

}
