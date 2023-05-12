package com.example.pens.domain;


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

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private int groupId;

    @Column
    private String fileName;


}
