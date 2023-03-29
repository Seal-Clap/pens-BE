package com.example.pens.domain;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false, length = 50)
    private String userEmail;

    @Column(nullable = false, length = 100)
    private String userPassword;

}
