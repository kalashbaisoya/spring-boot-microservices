package io.codejava.OrderService;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceConfig {

	
//	By using this OrderServiceConfig class as a @TestConfiguration,
//	we can customize the bean creation process during testing.
//	In this case, it overrides the default ServiceInstanceListSupplier
//	bean with your custom implementation, TestServiceInstanceListSupplier.

    @Bean
    public ServiceInstanceListSupplier supplier() {
        return new TestServiceInstanceListSupplier();
    }
}