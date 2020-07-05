package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private int restaurantId;
    private String userRole;
    private UserProfileResponseModel user;

    public AuthResponse(String accessToken, int restaurantId) {
        this.accessToken = accessToken;
        this.restaurantId = restaurantId;
    }

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
