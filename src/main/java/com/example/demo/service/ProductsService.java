package com.example.demo.service;

import com.example.demo.config.ApplicationConstants;
import com.example.demo.config.ConnectBQ;
import com.example.demo.model.*;
import com.google.cloud.bigquery.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.service.EcommService.*;

@Service
public class ProductsService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ApplicationConstants.pattern);
    @Autowired
    public ConnectBQ connectBQ;

    @Autowired
    public BigQueryPredictionsService bigQueryPredictionsService;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public List<ProductInfoDTO> getProductsFromCloudBQ() {
        String query = "Select item_description, cost_in_rs, imageUrl, stock_left from `"
                + ApplicationConstants.PROJECT_ID
                + "."
                + ApplicationConstants.INPUT_DATASET_NAME
                + "."
                + ApplicationConstants.TABLE_NAME_PRODUCTS
                + "`;";

        TableResult result = connectBQ.query(query);
        System.out.println("________________________1___________________");
//        result.iterateAll().forEach(row -> row.forEach(fieldValue -> System.out.println(fieldValue.getStringValue())));
        List<ProductInfoDTO> listOfProducts = new ArrayList<>();

        result.iterateAll().forEach(row -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            System.out.println(row.toString());
            productInfoDTO.setName(row.get(0).getStringValue());
            productInfoDTO.setCost(Double.parseDouble(row.get(1).getValue().toString()));
            productInfoDTO.setImageUrl(row.get(2).getValue().toString());
            productInfoDTO.setStockLeft(Integer.parseInt(row.get(3).getValue().toString()));
            System.out.println("LOOOPPPP Inventory ");
            System.out.println(productInfoDTO.getName() + "??? " + productInfoDTO.getCost() + "??? " + productInfoDTO.getStockLeft());
            listOfProducts.add(productInfoDTO);
        });
        return listOfProducts;
    }

    public ForecastInfoList getPredictions() {
//        String date = simpleDateFormat.format();
//        System.out.println(date);

        String PREDICTIONS_TABLE_NAME = "predictions_2022_05_30T12_31_34_303Z_494";
        String query = "Select DATE(date), item_description, predicted_total_bottles_sold from `"
                + ApplicationConstants.PROJECT_ID
                + "."
                + ApplicationConstants.PREDICTIONS_DATASET_NAME
                + "."
                + PREDICTIONS_TABLE_NAME
                + "` order by date;";
        TableResult result = connectBQ.query(query);
//        System.out.println("____________________PREDICTIONS 1_____________________________________");
//        result.iterateAll().forEach(row -> System.out.println(row.get(0).getValue()));


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
            forecastInfo.setForecastValue(Double.parseDouble(row.get(2).getRecordValue().get(0).getValue().toString()));
            System.out.println(forecastInfo.getDate().toString() + " ***** " + forecastInfo.getItemDescription() + " **** " + forecastInfo.getForecastValue());
            forecastInfoList.getForecastInfoList().add(forecastInfo);
        });
