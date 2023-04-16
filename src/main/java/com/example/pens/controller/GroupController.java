package com.example.pens.controller;

import com.example.pens.domain.AddUserToGroupRequest;
import com.example.pens.domain.GroupRequest;
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
    public ResponseEntity createGroup(@RequestBody GroupRequest request) {
        return groupService.createGroup(request);
    }

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody AddUserToGroupRequest request) {
        return groupService.addUserToGroup(request);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(@RequestBody GroupRequest request) {
        return groupService.getUsersInGroup(request.getGroupId());
    }
}
