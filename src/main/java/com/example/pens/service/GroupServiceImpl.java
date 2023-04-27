package com.example.pens.service;

import com.example.pens.domain.*;
import com.example.pens.domain.redis.GroupInvite;
import com.example.pens.domain.request.GroupDTO;
import com.example.pens.domain.request.groupUserRelationDTO;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import com.example.pens.repository.redis.InviteRedisRepository;
import com.example.pens.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        try {
            User user = userOptional.get();
            groupRepository.save(
                    Group.builder()
                            .groupName(request.getGroupName())
                            .groupAdminUser(user)
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
        if (!requestUserId.equals(groupOptional.get().getGroupAdminUser().getUserId())) {
            return new ResponseEntity<CommonResponse>(new CommonResponse(false, "only group admin can invite user"), HttpStatus.UNAUTHORIZED);
        }
        GroupInvite groupInvite = new GroupInvite(request.getGroupId(), request.getUserId());
        SimpleMailMessage inviteMail = new SimpleMailMessage();
        inviteMail.setSubject("[pens'] "+ groupOptional.get().getGroupName() + " group invite Request");
//        inviteMail.setTo(userRepository.findById(request.getUserId()).toString());
        // TODO mail text remote server 로 변경
        inviteMail.setText("http://localhost:8080/group/accept-invite/" + groupInvite.getAcceptString());
        javaMailSender.send(inviteMail);
        inviteRedisRepository.save(groupInvite);
        return new ResponseEntity<CommonResponse>(new CommonResponse(true, "invite success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity acceptInvite(String acceptString) {
        return null;
    }

}
