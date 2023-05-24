package com.example.pens.service;

import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.Group;
import com.example.pens.domain.websocket.VoiceChannel;
import com.example.pens.repository.GroupRepository;
import com.example.pens.repository.UserRepository;
import com.example.pens.repository.VoiceChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VoiceChannelServiceImpl implements VoiceChannelService {
    private final VoiceChannelRepository voiceChannelRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private StringRedisTemplate stringRedisTemplate;
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
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        String userName = userRepository.findById(userId).get().getUserName();
        ops.add(roomId, userName);
    }

    @Override
    public void deleteChannelUser(Integer userId, String roomId) {
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        String userName = userRepository.findById(userId).get().getUserName();
        ops.remove(roomId, userName);
    }

    @Override
    public List<String> getChannelUsers(String roomId) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        return new ArrayList<>(setOps.members(roomId));
    }
}
