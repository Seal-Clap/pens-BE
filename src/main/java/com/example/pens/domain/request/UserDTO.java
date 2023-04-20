package com.example.pens.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Vo
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;
}
