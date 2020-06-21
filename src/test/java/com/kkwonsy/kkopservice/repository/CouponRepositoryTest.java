package com.kkwonsy.kkopservice.repository;

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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    public void findAllWithUserId() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // when
        List<Coupon> coupons = couponRepository.findAllWithUserId(user.getId());

        // then
        assertThat(coupons.size()).isEqualTo(2);
        assertThat(coupons.get(0)).isNotNull();
        assertThat(coupons.get(0).getCreatedAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

    private User saveAndGetUser() {
        return userRepository.save(
            User.builder()
                .email("kkwonsytest@naver.com")
                .name("kkwonsytest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }
}