package com.example.java_base.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class AwsMain {
    public static void main(String[] args) {

        String accessKey="SFNEXGZMTWIGWLNYLVTH";
        String secretKey="rveSZLHZAEr57zcnRd5I2xkd0GVUqCwPP0gRYlRe";
//        String endPointString="https://s3.qingstor.com";
        String endPointString="https://s3.sh1a.qingstor.com";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        AwsClientBuilder.EndpointConfiguration endPoint =new AwsClientBuilder.
                EndpointConfiguration(endPointString, "sh1a");
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endPoint)
                .build();

        Bucket bucket = null;
        String bucketName="test-zhs-003";
        CreateBucketRequest createBucketRequest =new CreateBucketRequest(bucketName, "sh1a");
        bucket = amazonS3.createBucket(createBucketRequest);
    }
}
