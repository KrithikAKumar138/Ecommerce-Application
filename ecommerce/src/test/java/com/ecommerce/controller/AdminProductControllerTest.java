package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminProductController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class AdminProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldShowProductList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(new Product(), new Product()));

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-product-list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void shouldShowAddForm() throws Exception {
        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void shouldSaveProduct() throws Exception {
        MockMultipartFile cover = new MockMultipartFile(
                "coverImageFile", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );
        MockMultipartFile img1 = new MockMultipartFile(
                "image1File", "img1.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );
        MockMultipartFile img2 = new MockMultipartFile(
                "image2File", "img2.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );

        mockMvc.perform(multipart("/admin/products/add")
                        .file(cover)
                        .file(img1)
                        .file(img2)
                        .param("name", "Test Product")
                        .param("price", "100")
                        .param("stock", "10")
                        .param("description", "Test Desc")
                        .param("category", "Test Cat")
                        .with(csrf())
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        verify(productService, times(1))
                .saveProductWithImages(any(Product.class), any(), any(), any());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(get("/admin/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        verify(productService, times(1)).delete(1L);
    }

    @Test
    void shouldShowEditForm() throws Exception {
        Product product = new Product();
        product.setId(1L);

        when(productService.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/admin/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        mockMvc.perform(post("/admin/products/edit/1")
                        .param("name", "Updated Product")
                        .param("price", "200")
                        .with(csrf())
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void shouldViewProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);

        when(productService.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/admin/products/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-product-detail"))
                .andExpect(model().attributeExists("product"));
    }
}
