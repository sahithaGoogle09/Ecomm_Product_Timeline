package com.example.demo.service;

import com.google.cloud.aiplatform.v1.BatchPredictionJob;
import com.google.cloud.aiplatform.v1.BigQueryDestination;
import com.google.cloud.aiplatform.v1.BigQuerySource;
import com.google.cloud.aiplatform.v1.JobServiceClient;
import com.google.cloud.aiplatform.v1.JobServiceSettings;
import com.google.cloud.aiplatform.v1.LocationName;
import com.google.cloud.aiplatform.v1.ModelName;
import com.google.gson.JsonObject;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BigQueryPredictionsService {

    public void batchPredictions() throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String project = "sahitha";
        String displayName = "java-bv-predictions09";
//        String modelName = "liqforecast3";
        String modelName = "3224634507801919488";
        String instancesFormat = "bigquery";
        String bigquerySourceInputUri = "bq://sahitha.myliqdata.input-data";
        String predictionsFormat = "bigquery";
        String bigqueryDestinationOutputUri = "bq://sahitha.predictionsliq";
        createBatchPredictionJobBigquerySample(
                project,
                displayName,
                modelName,
                instancesFormat,
                bigquerySourceInputUri,
                predictionsFormat,
                bigqueryDestinationOutputUri);
    }

    public void createBatchPredictionJobBigquerySample(
            String project,
            String displayName,
            String model,
            String instancesFormat,
            String bigquerySourceInputUri,
            String predictionsFormat,
            String bigqueryDestinationOutputUri)
            throws IOException {
        JobServiceSettings settings =
                JobServiceSettings.newBuilder()
                        .setEndpoint("us-central1-aiplatform.googleapis.com:443")
                        .build();
        String location = "us-central1";

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (JobServiceClient client = JobServiceClient.create(settings)) {
            JsonObject jsonModelParameters = new JsonObject();
            Value.Builder modelParametersBuilder = Value.newBuilder();
            JsonFormat.parser().merge(jsonModelParameters.toString(), modelParametersBuilder);
            Value modelParameters = modelParametersBuilder.build();
            BigQuerySource bigquerySource =
                    BigQuerySource.newBuilder().setInputUri(bigquerySourceInputUri).build();
            BatchPredictionJob.InputConfig inputConfig =
                    BatchPredictionJob.InputConfig.newBuilder()
                            .setInstancesFormat(instancesFormat)
                            .setBigquerySource(bigquerySource)
                            .build();
            BigQueryDestination bigqueryDestination =
                    BigQueryDestination.newBuilder().setOutputUri(bigqueryDestinationOutputUri).build();
            BatchPredictionJob.OutputConfig outputConfig =
                    BatchPredictionJob.OutputConfig.newBuilder()
                            .setPredictionsFormat(predictionsFormat)
                            .setBigqueryDestination(bigqueryDestination)
                            .build();
            String modelName = ModelName.of(project, location, model).toString();
            BatchPredictionJob batchPredictionJob =
                    BatchPredictionJob.newBuilder()
                            .setDisplayName(displayName)
                            .setModel(modelName)
                            .setModelParameters(modelParameters)
                            .setInputConfig(inputConfig)
                            .setOutputConfig(outputConfig)
                            .build();
            LocationName parent = LocationName.of(project, location);
            BatchPredictionJob response = client.createBatchPredictionJob(parent, batchPredictionJob);
            System.out.format("response: %s\n", response);
            System.out.format("\tName: %s\n", response.getName());
        }
    }
}