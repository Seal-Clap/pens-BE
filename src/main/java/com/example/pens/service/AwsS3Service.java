package com.example.pens.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.pens.domain.CommonResponse;
import com.example.pens.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public ResponseEntity uploadFile(Integer groupId, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity(new CommonResponse(false, "File Empty"), HttpStatus.FORBIDDEN);
        }

        String fileName = FileUtil.buildFileName(groupId, multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch (IOException e) {
            return new ResponseEntity(new CommonResponse(false, "File Upload Failed"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(new CommonResponse(true, amazonS3Client.getUrl(bucketName, fileName).toString()), HttpStatus.OK);
    }


}
