package com.example.pens.repository;

import com.example.pens.domain.User;
import com.example.pens.domain.request.UserRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUserEmail(String userEmail);
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserEmail(String userEmail);
}
