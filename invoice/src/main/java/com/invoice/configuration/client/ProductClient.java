package com.invoice.configuration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.invoice.api.dto.DtoProduct;


/*
 * Sprint 2 - Requerimiento 2
 * Actualizar el metodo getProduct
 */
@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("product/{gtin}")
	public ResponseEntity<DtoProduct> getProduct(@PathVariable("gtin") String gtin);

}

/*
 * Sprint 3 - Requerimiento 5
 * Agregar método updateProductStock para actualizar el stock de productos
 */
