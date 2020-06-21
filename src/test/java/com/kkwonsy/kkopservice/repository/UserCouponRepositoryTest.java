package com.kkwonsy.kkopservice.repository;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.domain.UserCoupon;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserCouponRepositoryTest {

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void save() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));

        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));

        // when
        UserCoupon save1 = userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        UserCoupon save2 = userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // then
        assertThat(save1.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(save2.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(save1.getCoupon().getCode()).isEqualTo(code1);
        assertThat(save2.getCoupon().getCode()).isEqualTo(code2);
    }

    @Test
    public void findByCouponIdAndUserId() throws Exception {
        // given
        User user = saveAndGetUser();
        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));
        UserCoupon save1 = userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        UserCoupon save2 = userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // when
        UserCoupon found = userCouponRepository.findByCouponIdAndUserId(coupon1.getId(), user.getId());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getUser().getId()).isEqualTo(user.getId());
        assertThat(found.getCoupon().getId()).isEqualTo(coupon1.getId());
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