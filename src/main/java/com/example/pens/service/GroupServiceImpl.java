package com.example.pens.service;

import com.example.pens.domain.AddUserToGroupRequest;
import com.example.pens.domain.Group;
import com.example.pens.domain.GroupRequest;
import com.example.pens.domain.User;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Optional;

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
            return new ResponseEntity("Success", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Exception : " + e.toString());
            throw new KeyAlreadyExistsException(); // Exception 변경해야 함
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
            return new ResponseEntity("Success", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new KeyAlreadyExistsException();
        }
    }
}
