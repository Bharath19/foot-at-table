package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
