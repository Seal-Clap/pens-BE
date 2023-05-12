package com.example.pens.service;

import org.hibernate.annotations.Any;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface VoiceChannelService {
    public ResponseEntity create(Map<String, Any> map);
}
