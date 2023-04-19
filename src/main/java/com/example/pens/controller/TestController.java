package com.example.pens.controller;

import com.example.pens.domain.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity test() {
        return new ResponseEntity(new CommonResponse(true, "test success"), HttpStatus.OK);
    }
}