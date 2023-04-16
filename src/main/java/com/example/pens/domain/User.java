package com.example.pens.domain;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "user")
@Builder
@Getter
@Setter
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

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Group> groups;
}
