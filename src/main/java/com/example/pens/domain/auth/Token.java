package com.example.pens.domain.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}