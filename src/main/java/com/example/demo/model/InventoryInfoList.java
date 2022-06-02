package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class InventoryInfoList {
    private List<InventoryInfo> inventoryInfoList = new ArrayList<>();

    public List<InventoryInfo> getInventoryInfoList() {
        return inventoryInfoList;
    }

    public void setInventoryInfoList(List<InventoryInfo> inventoryInfoList) {
        this.inventoryInfoList = inventoryInfoList;
    }
}
