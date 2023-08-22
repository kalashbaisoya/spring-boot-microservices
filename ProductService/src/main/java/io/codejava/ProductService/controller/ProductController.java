package io.codejava.ProductService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.codejava.ProductService.model.ProductRequest;
import io.codejava.ProductService.model.ProductResponse;
import io.codejava.ProductService.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PreAuthorize("hasAuthority('Admin')")
	@PostMapping
	public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
		
		long productId = productService.addProduct(productRequest);
		
		return new ResponseEntity <>(productId,HttpStatus.CREATED);
		
	}
	
	@GetMapping
	public ResponseEntity<ProductResponse> getAllProduct(){
		
		List<ProductResponse> response = productService.getAllProduct();
		return null;
	}
	
	@PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long Productid) {
		
		ProductResponse response=productService.getProductById(Productid);
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAuthority('SCOPE_internal')")
	@PutMapping("/reduceQty/{id}")
	public ResponseEntity<Void> reduceQuantity(
			@PathVariable("id") long productId,@RequestParam long quantity) {
		
		productService.reduceQuantity(productId,quantity);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	

}
