package com.kkwonsy.kkopservice.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String code;

    private LocalDate expiryDate;

    private boolean issuedYn;

    public static Coupon createCoupon(String code) {
        Coupon coupon = new Coupon();
        coupon.code = code;
        coupon.issuedYn = false;
        return coupon;
    }

    public void issueCoupon() {
        this.issuedYn = true;
        this.expiryDate = LocalDate.now().plusMonths(1);
    }
}
