package com.example.pens.service;

import com.example.pens.domain.AddUserToGroupRequest;
import com.example.pens.domain.GroupRequest;
import org.springframework.http.ResponseEntity;

public interface GroupService {
    ResponseEntity createGroup(GroupRequest request);

    ResponseEntity addUserToGroup(AddUserToGroupRequest request);

}
