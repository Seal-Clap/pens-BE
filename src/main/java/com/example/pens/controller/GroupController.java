package com.example.pens.controller;

import com.example.pens.domain.request.groupUserRelationDTO;
import com.example.pens.domain.request.GroupDTO;
import com.example.pens.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity createGroup(@RequestBody GroupDTO request) {
        return groupService.createGroup(request);
    }

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody groupUserRelationDTO request) {
        return groupService.addUserToGroup(request);
    }

    @PostMapping("/delete-user")
    public ResponseEntity deleteUser(@RequestBody groupUserRelationDTO request) {
        return groupService.deleteUser(request);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(@RequestBody GroupDTO request) {
        return groupService.getUsersInGroup(request);
    }
}
