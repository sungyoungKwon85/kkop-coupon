package com.kkwonsy.kkopservice.domain;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private boolean usedYn;

    private LocalDateTime usedDate;

    public static UserCoupon createUserCoupon(User user, Coupon coupon) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.user = user;
        userCoupon.coupon = coupon;
        userCoupon.usedYn = false;
        return userCoupon;
    }

    public void useCoupon() {
        this.usedYn = true;
        this.usedDate = LocalDateTime.now();
    }

    public void cancelUsingCoupon() {
        this.usedYn = false;
        this.usedDate = null;
    }

}
