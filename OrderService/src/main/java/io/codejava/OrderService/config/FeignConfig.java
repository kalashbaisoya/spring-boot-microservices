package io.codejava.OrderService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;
import io.codejava.OrderService.external.decoder.CustomErrorDecoder;

@Configuration
public class FeignConfig {

	
	@Bean
	ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder();
	}
}
