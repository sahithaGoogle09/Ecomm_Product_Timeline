package com.example.demo.model;

public class ProductInfoVO {
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    private Integer productId;
    private String productName;
    private String productImage;
    private double productPrice;
    private boolean isLikelyToBeSold;
    private Integer likelyToBeSoldDays;

    private String priorityToRestock;

    private Boolean outOfStock;

    public Boolean getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getPriorityToRestock() {
        return priorityToRestock;
    }

    public void setPriorityToRestock(String priorityToRestock) {
        this.priorityToRestock = priorityToRestock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isLikelyToBeSold() {
        return isLikelyToBeSold;
    }

    public void setLikelyToBeSold(boolean likelyToBeSold) {
        isLikelyToBeSold = likelyToBeSold;
    }

    public Integer getLikelyToBeSoldDays() {
        return likelyToBeSoldDays;
    }

    public void setLikelyToBeSoldDays(Integer likelyToBeSoldDays) {
        this.likelyToBeSoldDays = likelyToBeSoldDays;
    }
}
