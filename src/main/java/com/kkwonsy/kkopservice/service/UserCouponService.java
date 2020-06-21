package com.kkwonsy.kkopservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkwonsy.kkopservice.advice.exception.AlreadyUsedCouponException;
import com.kkwonsy.kkopservice.advice.exception.CUserNotFoundException;
import com.kkwonsy.kkopservice.advice.exception.CouponNotExistException;
import com.kkwonsy.kkopservice.advice.exception.IssuableCouponNotExistException;
import com.kkwonsy.kkopservice.advice.exception.TimeOutToCancelCouponException;
import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.domain.UserCoupon;
import com.kkwonsy.kkopservice.model.response.v1.IssuedCoupon;
import com.kkwonsy.kkopservice.model.response.v1.UserCouponExpiry;
import com.kkwonsy.kkopservice.repository.CouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserCouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserCouponRepository;
import com.kkwonsy.kkopservice.repository.UserJpaRepository;
import com.kkwonsy.kkopservice.util.CouponUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponService {

    private final CouponJpaRepository couponJpaRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserCouponJpaRepository userCouponJpaRepository;


    @Transactional
    public void createCoupons(Integer n) {
        for (int i = 0; i < n; i++) {
            createCoupon();
        }
    }

    private void createCoupon() {
        Coupon coupon = Coupon.createCoupon(CouponUtil.generateCouponCode());
        try {
            couponJpaRepository.save(coupon);
        } catch (DataIntegrityViolationException e) {
            createCoupon();
        }
    }


    @Transactional
    public IssuedCoupon issueCoupon(Long userId) {
        Coupon coupon = couponJpaRepository.findFirstByIssuedYnIsFalse();
        if (coupon == null) {
            throw new IssuableCouponNotExistException();
        }
        coupon.issueCoupon();

        User user = userJpaRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);
        userCouponJpaRepository.save(userCoupon);

        return IssuedCoupon.builder()
            .couponCode(coupon.getCode())
            .couponId(coupon.getId())
            .expiryDate(coupon.getExpiryDate())
            .createdDate(coupon.getCreatedAt())
            .build();
    }

    public List<IssuedCoupon> getCouponsBy(Long userId) {
        List<Coupon> coupons = userCouponRepository.findAllWithUserId(userId);
        return coupons.stream()
            .map(c -> IssuedCoupon.from(c))
            .collect(Collectors.toList());
    }

    @Transactional
    public void useCoupon(Long userId, Long couponId) {
        UserCoupon found = userCouponJpaRepository.findByCouponIdAndUserId(couponId, userId);
        if (found == null) {
            throw new CouponNotExistException();
        }
        if (!found.isUsedYn()) {
            found.useCoupon();
            userCouponJpaRepository.save(found);
        } else {
            throw new AlreadyUsedCouponException();
        }
    }

    @Transactional
    public void cancelUsingCoupon(Long userId, Long couponId) {
        UserCoupon found = userCouponJpaRepository.findByCouponIdAndUserId(couponId, userId);
        if (found == null) {
            throw new CouponNotExistException();
        }
        LocalDateTime hourAgo = LocalDateTime.now().minusHours(1);
        if (found.isUsedYn() && found.getUsedDate().isAfter(hourAgo)) { // 사용후 1시간 지나면 취소할 수 없음
            found.cancelUsingCoupon();
            userCouponJpaRepository.save(found);
        } else {
            throw new TimeOutToCancelCouponException();
        }
    }

    public List<UserCouponExpiry> getUserCouponExpiryWithin3Days() {
        return userCouponRepository.getUserCouponExpiryWithin3Days();
    }
}
