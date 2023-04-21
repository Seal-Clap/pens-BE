package com.example.pens.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;


@Getter
@RedisHash(value = "invite", timeToLive = 86400)
@NoArgsConstructor
@AllArgsConstructor
public class GroupInvite {

    @Id
    private String id;
    private Integer group_id;
    private Integer user_id;
    @Indexed
    private String acceptString;
    private LocalDateTime createdAt;

    @Builder
    public GroupInvite(Integer group_id, Integer user_id) {
        this.acceptString = Base64.getEncoder().encodeToString((group_id + "stringification" + user_id).getBytes(StandardCharsets.UTF_8));
        this.group_id = group_id;
        this.user_id = user_id;
        this.createdAt = LocalDateTime.now();
    }
}
