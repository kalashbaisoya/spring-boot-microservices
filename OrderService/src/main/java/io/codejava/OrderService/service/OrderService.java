package io.codejava.OrderService.service;

import io.codejava.OrderService.models.OrderRequest;
import io.codejava.OrderService.models.OrderResponse;

public interface OrderService {

	Long placeOrder(OrderRequest orderRequest);

	OrderResponse getOrderDetailsByOrderId(long orderId);

}
