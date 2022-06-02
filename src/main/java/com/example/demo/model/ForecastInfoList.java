package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class ForecastInfoList {
    private List<ForecastInfo> forecastInfoList = new ArrayList<>();

    public List<ForecastInfo> getForecastInfoList() {
        return forecastInfoList;
    }

    public void setForecastInfoList(List<ForecastInfo> forecastInfoList) {
        this.forecastInfoList = forecastInfoList;
    }
}
