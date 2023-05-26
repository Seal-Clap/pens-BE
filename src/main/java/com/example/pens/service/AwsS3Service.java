package com.example.pens.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.Group;
import com.example.pens.domain.GroupFile;
import com.example.pens.repository.GroupFileRepository;
import com.example.pens.repository.GroupRepository;
import com.example.pens.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AmazonS3 amazonS3Client;
    private final GroupFileRepository groupFileRepository;
    private final GroupRepository groupRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public ResponseEntity uploadFile(Integer groupId, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity(new CommonResponse(false, "File Empty"), HttpStatus.FORBIDDEN);
        }

        String fileName = FileUtil.buildFileName(groupId, multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            Optional<Group> group = groupRepository.findById(groupId);
            Optional<GroupFile> existingFile = groupFileRepository.findByFileNameAndGroup(multipartFile.getOriginalFilename(), group.get());
            if (existingFile.isPresent()) {
                // If file with the same name exists, update it.
                GroupFile groupFile = existingFile.get();
                groupFile.setFileName(multipartFile.getOriginalFilename());
                groupFile.setGroup(groupRepository.getReferenceById(groupId));
                groupFileRepository.save(groupFile);
            } else {
                // If no file with the same name exists, save a new one.
                GroupFile groupFile = GroupFile.builder().fileName(multipartFile.getOriginalFilename()).group(groupRepository.getReferenceById(groupId)).build();
                groupFileRepository.save(groupFile);
            }
        } catch (IOException e) {
            return new ResponseEntity(new CommonResponse(false, "File Upload Failed"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(new CommonResponse(true, amazonS3Client.getUrl(bucketName, fileName).toString()), HttpStatus.OK);
    }

    public ResponseEntity<byte[]> downloadFile(String resourcePath) throws IOException {

        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, resourcePath));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String fileName = URLEncoder.encode(resourcePath, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);


    }

    public ResponseEntity<byte[]> downloadFile(Integer fileId) throws IOException {
        GroupFile groupFile = groupFileRepository.getReferenceById(fileId);
        String resourcePath = FileUtil.buildFileName(groupFile.getGroup().getGroupId(), groupFile.getFileName());
        return downloadFile(resourcePath);
    }


    public ResponseEntity getFileList(Integer groupId) {
        List<GroupFile> groupFiles = groupFileRepository.findGroupFileByGroup(groupRepository.getById(groupId));

        List<Map<String, Object>> groupFileMaps = groupFiles.stream()
                .map(groupFile -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileId", groupFile.getFileId());
                    map.put("fileName", groupFile.getFileName());
                    return map;
                })
                .collect(Collectors.toList());
        return new ResponseEntity(groupFileMaps, HttpStatus.OK);
    }

    public ResponseEntity deleteFile(Integer groupId, String fileName) {
        try {
            String fileKey = FileUtil.buildFileName(groupId, fileName);
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));

            // You might also want to delete the corresponding GroupFile from your database
            Optional<Group> group = groupRepository.findById(groupId);
            Optional<GroupFile> existingFile = groupFileRepository.findByFileNameAndGroup(fileName, group.get());
            if (existingFile.isPresent()) {
                groupFileRepository.delete(existingFile.get());
            }

            return new ResponseEntity(new CommonResponse(true, "File deleted successfully"), HttpStatus.OK);
        } catch (AmazonServiceException e) {
            return new ResponseEntity(new CommonResponse(false, "File deletion failed: " + e.getErrorMessage()), HttpStatus.FORBIDDEN);
        }
    }
}
