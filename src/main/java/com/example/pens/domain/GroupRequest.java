package com.example.pens.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GroupRequest {
    private String groupName;
    private String groupAdmin;
}