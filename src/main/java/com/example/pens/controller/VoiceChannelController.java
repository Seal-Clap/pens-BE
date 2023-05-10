package com.example.pens.controller;


import com.example.pens.service.GroupService;
import com.example.pens.service.VoiceChannelService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Any;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class VoiceChannelController {
    private VoiceChannelService voiceChannelService;
    @PostMapping
    public ResponseEntity createChannel(@RequestBody Map<String, Any> map) {
        return voiceChannelService.create(map);
    }
}
