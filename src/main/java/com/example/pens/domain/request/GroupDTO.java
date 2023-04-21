package com.example.pens.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GroupDTO {
    private int groupId;
    private String groupName;
    private int groupAdmin;
}