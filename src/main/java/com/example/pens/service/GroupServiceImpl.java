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
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final InviteRedisRepository inviteRedisRepository;
    private final JavaMailSender javaMailSender;
    @Override
    public ResponseEntity createGroup(GroupDTO request) {
        Optional<User> userOptional = userRepository.findById(request.getGroupAdminUserId());
        Set<User> users = new HashSet<>();

        try {
            User user;
            if (userOptional.isPresent())
                user = userOptional.get();
            else
                return new ResponseEntity(new CommonResponse(false, "User not found"), HttpStatus.NOT_FOUND);
            users.add(user);
            groupRepository.save(
                    Group.builder()
                            .groupName(request.getGroupName())
                            .groupAdminUser(user)
                            .users(users)
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
    public ResponseEntity getUsersInGroup(Integer groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            Set<User> users = group.getUsers();
            return new ResponseEntity(List.copyOf(users), HttpStatus.OK);
        } else {
            return new ResponseEntity(new CommonResponse(false, "no group"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity invite(String groupId, String userEmail) {
        Integer groupIdRequest = Integer.parseInt(groupId);
        Optional<Group> groupOptional = groupRepository.findById(groupIdRequest);
//        Integer requestUserId = userRepository.findByUserEmail(SecurityUtil.getCurrentUserEmail()).getUserId();
//        if (!requestUserId.equals(groupOptional.get().getGroupAdminUser().getUserId())) {
//            return new ResponseEntity<CommonResponse>(new CommonResponse(false, "only group admin can invite user"), HttpStatus.UNAUTHORIZED);
//        }
        GroupInvite groupInvite = new GroupInvite(groupIdRequest, userEmail);
        SimpleMailMessage inviteMail = new SimpleMailMessage();
        inviteMail.setSubject("[pens'] "+ groupOptional.get().getGroupName() + " group invite Request");
        // TODO mail text remote server 로 변경
        inviteMail.setTo(userEmail);
        inviteMail.setText("http://localhost:8080/group/accept-invite/" + groupInvite.getId());
        javaMailSender.send(inviteMail);
        inviteRedisRepository.save(groupInvite);
        return new ResponseEntity<CommonResponse>(new CommonResponse(true, "invite success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity acceptInvite(String acceptString) {
        try {
            GroupInvite groupInvite = inviteRedisRepository.findById(acceptString).get();
            addUserToGroup(new GroupUserRelationDTO(userRepository.findByUserEmail(groupInvite.getUser_email()).getUserId(), groupInvite.getGroup_id()));
            inviteRedisRepository.delete(groupInvite);
            return new ResponseEntity<CommonResponse>(new CommonResponse(true, "invite success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResponse>(new CommonResponse(false, "invite failed"), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity checkIsAdmin(Integer groupId, Integer userId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        try {
            Group group = groupOptional.get();
            if(group.getGroupAdminUser().getUserId() == userId)
                return new ResponseEntity<CommonResponse>(new CommonResponse(true, "admin true"), HttpStatus.OK);
            else
                return new ResponseEntity<CommonResponse>(new CommonResponse(false, "admin false"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

}
