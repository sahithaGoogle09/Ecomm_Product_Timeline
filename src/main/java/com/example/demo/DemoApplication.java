package com.example.demo;

import com.example.demo.service.BigQueryPredictionsService;
import com.example.demo.service.EcommService;
import com.example.demo.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.example.demo"})
@Configuration
public class DemoApplication implements CommandLineRunner {

    @Autowired
    public EcommService ecommService;

    public static BigQueryPredictionsService bigQueryPredictionsService;

    @Autowired
    public ProductsService productsService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("hoi");
//insert inventory info periodically
        //predict next 7 days data and store in bigquery

//		ecommService.getProducts();
//		ecommService.getPredictions("Black Velvet");
//		ecommService.getInventoryInfo();
//		ecommService.findStockInDays("Black Velvet");
//		bigQueryPredictionsService.batchPredictions();
//        productsService.getProductsFromCloudBQ();

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("hhhhhhhhhhhhhhhhh");
//        productsService.getProductsFromCloudBQ();
//        ecommService.findStockInDays("Black Velvet");
//        productsService.initiatePrediction();
//        productsService.findStockInDays();
    }

}