//        result.iterateAll().forEach(row -> System.out.println(row.get(3).getRecordValue().get(0).getValue().toString()));
//        result.iterateAll().forEach(row -> row.forEach(fieldValue -> System.out.println(fieldValue.getStringValue())));
        return forecastInfoList;
    }

    public List<ProductInfoVO> findStockInDays() {

        List<ProductInfoVO> productInfoVOS = new ArrayList<>();
        List<ProductInfoDTO> productInfoDTOS = getProductsFromCloudBQ();

        Map<String, List<ForecastInfo>> predictionsMap =
                getPredictions().getForecastInfoList().stream().collect(Collectors.groupingBy(ForecastInfo::getItemDescription));

        for (ProductInfoDTO productInfoDTO : productInfoDTOS) {
            System.out.println("In productInfoDTOS list***********************************************");
            ProductInfoVO productInfoVO = new ProductInfoVO();
            var ref = new Object() {
                double tempSum = 0;

                int noOfDaysInStock = 0;
            };


//            predictionsMap.get(productInfoDTO.getName()).stream().
//                    filter(forecastInfo -> forecastInfo.getDate().after(new Date())).
//                    sorted(Comparator.comparing(ForecastInfo::getDate)).forEach(forecastInfo -> System.out.println(forecastInfo.getItemDescription()));

            //sort by date TODO
//            predictionsMap.get(productInfoDTO.getName()).stream().filter(forecastInfo -> forecastInfo.getDate().after(new Date())).sorted(Comparator.comparing(forecastInfo -> forecastInfo.getDate()));
//            predictionsMap.get(productInfoDTO.getName()).sort(Comparator.comparing(ForecastInfo::getDate));
//            predictionsMap.get(productInfoDTO.getName()).forEach(forecastInfo -> {
            predictionsMap.get(productInfoDTO.getName()).stream().
                    filter(forecastInfo -> {
                        try {
                            return forecastInfo.getDate().after(simpleDateFormat.parse("2022-04-27"));//replace with new date TODO
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }).
                    sorted(Comparator.comparing(ForecastInfo::getDate)).forEach(forecastInfo -> {
                        System.out.println(forecastInfo.getDate() + "  " + forecastInfo.getItemDescription() + "   " + forecastInfo.getForecastValue());
                        if (productInfoDTO.getStockLeft() > ref.tempSum) {
                            System.out.println("Stock left" + productInfoDTO.getStockLeft());
                            System.out.println("Tempsum:" + ref.tempSum);
                            ref.tempSum = ref.tempSum + Double.valueOf(forecastInfo.getForecastValue()).longValue();
                            ref.noOfDaysInStock = ref.noOfDaysInStock + 1;
                            System.out.println("No of days in stock: " + ref.noOfDaysInStock);
                        }

                    });
            productInfoVO.setProductName(productInfoDTO.getName());
            productInfoVO.setProductPrice(Double.parseDouble(df.format(productInfoDTO.getCost())));
            productInfoVO.setProductImage(productInfoDTO.getImageUrl());
            if (ref.noOfDaysInStock < 7) {
                productInfoVO.setLikelyToBeSold(true);
                productInfoVO.setLikelyToBeSoldDays(ref.noOfDaysInStock);
            } else {
                productInfoVO.setLikelyToBeSold(false);
            }

//            if(ref.noOfDaysInStock < 4){
//                productInfoVO.setPriorityToRestock("High");
//            } else if (ref.noOfDaysInStock < 6) {
//                productInfoVO.setPriorityToRestock("Medium");
//            }else if((ref.noOfDaysInStock < 7)) {
//                productInfoVO.setPriorityToRestock("Low");
//            }
            productInfoVOS.add(productInfoVO);
        }

        for (ProductInfoVO productInfoVO : productInfoVOS) {
            System.out.println("Product Name: " + productInfoVO.getProductName() + "  cost: "
                    + productInfoVO.getProductPrice() + "  ProductTobeSold(yes/no): " + productInfoVO.isLikelyToBeSold()
                    + " Likely To be sold in: " + productInfoVO.getLikelyToBeSoldDays());
        }
        return productInfoVOS;

    }

    public void initiatePrediction() throws IOException {
        String project = "sahitha";
        String displayName = "java-bv-predictions_overall";
//        String modelName = "liqforecast3";
        String modelName = "505164020311916544";
        String instancesFormat = "bigquery";
        String bigquerySourceInputUri = "bq://sahitha.inputs.input-overall-data-20";
        String predictionsFormat = "bigquery";
        String bigqueryDestinationOutputUri = "bq://sahitha.predictionsliq";
        bigQueryPredictionsService.createBatchPredictionJobBigquerySample(
                project,
                displayName,
                modelName,
                instancesFormat,
                bigquerySourceInputUri,
                predictionsFormat,
                bigqueryDestinationOutputUri);
    }

    public void insertInputForPredictionInBQ() {
        //run every 7 days

    }
}
