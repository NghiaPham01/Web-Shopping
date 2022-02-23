package com.mockproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mockproject.entity.Products;
import com.mockproject.repository.ProductsRepo;
import com.mockproject.service.impl.ProductsServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ProductsServiceTest {
	
	@Mock
	private ProductsRepo repo;
	
	@InjectMocks
	private ProductsServiceImpl productService;

	@Test
	public void testFindById_WithExistProductId() throws Exception {
		Products expectedProduct = new Products();
		expectedProduct.setId(1L);
		expectedProduct.setName("product");
		// khi goi vao productService.findById(1L) -> mong muon tra ve expectedProduct
		when(repo.findById(1L)).thenReturn(Optional.of(expectedProduct));
		
		try {
			Products actualProduct = productService.findById(1L);
			assertEquals(expectedProduct.getId(), actualProduct.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Test case failed");
		}
	}
}
