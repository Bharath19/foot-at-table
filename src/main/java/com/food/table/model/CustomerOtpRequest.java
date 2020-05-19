package com.food.table.model;

import lombok.Data;

@Data
public class CustomerOtpRequest {

    private Long phoneNo;

    private int otp;
}
