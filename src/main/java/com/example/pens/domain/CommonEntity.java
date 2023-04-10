package com.example.pens.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class CommonEntity<T> {
    private String result;
    private HttpStatus code;
    private String msg;
    private T data;
}
