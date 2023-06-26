package com.example.usercenter.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutBucketImageRequest;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class UploadUtil {

    private static String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    private static String BUCKET_NAME = "xiancai-image-bucket";
    private static String ACCESS_KEY_ID = "LTAI5tRZqknyFHzyE3YZWJn6";
    private static String ACCESS_KEY_SECRET = "pqHGMG1rzURpTi5StQV4nsOiVhotyT";
    private static OSS ossClient;

    static {
        try {
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public static String uploadImage(MultipartFile multipartFile, String fileName) {
        InputStream in = null;
        try {
            in = multipartFile.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpg");
            ossClient.putObject(BUCKET_NAME, fileName, in, metadata);
            String uploadUrl = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + fileName;
            return uploadUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
