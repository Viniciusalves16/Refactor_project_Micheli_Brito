package com.example.springTest.controllers;

import com.example.springTest.dtos.ProductRecord;
import com.example.springTest.model.ProductModel;
import com.example.springTest.repository.ProductRepository;
import com.example.springTest.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired //todo:Injeção de dependencia para extrair regras de negocio para a classe service
    ProductService productService;

    @GetMapping("/products")                                     //todo:Implementacao de paginacao
    public ResponseEntity<Page<ProductModel>> getAllProducts(@PageableDefault(size = 2, sort = {"value"}, direction = Sort.Direction.DESC) Pageable p) {
        Page<ProductModel> productsList = productRepository.findAll(p); // recupera todos os produtos
        return productService.getAllProducts(productsList); //todo: Extraindo regras de negocios para a classe service
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Object> findOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productO = productRepository.findById(id);
        return productService.searchOneProduct(productO); //todo: extraindo regra de negocio para a classe service
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecord productRecord) {
        Optional<ProductModel> product = productRepository.findById(id);
        return productService.updateOneProduct(product, productRecord);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity delectProduct(@PathVariable(value = "id") UUID id,
                                        @RequestBody @Valid ProductRecord productRecord) {
        Optional<ProductModel> product = productRepository.findById(id);
        ResponseEntity r = productService.verifyDelete(product); //todo: extraindo regra de negocio para a classe service
        return r;
    }


}
