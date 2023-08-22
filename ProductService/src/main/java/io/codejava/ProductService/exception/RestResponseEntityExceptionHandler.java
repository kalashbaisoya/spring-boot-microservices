package io.codejava.ProductService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.codejava.ProductService.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
		extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> productNotFound(ProductNotFoundException ex){
		
		
		return new ResponseEntity<>(new ErrorResponse().builder()
				.errorMessage(ex.getMessage())
				.errorCode(ex.getErrorCode())
				.build(), HttpStatus.NOT_FOUND);
	}
}
