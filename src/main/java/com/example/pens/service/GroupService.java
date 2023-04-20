package com.example.pens.service;

import com.example.pens.domain.request.groupUserRelationDTO;
import com.example.pens.domain.request.GroupDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    ResponseEntity createGroup(GroupDTO request);

    ResponseEntity addUserToGroup(groupUserRelationDTO request);

    ResponseEntity deleteUser(groupUserRelationDTO request);

}
