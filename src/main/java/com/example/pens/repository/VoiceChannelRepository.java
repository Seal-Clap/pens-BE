package com.example.pens.repository;

import com.example.pens.domain.websocket.VoiceChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceChannelRepository extends JpaRepository<VoiceChannel, Integer> {
}
