package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoList {
    private List<ProductInfoVO> productInfoVOS = new ArrayList<>();

    public List<ProductInfoVO> getProductInfoVOS() {
        return productInfoVOS;
    }

    public void setProductInfoVOS(List<ProductInfoVO> productInfoVOS) {
        this.productInfoVOS = productInfoVOS;
    }
}
