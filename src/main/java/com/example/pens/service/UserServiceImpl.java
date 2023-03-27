package com.example.pens.service;

import com.example.pens.domain.User;
import com.example.pens.domain.UserRequest;
import com.example.pens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(UserRequest request) {
        try {
            userRepository.save(
                    User.builder()
                            .userName(request.getUserName())
                            .userEmail(request.getUserEmail())
                            .userPassword(passwordEncoder.encode(request.getUserPassword()))
                            .build()
            );
            return "Success";
        } catch (Exception e) {
            throw new KeyAlreadyExistsException(); // Exception 변경해야 함
        }
    }
}
