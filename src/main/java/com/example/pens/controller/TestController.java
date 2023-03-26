package com.example.pens.controller;

import com.example.pens.domain.CommonEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity test() {
        CommonEntity.CommonEntityBuilder<Object> builder = CommonEntity.builder();
        builder.result("Success");
        builder.code("200");
        builder.data(null);
        builder.msg("Test Response");

        CommonEntity response = builder.build();

        return new ResponseEntity(response, HttpStatus.OK);
    }
}