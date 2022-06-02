package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

//@Data
//@Accessors(chain = true)
public class Product {
   private List<String> productsList;
   public List<String> getProductsList() { return productsList; }

   // Method 2 - Setter
   public void setProductsList(List<String> N)
   {

      // This keyword refers to current instance itself
      this.productsList = N;
   }
}
