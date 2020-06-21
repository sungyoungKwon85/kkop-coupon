package com.kkwonsy.kkopservice.model.response.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kkwonsy.kkopservice.domain.Coupon;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IssuedCoupon {

    private Long couponId;

    private String couponCode;

    private LocalDate expiryDate;

    private LocalDateTime createdDate;

    @Builder
    public IssuedCoupon(Long couponId, String couponCode, LocalDate expiryDate, LocalDateTime createdDate) {
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.expiryDate = expiryDate;
        this.createdDate = createdDate;
    }

    private IssuedCoupon() {
    }

    public static IssuedCoupon from(Coupon coupon) {
        IssuedCoupon ic = new IssuedCoupon();
        ic.couponId = coupon.getId();
        ic.couponCode = coupon.getCode();
        ic.expiryDate = coupon.getExpiryDate();
        ic.createdDate = coupon.getCreatedAt();
        return ic;
    }
}
