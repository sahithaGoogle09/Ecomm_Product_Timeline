package com.example.demo.service;

import com.google.cloud.aiplatform.util.ValueConverter;
import com.google.cloud.aiplatform.v1.AcceleratorType;
import com.google.cloud.aiplatform.v1.BatchDedicatedResources;
import com.google.cloud.aiplatform.v1.BatchPredictionJob;
import com.google.cloud.aiplatform.v1.GcsDestination;
import com.google.cloud.aiplatform.v1.GcsSource;
import com.google.cloud.aiplatform.v1.JobServiceClient;
import com.google.cloud.aiplatform.v1.JobServiceSettings;
import com.google.cloud.aiplatform.v1.LocationName;
import com.google.cloud.aiplatform.v1.MachineSpec;
import com.google.cloud.aiplatform.v1.ModelName;
import com.google.protobuf.Value;
import java.io.IOException;

public class CreateBatchPredictionJobSample {
    public void predict() throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String project = "sahitha";
        String displayName = "java-bv-predictions";
//        String modelName = "liqforecast3";
        String modelName = "3224634507801919488";
        String instancesFormat = "bigquery";
        String gcsSourceUri = "GCS_SOURCE_URI";
        String predictionsFormat = "PREDICTIONS_FORMAT";
        String gcsDestinationOutputUriPrefix = "GCS_DESTINATION_OUTPUT_URI_PREFIX";
        createBatchPredictionJobSample(
                project,
                displayName,
                modelName,
                instancesFormat,
                gcsSourceUri,
                predictionsFormat,
                gcsDestinationOutputUriPrefix);
    }

    static void createBatchPredictionJobSample(
            String project,
            String displayName,
            String model,
            String instancesFormat,
            String gcsSourceUri,
            String predictionsFormat,
            String gcsDestinationOutputUriPrefix)
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

            // Passing in an empty Value object for model parameters
            Value modelParameters = ValueConverter.EMPTY_VALUE;

            GcsSource gcsSource = GcsSource.newBuilder().addUris(gcsSourceUri).build();
            BatchPredictionJob.InputConfig inputConfig =
                    BatchPredictionJob.InputConfig.newBuilder()
                            .setInstancesFormat(instancesFormat)
                            .setGcsSource(gcsSource)
                            .build();
            GcsDestination gcsDestination =
                    GcsDestination.newBuilder().setOutputUriPrefix(gcsDestinationOutputUriPrefix).build();
            BatchPredictionJob.OutputConfig outputConfig =
                    BatchPredictionJob.OutputConfig.newBuilder()
                            .setPredictionsFormat(predictionsFormat)
                            .setGcsDestination(gcsDestination)
                            .build();
            MachineSpec machineSpec =
                    MachineSpec.newBuilder()
                            .setMachineType("n1-standard-2")
                            .setAcceleratorType(AcceleratorType.NVIDIA_TESLA_K80)
                            .setAcceleratorCount(1)
                            .build();
            BatchDedicatedResources dedicatedResources =
                    BatchDedicatedResources.newBuilder()
                            .setMachineSpec(machineSpec)
                            .setStartingReplicaCount(1)
                            .setMaxReplicaCount(1)
                            .build();
            String modelName = ModelName.of(project, location, model).toString();
            BatchPredictionJob batchPredictionJob =
                    BatchPredictionJob.newBuilder()
                            .setDisplayName(displayName)
                            .setModel(modelName)
                            .setModelParameters(modelParameters)
                            .setInputConfig(inputConfig)
                            .setOutputConfig(outputConfig)
                            .setDedicatedResources(dedicatedResources)
                            .build();
            LocationName parent = LocationName.of(project, location);
            BatchPredictionJob response = client.createBatchPredictionJob(parent, batchPredictionJob);
            System.out.format("response: %s\n", response);
            System.out.format("\tName: %s\n", response.getName());
        }
    }
}
