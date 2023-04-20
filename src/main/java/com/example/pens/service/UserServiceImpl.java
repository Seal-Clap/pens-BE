package com.example.pens.service;

import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.User;
import com.example.pens.domain.auth.Authority;
import com.example.pens.domain.request.UserDTO;
import com.example.pens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity register(UserDTO request) {
        String email = request.getUserEmail();
        try {
            if (userRepository.findByUserEmail(email) != null) {
                return new ResponseEntity(new CommonResponse(false, "duplicated email"), HttpStatus.CONFLICT);
            }

            Authority authority = Authority.builder()
                    .authorityName("USER")
                    .build();

            userRepository.save(
                    User.builder()
                            .userName(request.getUserName())
                            .userEmail(request.getUserEmail())
                            .userPassword(passwordEncoder.encode(request.getUserPassword()))
                            .authorities(Collections.singleton(authority))
                            .build()
            );

            return new ResponseEntity(new CommonResponse(true, "register success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity validationLogin(UserDTO userRequest) {
        try {
            String email = userRequest.getUserEmail();
            String password = userRequest.getUserPassword();
            User loginUser = userRepository.findByUserEmail(email);
            if (loginUser == null) {
                String msg = "email not found";
                return new ResponseEntity("email not found", HttpStatus.FORBIDDEN);
            }

            if (!passwordEncoder.matches(password, loginUser.getUserPassword())) {
                return new ResponseEntity(new CommonResponse(false, "email not exist"), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity(new CommonResponse(true, "login success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occur"), HttpStatus.BAD_REQUEST);
        }
    }
}
