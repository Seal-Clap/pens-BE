package com.example.pens.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_content")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class GroupFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;


    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    private String fileName;


}
