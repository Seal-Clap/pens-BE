package com.example.pens.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonEntity<T> {
    private String result;
    private String code;
    private String msg;
    private T data;
}
