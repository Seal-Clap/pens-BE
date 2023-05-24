package com.example.pens.service;

import com.example.pens.domain.websocket.VoiceChannel;
import org.hibernate.annotations.Any;
import org.springframework.http.ResponseEntity;

import java.nio.channels.Channel;
import java.util.List;
import java.util.Map;

public interface VoiceChannelService {
    public ResponseEntity create(Integer groupId, String channelName);
    public ResponseEntity getChannels(Integer groupId);
    public void putChannelUsers(Integer userId, String roomId);
    public void deleteChannelUser(Integer userId, String roomId);
    public List<String> getChannelUsers(Integer roomId);
}
