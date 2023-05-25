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
    public ResponseEntity putChannelUsers(Integer userId, String channelId);
    public ResponseEntity deleteChannelUser(Integer userId, String channelId);
    public List<String> getChannelUsers(String channelId);
}
