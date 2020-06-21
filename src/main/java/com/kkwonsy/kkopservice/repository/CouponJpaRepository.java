package com.kkwonsy.kkopservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkwonsy.kkopservice.domain.Coupon;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    Coupon findFirstByIssuedYnIsFalse();
}
