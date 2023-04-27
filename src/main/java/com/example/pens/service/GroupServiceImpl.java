package com.example.pens.service;

import com.example.pens.domain.*;
import com.example.pens.domain.redis.GroupInvite;
import com.example.pens.domain.request.GroupDTO;
import com.example.pens.domain.request.GroupUserRelationDTO;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import com.example.pens.repository.redis.InviteRedisRepository;
import com.example.pens.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final InviteRedisRepository inviteRedisRepository;
//    private final JavaMailSender emailSender;
    @Override
    public ResponseEntity createGroup(GroupDTO request) {
        Optional<User> userOptional = userRepository.findById(request.getGroupAdminUserId());
        try {
            User user;
            if (userOptional.isPresent())
                user = userOptional.get();
            else
                return new ResponseEntity(new CommonResponse(false, "User not found"), HttpStatus.NOT_FOUND);

            groupRepository.save(
                    Group.builder()
                            .groupName(request.getGroupName())
                            .groupAdminUser(user)
                            .build()
            );
            return new ResponseEntity(new CommonResponse(true, "group create success"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity deleteGroup(GroupUserRelationDTO request) {
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        try {
            Group group = groupOptional.get();
            if(group.getGroupAdminUser().getUserId() != request.getUserId())
                return new ResponseEntity<CommonResponse>(new CommonResponse(false, "only group admin can delete group"), HttpStatus.UNAUTHORIZED);
            groupRepository.delete(group);
            return new ResponseEntity(new CommonResponse(true, "group delete success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity addUserToGroup(GroupUserRelationDTO request) {
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
    public ResponseEntity deleteUser(GroupUserRelationDTO request) {
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
    public ResponseEntity invite(GroupUserRelationDTO request) {
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        Integer requestUserId = userRepository.findByUserEmail(SecurityUtil.getCurrentUserEmail()).getUserId();
        //if (!requestUserId.equals(groupOptional.get().getGroupAdmin())) {
        //    return new ResponseEntity<CommonResponse>(new CommonResponse(false, "only group admin can invite user"), HttpStatus.UNAUTHORIZED);
        //}
        GroupInvite groupInvite = new GroupInvite(request.getGroupId(), request.getUserId());
//        SimpleMailMessage inviteMail = new SimpleMailMessage();
//        inviteMail.setSubject("pens' invite Request");
//        inviteMail.setTo(userRepository.findById(request.getUserId()).toString());
//        inviteMail.setText(groupInvite.getAcceptString());
//        emailSender.send(inviteMail);
        inviteRedisRepository.save(groupInvite);
        return new ResponseEntity<CommonResponse>(new CommonResponse(true, "invite success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity acceptInvite(String acceptString) {
        return null;
    }

    @Override
    public ResponseEntity checkIsAdmin(GroupUserRelationDTO request) {
        Optional<Group> groupOptional = groupRepository.findById(request.getGroupId());
        try {
            Group group = groupOptional.get();
            if(group.getGroupAdminUser().getUserId() == request.getUserId())
                return new ResponseEntity<CommonResponse>(new CommonResponse(true, "admin true"), HttpStatus.OK);
            else
                return new ResponseEntity<CommonResponse>(new CommonResponse(false, "admin false"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

}
