package com.example.pens.service;

import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.User;
import com.example.pens.domain.auth.Authority;
import com.example.pens.domain.request.UserDTO;
import com.example.pens.jwt.JwtFilter;
import com.example.pens.jwt.JwtTokenProvider;
import com.example.pens.repository.UserRepository;
import com.example.pens.util.SecurityUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    @Transactional
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
            return new ResponseEntity(new CommonResponse(false, "error occurs"), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity login(@Valid @RequestBody UserDTO userRequest) {
        try {
            String email = userRequest.getUserEmail();
            String password = userRequest.getUserPassword();
            User loginUser = userRepository.findByUserEmail(email);
            if (loginUser == null) {
                return new ResponseEntity(new CommonResponse(false, "email not exist"), HttpStatus.FORBIDDEN);
            }
            if (!passwordEncoder.matches(password, loginUser.getUserPassword())) {
                return new ResponseEntity(new CommonResponse(false, "wrong password"), HttpStatus.FORBIDDEN);
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);


            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            HashMap<String, Object> loginResponse = new HashMap<>();

            loginResponse.put("success", true);
            loginResponse.put("message", "login success");
            loginResponse.put("userId", loginUser.getUserId());
            loginResponse.put("token", jwt);

            return new ResponseEntity(loginResponse, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CommonResponse(false, "error occurs"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity identify() {
        return new ResponseEntity(userRepository.findByUserEmail(SecurityUtil.getCurrentUserEmail()).getUserId(), HttpStatus.OK);
    }
}
