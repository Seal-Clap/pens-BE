package com.example.pens.controller;

import com.example.pens.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final AwsS3Service awsS3Service;
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("groupId") Integer groupId, @RequestPart(value = "file") MultipartFile multipartFile) {
        return awsS3Service.uploadFile(groupId, multipartFile);
    }
}
