package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseModel {

    private String name;

    private String email;
    
    private String userRole;
    
    private Integer userId;

    private long phoneNo;

    private String imageUrl;
}
