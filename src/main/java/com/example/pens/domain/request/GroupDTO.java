package com.example.pens.domain.request;

import com.example.pens.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GroupDTO {
    private int groupId;
    private String groupName;
    private int groupAdminUserId;
}