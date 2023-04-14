package com.example.pens.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AddUserToGroupRequest {
    private int userId;
    private int groupId;
}
