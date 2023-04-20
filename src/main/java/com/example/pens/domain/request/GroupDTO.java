package com.example.pens.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
<<<<<<< HEAD:src/main/java/com/example/pens/domain/request/GroupDTO.java
public class GroupDTO {
    private Long groupId;
=======
public class GroupRequest {
    private int groupId;
>>>>>>> UsersBranch:src/main/java/com/example/pens/domain/GroupRequest.java
    private String groupName;
    private String groupAdmin;
}