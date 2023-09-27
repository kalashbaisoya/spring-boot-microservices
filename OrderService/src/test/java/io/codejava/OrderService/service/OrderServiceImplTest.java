package io.codejava.OrderService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
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
import io.codejava.OrderService.models.PaymentMode;
import io.codejava.OrderService.repository.OrderRepository;

@SpringBootTest
public class OrderServiceImplTest {

    private final CustomLogger customLogger = new CustomLogger();

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@Mock
	private PaymentService paymentService;

	@Mock
	private RestTemplate restTemplate;

    @Value("${microservices.product}")
    private String productServiceUrl;
    @Value("${microservices.payment}")
    private String paymentServiceUrl;


    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(orderService,"paymentServiceUrl",paymentServiceUrl);

        ReflectionTestUtils.setField(orderService,"productServiceUrl",productServiceUrl);
    }

    @AfterEach
    public void logTestDetails() {
        customLogger.log("Test completed: {}", this.getClass().getName());
    }

    @DisplayName("Get Order - Success Scenario")
    @Test
    void test_When_Order_Success() {
        //Mocking
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                productServiceUrl + order.getProductId(),
                ProductResponse.class
        )).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject(
                paymentServiceUrl+"order/" + order.getOrderId(),
                PaymentResponse.class
        )).thenReturn(getMockPaymentResponse());

        //Actual
        OrderResponse orderResponse = orderService.getOrderDetailsByOrderId(1);

        //Verification
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                productServiceUrl + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                paymentServiceUrl+"order/" + order.getOrderId(),
                PaymentResponse.class);


        //Assert
        assertNotNull(orderResponse);
        assertEquals(order.getOrderId(), orderResponse.getOrderId());
    }

    @DisplayName("Get Orders - Failure Scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Found() {

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        CustomException exception =
                assertThrows(CustomException.class,
                        () -> orderService.getOrderDetailsByOrderId(1));
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(orderRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success() {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        // defining the functions save(), reduceQuantity and doPayment
        // we can also call this mimicking these functions
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));

        //here is the actual method that is being tested
        // here we are testing placeOrder() method from orderService
        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);
    }

    @DisplayName("Place Order - Payment Failed Scenario")
    @Test
    void test_when_Place_Order_Payment_Fails_then_Order_Placed() {

        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId((long) 1)
                .quantity((long) 10)
                .paymentMode(PaymentMode.CASH)
                .amount((long) 100)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("iPhone")
                .price(100)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .orderId((long) 1)
                .amount((long) 100)
                .quantity((long) 200)
                .productId((long) 2)
                .build();
    }

}

class CustomLogger {
    public void log(String message, Object... args) {
        // Replace this with your preferred logging mechanism (e.g., SLF4J, log4j, System.out)
        System.out.printf(message + "%n", args);
    }
}
