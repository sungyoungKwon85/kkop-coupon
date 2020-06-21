package com.kkwonsy.kkopservice.model.response.v1;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCouponExpiry {

    private String userName;

    private String userEmail;

    private String couponCode;

    private LocalDate couponExpiryDate;

    @Builder
    public UserCouponExpiry(String userName, String userEmail, String couponCode, LocalDate couponExpiryDate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.couponCode = couponCode;
        this.couponExpiryDate = couponExpiryDate;
    }
}
