package com.example.pens.controller;

import com.example.pens.domain.request.GroupUserRelationDTO;
import com.example.pens.domain.request.GroupDTO;
import com.example.pens.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
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

    @PostMapping("/delete")
    public ResponseEntity deleteGroup(@RequestBody GroupUserRelationDTO request) { return groupService.deleteGroup(request); }

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody GroupUserRelationDTO request) {
        return groupService.addUserToGroup(request);
    }

    @PostMapping("/delete-user")
    public ResponseEntity deleteUser(@RequestBody GroupUserRelationDTO request) {
        return groupService.deleteUser(request);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(@RequestBody GroupDTO request) {
        return groupService.getUsersInGroup(request);
    }

    @PostMapping("/invite")
    public ResponseEntity invite(@RequestBody Map<String, String> map) {
        return groupService.invite(map.get("groupId"), map.get("userEmail"));
    }

    @GetMapping("/accept-invite/{acceptString}")
    public ResponseEntity acceptInvite(@PathVariable("acceptString") String acceptString) {
        return groupService.acceptInvite(acceptString);
    }

    @GetMapping("/isAdmin")
    public ResponseEntity checkIsAdmin(@RequestBody GroupUserRelationDTO request) { return groupService.checkIsAdmin(request); }
}
