package com.example.pens.controller;


import com.example.pens.domain.websocket.VoiceChannel;
import com.example.pens.service.VoiceChannelService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Any;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class VoiceChannelController {
    private final VoiceChannelService voiceChannelService;

    @PostMapping
    public ResponseEntity createChannel(@RequestBody Map<String, String> map) {
        return voiceChannelService.create(Integer.parseInt(map.get("groupId")), map.get("channelName"));
    }

    @GetMapping
    public ResponseEntity getChannels(@RequestParam Integer groupId) {
        return voiceChannelService.getChannels(groupId);
    }
}
