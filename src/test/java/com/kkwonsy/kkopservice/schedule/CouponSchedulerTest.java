package com.kkwonsy.kkopservice.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.domain.UserCoupon;
import com.kkwonsy.kkopservice.repository.CouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserCouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CouponSchedulerTest {

    @Autowired
    private CouponScheduler couponScheduler;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @Test
    public void checkAndPushExpiringCoupon() throws Exception {
        // given
        User user = saveAndGetUser();

        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(LocalDateTime.now().toString()));
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(LocalDateTime.now().plusDays(10).toString()));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon2));
        coupon1.setExpiryDate(LocalDate.now().plusDays(1));
        coupon2.setExpiryDate(LocalDate.now().plusDays(2));
        // when
        couponScheduler.checkAndPushExpiringCoupon();
        // then
    }

    private User saveAndGetUser() {
        return userJpaRepository.save(
            User.builder()
                .email("kkwonsytest@naver.com")
                .name("kkwonsytest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }

}