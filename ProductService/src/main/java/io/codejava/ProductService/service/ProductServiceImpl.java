package io.codejava.ProductService.service;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.codejava.ProductService.entities.Product;
import io.codejava.ProductService.exception.ProductNotFoundException;
import io.codejava.ProductService.model.ProductRequest;
import io.codejava.ProductService.model.ProductResponse;
import io.codejava.ProductService.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public long addProduct(ProductRequest productRequest) {
		log.info("Adding Product..");
		
		Product product = Product.builder()
				.productName(productRequest.getName())
				.quantity(productRequest.getQuantity())
				.price(productRequest.getPrice())
				.build();
		productRepository.save(product);
		
		log.info("Product Created..");
		return product.getProductId();
	}

	@Override
	public long updateProduct(ProductRequest productDto) {
		// TODO Auto-generated method stub
		return (Long) null;
	}

	@Override
	public ProductResponse getProductById(Long id) {
		log.info("Getting Product by Id {}",id);
		Product product = productRepository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product Not found with Id:","PRODUCT_NOT_FOUND"));
		
		ProductResponse productResponse
					= new ProductResponse();
		BeanUtils.copyProperties(product, productResponse);
		
		return productResponse;
	}

	@Override
	public List<ProductResponse> getAllProduct() {
//		List<Product> product= productRepository.findAll();
//		List<ProductResponse> r = product.stream()
//				.map((product)->ProductResponse
//						.builder()
//						.productId(product.getProductId())
//						.productName(product.getProductName())
//						.quantity(product.getQuantity())
//						.price(product.getPrice()).build())
//				.collect(Collectors.toList());
		return null;
	}

	@Override
	public ProductResponse deleteProduct(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reduceQuantity(long productId, long quantity) {
		
		log.info("Reduce quantity by {} of ProductId {}",quantity,productId);
		Product product = productRepository.findById(productId)
				.orElseThrow(()->new ProductNotFoundException("Product Not found with Id:","PRODUCT_NOT_FOUND"));
		
		if(product.getQuantity()>quantity) {
			product.setQuantity(product.getQuantity()-quantity);
			productRepository.save(product);
		}
		else {
			throw new ProductNotFoundException("Product is not available in sufficient quanttity", "PRODUCT_QTY_INSUFFICIENT");
		}
		
		log.info("Product quantity updated successfully ! ");
	}

}
