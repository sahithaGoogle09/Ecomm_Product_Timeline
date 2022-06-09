package com.example.demo.controllers;

import com.example.demo.model.Product;
import com.example.demo.model.ProductInfoList;
import com.example.demo.service.EcommService;
import com.example.demo.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class EcommController {

    @Autowired
    public EcommService ecommService;

    @Autowired
    public ProductsService productsService;

    @GetMapping("/getProducts")
    public Product getProducts() {
        Product product = new Product();
        product = ecommService.getProducts();
        System.out.println(product);
        return product;
    }

    @GetMapping("/getListOfproducts")
    public ProductInfoList getProductsInfoList() {
        ProductInfoList productInfoList = new ProductInfoList();
//        productsService.findStockInDays();
        productInfoList.setProductInfoVOS(productsService.findStockInDays());
        return productInfoList;
    }

    @PostMapping("/updateStock")
    public void updateStockOfProduct(@RequestParam Integer productId, @RequestParam Integer unitsBought) {
        productsService.updateStockAfterBought(productId, unitsBought);
    }
}
