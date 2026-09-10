package com.wedding.gallery.service.impl;

import com.wedding.gallery.config.CloudflareR2Access;
import com.wedding.gallery.service.CloudflareR2Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
public class CloudflareR2ServiceImpl implements CloudflareR2Service {

    private final CloudflareR2Access r2Access;
    private final S3Client s3Client;

    public CloudflareR2ServiceImpl(CloudflareR2Access r2Access) {
        this.r2Access = r2Access;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(r2Access.getAccessKey(), r2Access.getSecretKey());

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(r2Access.getEndpoint()))
                .serviceConfiguration(serviceConfiguration)
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * Method reusable untuk upload file apa saja ke Cloudflare R2
     */
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {

            String keyName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");

            PutObjectRequest putObjectRequestBuilder = PutObjectRequest.builder()
                    .bucket(r2Access.getBucketName())
                    .key(keyName)
                    .contentType(file.getContentType()).build();

            s3Client.putObject(
                    putObjectRequestBuilder,
                    RequestBody.fromBytes(file.getBytes())
            );

            return String.format("%s", keyName);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal upload file ke storage: " + e.getMessage());
        }
    }
}