package com.example.demo.model;

import java.util.Date;

public class ForecastInfo {
    private Date date;
    private String itemDescription;
    private double forecastValue;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(double forecastValue) {
        this.forecastValue = forecastValue;
    }
}
