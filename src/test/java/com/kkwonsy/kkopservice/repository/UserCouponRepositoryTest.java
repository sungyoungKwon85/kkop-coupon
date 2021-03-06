package com.kkwonsy.kkopservice.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
import com.kkwonsy.kkopservice.model.response.v1.UserCouponExpiry;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserCouponRepositoryTest {

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @Test
    public void findAllWithUserId() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // when
        List<Coupon> coupons = userCouponRepository.findAllWithUserId(user.getId());

        // then
        assertThat(coupons.size()).isEqualTo(2);
        assertThat(coupons.get(0)).isNotNull();
        assertThat(coupons.get(0).getCreatedAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

    @Test
    public void getUserCouponExpiryWithin3Days() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon2));
        coupon1.setExpiryDate(LocalDate.now().plusDays(1));
        coupon2.setExpiryDate(LocalDate.now().plusDays(1));

        // when
        List<UserCouponExpiry> expiries = userCouponRepository.getUserCouponExpiryWithin3Days();
        // then
        assertThat(expiries).isNotNull();
        assertThat(expiries.size()).isEqualTo(2);
        assertThat(expiries.get(0).getUserName()).isEqualTo("kkwonsytest");
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