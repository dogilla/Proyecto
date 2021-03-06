package com.product.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.entity.Product;
import com.product.api.dto.ApiResponse;
import com.product.api.dto.DtoProductList;
import com.product.api.entity.Category;
import com.product.api.service.SvcProduct;
import com.product.exception.ApiException;

@RestController
@RequestMapping("/product")
public class CtrlProduct {

	private final SvcProduct productService;
	@Autowired
	CtrlProduct(SvcProduct productService) {
		this.productService = productService;
	}
	
	@Autowired
	SvcProduct svc;
	
	@GetMapping("/category/{category_id}")
	public ResponseEntity<List<DtoProductList>> getProducts(@PathVariable("category_id") Integer category_id){		
		return new ResponseEntity<List<DtoProductList>>(svc.getProducts(category_id), HttpStatus.OK);
	}
	
	@GetMapping("/{gtin}")
	public ResponseEntity<Product> getProduct(@PathVariable("gtin") String gtin){
		 return new ResponseEntity<Product>(svc.getProduct(gtin), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody Product in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<ApiResponse>(svc.createProduct(in), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody Product in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<ApiResponse>(svc.updateProduct(in, id), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Integer id){
		return new ResponseEntity<ApiResponse>(svc.deleteProduct(id), HttpStatus.OK);
	}
	
	@PutMapping(path = "/{product_id}/{quantity}")
	ResponseEntity<ApiResponse> updateProductStock(@PathVariable(value = "product_id") int productId,
												@PathVariable(value = "quantity") int quantity) {
		try {
			ApiResponse response = productService.updateProductStock(productId, quantity);
		}catch(DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("stock"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "we have not enough stock");
		}
		return new ResponseEntity<>(new ApiResponse("stock updated"), HttpStatus.OK);
	}	
	
	@PutMapping("/{product_id}/category")
	public ResponseEntity<ApiResponse> updateProductCategory(@PathVariable ("product_id") Integer product_id, 
															 @Valid @RequestBody Category category, 
															 BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<ApiResponse>(svc.updateProductCategory(category, product_id), HttpStatus.OK);
	}
	
	
}
