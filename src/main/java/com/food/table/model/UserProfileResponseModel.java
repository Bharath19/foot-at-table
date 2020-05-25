package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseModel {

    private String name;

    private String email;

    private long phoneNo;

    private String imageUrl;
}
