package com.example.pens;

import com.example.pens.domain.Group;
import com.example.pens.domain.User;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    public void getUsersInGroup() {
        // Create test users
        User user1 = new User();
        user1.setUserEmail("test1@example.com");
        user1.setUserName("Test User 1");
        user1.setUserPassword("password");

        User user2 = new User();
        user2.setUserEmail("test2@example.com");
        user2.setUserName("Test User 2");
        user2.setUserPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        // Create a test group and add users
        Group group = new Group();
        group.setGroupName("Test Group");
        group.setGroupAdmin("testadmin@example.com");
        group.setUsers(Set.of(user1, user2));

        group = groupRepository.save(group);

        // Perform the test
        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/groups/" + group.getGroupId() + "/users", User[].class);

        // Verify the results
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<User> users = List.of(response.getBody());
        assertThat(users).hasSize(2);
        assertThat(users).extracting("userId").containsExactlyInAnyOrder(user1.getUserId(), user2.getUserId());
        assertThat(users).extracting("userName").containsExactlyInAnyOrder(user1.getUserName(), user2.getUserName());
    }
}
