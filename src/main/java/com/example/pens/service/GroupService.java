package com.example.pens.service;

import com.example.pens.domain.request.GroupUserRelationDTO;
import com.example.pens.domain.request.GroupDTO;
import org.springframework.http.ResponseEntity;

public interface GroupService {
    ResponseEntity createGroup(GroupDTO request);

    ResponseEntity deleteGroup(GroupUserRelationDTO request);

    ResponseEntity addUserToGroup(GroupUserRelationDTO request);

    ResponseEntity deleteUser(GroupUserRelationDTO request);

    ResponseEntity getUsersInGroup(GroupDTO request);

    ResponseEntity invite(String groupId, String userEmail);

    ResponseEntity acceptInvite(String acceptString);

    ResponseEntity checkIsAdmin(GroupUserRelationDTO request);
}
