package com.example.pens.service;

import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.websocket.VoiceChannel;
import com.example.pens.repository.VoiceChannelRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Any;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoiceChannelServiceImpl implements VoiceChannelService {
    private final VoiceChannelRepository voiceChannelRepository;
    @Override
    public ResponseEntity create(Map<String, Any> map) {
        try {
            String channelName = map.get("channelName").toString();
            Integer groupId =  Integer.parseInt(map.get("groupId").toString());
            voiceChannelRepository.save(VoiceChannel.builder().channelName(channelName).groupId(groupId).build());
            return new ResponseEntity(new CommonResponse(true, "voice channel create success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "voice channel create fail"), HttpStatus.FORBIDDEN);
        }
    }
}
