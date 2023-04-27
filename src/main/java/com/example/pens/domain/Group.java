package com.example.pens.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Column(nullable = false, length = 30)
    private String groupName;

    @ManyToOne(fetch=FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name="user_id")
    private User groupAdminUser;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonBackReference
    @JoinTable(
            name = "group_user_relation",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
