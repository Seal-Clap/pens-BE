package com.example.pens.service;

import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.Group;
import com.example.pens.domain.websocket.VoiceChannel;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.VoiceChannelRepository;
import com.example.pens.repository.redis.VoiceChannelUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class VoiceChannelServiceImpl implements VoiceChannelService {
    private final VoiceChannelRepository voiceChannelRepository;
    private final GroupRepository groupRepository;
    private final VoiceChannelUserRepository voiceChannelUserRepository;
    private StringRedisTemplate redisTemplate;
    @Override
    public ResponseEntity create(Integer groupId, String channelName) {
        try {
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException());
            VoiceChannel voiceChannel = VoiceChannel.builder()
                    .group(group)
                    .channelName(channelName)
                    .build();
            voiceChannelRepository.save(voiceChannel);
            return new ResponseEntity(new CommonResponse(true, "voice channel create success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "voice channel create fail"), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity getChannels(Integer groupId) {
        List<VoiceChannel> voiceChannels = voiceChannelRepository.findVoiceChannelsByGroup_GroupId(groupId);
        List<Map<String, Object>> responseChannels = new ArrayList<>();
        for (VoiceChannel voiceChannel : voiceChannels) {
            Map<String, Object> channelMap = new HashMap<>();
            channelMap.put("channelId", voiceChannel.getChannelId());
            channelMap.put("channelName", voiceChannel.getChannelName());
            channelMap.put("groupId", voiceChannel.getGroup().getGroupId());
            responseChannels.add(channelMap);
        }
        return new ResponseEntity(responseChannels, HttpStatus.OK);
    }

    @Override
    public void putChannelUsers(Integer userId, String roomId) {
        // TODO: redis set (roomId, userName) 삽입
    }

    @Override
    public void deleteChannelUser(Integer userId, String roomId) {
        // TODO: redis (roomId, userName) 삭제
    }

    @Override
    public List<String> getChannelUsers(Integer roomId) {
        // TODO: redis set roomId userName 목록 반환
        return null;
    }
}
