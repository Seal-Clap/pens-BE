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
        String email = request.getUserEmail();
        try {
            if (userRepository.findByUserEmail(email) != null) {
                return "duplicated email";
            }
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

    @Override
    public String validationLogin(UserRequest userRequest) {
        try {
            String email = userRequest.getUserEmail();
            String password = userRequest.getUserPassword();
            User loginUser = userRepository.findByUserEmail(email);

            if (loginUser == null) {
                return "해당 이메일의 유저가 존재하지 않습니다.";
            }

            if (!passwordEncoder.matches(password, loginUser.getUserPassword())) {
                return "비밀번호가 일치하지 않습니다.";
            }

            return "success";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
