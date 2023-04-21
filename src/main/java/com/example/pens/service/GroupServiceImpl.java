package com.example.pens.service;

import com.example.pens.domain.*;
import com.example.pens.domain.request.GroupDTO;
import com.example.pens.domain.request.groupUserRelationDTO;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import com.example.pens.util.SecurityUtil;
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
    public ResponseEntity createGroup(GroupDTO request) {
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
    public ResponseEntity addUserToGroup(groupUserRelationDTO request) {
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
    public ResponseEntity deleteUser(groupUserRelationDTO request) {
        System.out.println("debug -> groupid:"+request.getGroupId()+ " and userid:" +request.getUserId());
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        try {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().remove(user);
            user.getGroups().remove(group);

            groupRepository.save(group);
            userRepository.save(user);
            return new ResponseEntity(new CommonResponse(true, "group delete user success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity getUsersInGroup(GroupDTO request) {
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            Set<User> users = group.getUsers();
            return new ResponseEntity(new UsersResponse(List.copyOf(users)), HttpStatus.OK);
        } else {
            return new ResponseEntity(new CommonResponse(false, "no group"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity invite(groupUserRelationDTO request) {
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        Integer requestUserId = userRepository.findByUserEmail(SecurityUtil.getCurrentUserEmail()).getUserId();

        if (!requestUserId.equals(groupOptional.get().getGroupAdmin())) {
            return new ResponseEntity<CommonResponse>(new CommonResponse(false, "only group admin can invite user"), HttpStatus.UNAUTHORIZED);
        }
        //admin 확인 완료
        // TODO: 초대 진행 redis
        return new ResponseEntity(new CommonResponse(true, "invite success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity acceptInvite(String acceptString) {
        return null;
    }

}
