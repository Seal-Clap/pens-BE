package com.example.pens.service;

import com.example.pens.domain.AddUserToGroupRequest;
import com.example.pens.domain.GroupRequest;
import com.example.pens.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    ResponseEntity createGroup(GroupRequest request);

    ResponseEntity addUserToGroup(AddUserToGroupRequest request);

    ResponseEntity getUsersInGroup(int groupId);
}
