package com.example.pens.domain.websocket;

import com.example.pens.domain.Group;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voice_channel")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoiceChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer channelId;

    @JsonProperty("groupId")
    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;

    private String channelName;
}
