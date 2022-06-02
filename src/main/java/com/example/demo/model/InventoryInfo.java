package com.example.demo.model;

public class InventoryInfo {
    private String itemDescription;
    private Long stockLeft;

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Long getStockLeft() {
        return stockLeft;
    }

    public void setStockLeft(Long stockLeft) {
        this.stockLeft = stockLeft;
    }
}
