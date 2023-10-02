package com.snaacker.sample.controller;

import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.service.ProductService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
  private ProductService productService;
  @Autowired
  public ProductController(ProductService productService){
    this.productService = productService;
  }
  @GetMapping("/product")
  public ResponseEntity<List<ProductResponse>> getProductsByProductFeedId(@PathVariable Long productId) {
    return new ResponseEntity<>(productService.getProductsByProductFeedId(productId), HttpStatus.OK);
  }

  @PostMapping("/product")
  public ResponseEntity<String> uploadXML(@RequestParam("file") MultipartFile multipartFile)
      throws IOException {
    return new ResponseEntity<>(productService.loadProducts(multipartFile), HttpStatus.OK);
  }
}