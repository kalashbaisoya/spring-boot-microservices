package io.codejava.ProductService.service;

import java.util.List;

import org.springframework.stereotype.Service;
import io.codejava.ProductService.model.ProductRequest;
import io.codejava.ProductService.model.ProductResponse;

@Service
public interface ProductService {
	
	long addProduct(ProductRequest productDto);
	long updateProduct(ProductRequest productDto);
	ProductResponse getProductById(Long id);
	List<ProductResponse> getAllProduct();
	ProductResponse deleteProduct(Long id);
	void reduceQuantity(long productId, long quantity);
	
}
