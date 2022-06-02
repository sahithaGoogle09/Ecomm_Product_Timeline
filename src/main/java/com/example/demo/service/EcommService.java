package com.example.demo.service;

import com.example.demo.config.ConnectBQ;
import com.example.demo.model.*;
import com.google.cloud.bigquery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EcommService {
    public static String PROJECT_ID = "sahitha";
    public static String DATASET_NAME = "myliqdata";
    public static String TABLE_NAME_INPUT_DATA = "to-be-trained-liquor-sales-data";
    public static String INVENTORY_INFO_TABLE_NAME = "inventory-info";

    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public Product getProducts() {
        return getProductsFromCloudBQ();
    }

    @Autowired
    public BigQueryPredictionsService bigQueryPredictionsService;

    @Autowired
    public ConnectBQ connectBQ;
    public Product getProductsFromCloudBQ() {
//        String projectId = "sahitha";
//        String datasetName = "myliqdata";
//        String tableName = "to-be-trained-liquor-sales-data";
//		String query =
//				"SELECT name, SUM(number) as total_people\n"
//						+ " FROM `"
//						+ projectId
//						+ "."
//						+ datasetName
//						+ "."
//						+ tableName
//						+ "`"
//						+ " WHERE state = 'TX'"
//						+ " GROUP BY name, state"
//						+ " ORDER BY total_people DESC"
//						+ " LIMIT 20";
        String query = "Select distinct item_description from `"
                + PROJECT_ID
                + "."
                + DATASET_NAME
                + "."
                + TABLE_NAME_INPUT_DATA
                + "`;";
        TableResult result = connectBQ.query(query);
        System.out.println("________________________1___________________");
//        result.iterateAll().forEach(row -> row.forEach(fieldValue -> System.out.println(fieldValue.getStringValue())));
        List<String> listOfProducts = new ArrayList<>();
        if (result != null) {
            result.iterateAll().forEach(row -> row.forEach(fieldValue ->
                    listOfProducts.add(fieldValue.getStringValue())
            ));
        }
        System.out.println(listOfProducts);
        System.out.println("________________________2___________________");
        if (result != null) {
            result.iterateAll().forEach(row -> System.out.println(row.toString()));
        }
//        result.iterateAll().forEach(row -> row.forEach(fieldValue -> new ArrayList<>().add(fieldValue.getValue())));

        Product product = new Product();
        product.setProductsList(listOfProducts);
        System.out.println(product.toString());
        return product;
    }

    public InventoryInfoList getInventoryInfo() {
        String query = "Select item_description, stock_left from `"
                + PROJECT_ID
                + "."
                + DATASET_NAME
                + "."
                + INVENTORY_INFO_TABLE_NAME
                + "` order by item_description;";
        TableResult result = connectBQ.query(query);
//        result.iterateAll().forEach(row->row.);
        InventoryInfoList inventoryInfoList = new InventoryInfoList();
        List<InventoryInfo> inventoryInfos = new ArrayList<>();

        result.iterateAll().forEach(row -> {
            InventoryInfo inventoryInfo = new InventoryInfo();
            System.out.println(row.toString());
            inventoryInfo.setItemDescription(row.get(0).getStringValue());
            inventoryInfo.setStockLeft(Long.parseLong(row.get(1).getValue().toString()));
            System.out.println("LOOOPPPP Inventory ");
            System.out.println(inventoryInfo.getItemDescription() +"???"+inventoryInfo.getStockLeft());
            inventoryInfoList.getInventoryInfoList().add(inventoryInfo);
////            inventoryInfo.setItemDescription();
//            inventoryInfos.add(inventoryInfo);
////            inventoryInfoList.getInventoryInfoList().add(inventoryInfo);
//            System.out.println(inventoryInfos);
        });

//        inventoryInfoList.setInventoryInfoList(inventoryInfos);
        return inventoryInfoList;
    }

    public ForecastInfoList getPredictions(String productName) {
//        String date = simpleDateFormat.format();
//        System.out.println(date);

        String PREDICTIONS_TABLE_NAME = "predictions_2022_05_04T12_42_27_123Z";
        String query = "Select * from `"
                + PROJECT_ID
                + "."
                + DATASET_NAME
                + "."
                + PREDICTIONS_TABLE_NAME
                + "` where item_description='" + productName + "' order by date;";
        TableResult result = connectBQ.query(query);
        System.out.println("____________________PREDICTIONS 1_____________________________________");
        result.iterateAll().forEach(row -> System.out.println(row.get(0).getValue()));


        System.out.println("____________________PREDICTIONS 2_____________________________________");
        ForecastInfoList forecastInfoList = new ForecastInfoList();


        result.iterateAll().forEach(row -> {
            ForecastInfo forecastInfo = new ForecastInfo();
            try {
                forecastInfo.setDate(simpleDateFormat.parse(row.get(0).getValue().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            forecastInfo.setItemDescription(row.get(1).getValue().toString());
//            forecastInfo.setForecastValue(row.get(4).getValue());
            forecastInfo.setForecastValue(Double.parseDouble(row.get(3).getRecordValue().get(0).getValue().toString()));
            System.out.println(forecastInfo.getDate().toString() + " ***** " + forecastInfo.getItemDescription() + " **** " + forecastInfo.getForecastValue());
            forecastInfoList.getForecastInfoList().add(forecastInfo);
        });
        result.iterateAll().forEach(row -> System.out.println(row.get(3).getRecordValue().get(0).getValue().toString()));
//        result.iterateAll().forEach(row -> row.forEach(fieldValue -> System.out.println(fieldValue.getStringValue())));
        return forecastInfoList;
    }

//    public static TableResult query(String query) {
//        try {
//            // Initialize client that will be used to send requests. This client only needs to be created
//            // once, and can be reused for multiple requests.
//            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
//
//            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
//
//            TableResult results = bigquery.query(queryConfig);
//
//            results
//                    .iterateAll()
//                    .forEach(row -> row.forEach(val -> System.out.printf("%s,", val.toString())));
//
//            System.out.println("Query performed successfully.");
//            return results;
//        } catch (BigQueryException | InterruptedException e) {
//            System.out.println("Query not performed \n" + e.toString());
//        }
//        return null;
//    }

    public void findStockInDays(String productName) {

        InventoryInfoList inventoryInfoList = getInventoryInfo();

        Long stockLeft = 0L;
        System.out.println("LAST INVENTORY INFO FETCH)))))))))))))))))))))");
        for (InventoryInfo inventoryInfo : inventoryInfoList.getInventoryInfoList()) {

            System.out.println(inventoryInfo.getItemDescription() +"  "+inventoryInfo.getStockLeft());
            if (inventoryInfo.getItemDescription().equals(productName)) {
                stockLeft = inventoryInfo.getStockLeft();
            }
        }
        System.out.println("Stock In the inventory: " + stockLeft);


        ForecastInfoList forecastInfoList = getPredictions(productName);

        Long tempSum = 0L;
        Long noOfDaysInStock = 0L;
//        forecastInfoList.getForecastInfoList().stream().forEach(forecastInfo -> {
//            if (stockLeft < tempSum) {
//                tempSum = tempSum + Double.valueOf(forecastInfo.getForecastValue()).longValue();
//                noOfDaysInStock = noOfDaysInStock + 1;
//            }
//        });
        System.out.println("LAST FORECAST INFO FETCH");
        for (ForecastInfo forecastInfo : forecastInfoList.getForecastInfoList()) {

            System.out.println(forecastInfo.getDate() +"  "+forecastInfo.getItemDescription()+"   "+forecastInfo.getForecastValue());
            if (stockLeft > tempSum) {
                System.out.println("Stock left"+stockLeft);
                System.out.println("Tempsum:"+tempSum);
                tempSum = tempSum + Double.valueOf(forecastInfo.getForecastValue()).longValue();
                noOfDaysInStock = noOfDaysInStock + 1;
            }
        }
        System.out.println("No of Days In Stock expected for Black Velvet: "+noOfDaysInStock);

        ProductStockInfo productStockInfo = new ProductStockInfo();
        productStockInfo.setItem_description("Black Velvet");
        productStockInfo.setNoOfDaysInStock(noOfDaysInStock.toString());
    }

    public void findStockInDaysAll() {

    }

}
