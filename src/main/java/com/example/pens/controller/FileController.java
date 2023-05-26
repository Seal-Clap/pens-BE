package com.example.pens.controller;

import com.example.pens.service.AwsS3Service;
import com.example.pens.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final AwsS3Service awsS3Service;
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("groupId") Integer groupId, @RequestPart(value = "file") MultipartFile multipartFile) {
        return awsS3Service.uploadFile(groupId, multipartFile);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("groupId") Integer groupId, @RequestParam("fileName") String fileName) throws IOException {
        String originalFilePath = FileUtil.buildFileName(groupId, fileName);
        return awsS3Service.downloadFile(originalFilePath);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") Integer fileId) throws IOException {
        return awsS3Service.downloadFile(fileId);
    }

    @GetMapping
    public ResponseEntity getFileList(@RequestParam(value = "groupId") Integer groupId) {
        return awsS3Service.getFileList(groupId);
    }

    @GetMapping("/info")
    public ResponseEntity getFileInfo(@RequestParam("fileId") Integer fileId) {
        return null;
    }

    private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(FileUtil.createContentDisposition(resourcePath));
        return headers;
    }

    @PostMapping("/delete")
    public ResponseEntity deleteFile(@RequestParam("groupId") Integer groupId, @RequestParam("fileName") String fileName) {
        return awsS3Service.deleteFile(groupId, fileName);
    }

}
