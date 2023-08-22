package io.codejava.ProductService.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundException extends RuntimeException {

	private String errorCode;
	
	public ProductNotFoundException(String msg,String errorCode) {
		// TODO Auto-generated constructor stub
	 
		super(msg);
		this.errorCode=errorCode;
	}
}
