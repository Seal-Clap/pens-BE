package com.example.pens.repository.redis;

import com.example.pens.domain.redis.GroupInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRedisRepository extends CrudRepository<GroupInvite, String> {
}
