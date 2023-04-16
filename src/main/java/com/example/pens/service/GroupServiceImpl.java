package com.example.pens.service;

import com.example.pens.domain.*;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity createGroup(GroupRequest request) {
        try {
            groupRepository.save(
                    Group.builder()
                            .groupName(request.getGroupName())
                            .groupAdmin(request.getGroupAdmin())
                            .build()
            );
            return new ResponseEntity(new CommonResponse(true, "group create success"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity addUserToGroup(AddUserToGroupRequest request) {
        System.out.println("debug -> groupid:"+request.getGroupId()+ " and userid:" +request.getUserId());
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        try {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().add(user);
            user.getGroups().add(group);

            groupRepository.save(group);
            userRepository.save(user);
            return new ResponseEntity(new CommonResponse(true, "group add user success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity getUsersInGroup(int groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            Set<User> users = group.getUsers();
            return new ResponseEntity(new UsersResponse(List.copyOf(users)), HttpStatus.OK);
        } else {
            throw new RuntimeException("Group not found with ID: " + groupId);
        }
    }
}
