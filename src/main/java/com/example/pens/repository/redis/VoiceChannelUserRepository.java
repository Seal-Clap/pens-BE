package com.example.pens.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceChannelUserRepository extends CrudRepository<String, String> {
}
