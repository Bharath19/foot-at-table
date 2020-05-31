package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private int restaurantId;

    public AuthResponse(String accessToken, int restaurantId) {
        this.accessToken = accessToken;
        this.restaurantId = restaurantId;
    }

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
