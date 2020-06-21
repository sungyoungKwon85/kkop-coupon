package com.kkwonsy.kkopservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkwonsy.kkopservice.domain.UserCoupon;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    UserCoupon findByCouponIdAndUserId(Long couponId, Long userId);
}
