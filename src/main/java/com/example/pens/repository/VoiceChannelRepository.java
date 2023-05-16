package com.example.pens.repository;

import com.example.pens.domain.websocket.VoiceChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.Channel;
import java.util.List;

@Repository
public interface VoiceChannelRepository extends JpaRepository<VoiceChannel, Integer> {
    public List<VoiceChannel> findVoiceChannelsByGroup_GroupId(Integer groupId);
}
