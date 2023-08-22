package io.codejava.OrderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.codejava.OrderService.external.response.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
		extends ResponseEntityExceptionHandler{

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
		
		
		return new ResponseEntity<>(new ErrorResponse().builder()
				.errorMessage(ex.getMessage())
				.errorCode(ex.getErrorCode())
				.build(), HttpStatus.valueOf(ex.getStatus()));
	}
}
