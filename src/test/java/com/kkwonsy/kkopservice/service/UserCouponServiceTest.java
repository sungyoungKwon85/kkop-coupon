package com.kkwonsy.kkopservice.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kkwonsy.kkopservice.advice.exception.AlreadyUsedCouponException;
import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.domain.UserCoupon;
import com.kkwonsy.kkopservice.model.response.v1.IssuedCoupon;
import com.kkwonsy.kkopservice.repository.CouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserCouponRepository;
import com.kkwonsy.kkopservice.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserCouponServiceTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    public void createCoupon_then_check_they_are_made() throws Exception {
        // given
        int numberOfCoupon = 10;
        // when
        userCouponService.createCoupons(numberOfCoupon);
        // then
        List<Coupon> all = couponJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(numberOfCoupon);
    }

    @Test
    public void issueCoupon() throws Exception {
        // given
        User user = saveAndGetUser();
        String code = LocalDateTime.now().toString();
        couponJpaRepository.save(Coupon.createCoupon(code));

        // when
        IssuedCoupon issuedCoupon = userCouponService.issueCoupon(user.getId());
        // then
        assertThat(issuedCoupon.getCouponCode()).isEqualTo(code);
        assertThat(issuedCoupon.getExpiryDate()).isEqualTo(LocalDate.now().plusMonths(1));
    }

    @Test
    public void getCouponsBy() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = Coupon.createCoupon(code1);
        couponJpaRepository.save(coupon1);
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = Coupon.createCoupon(code2);
        couponJpaRepository.save(coupon2);
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // when
        List<IssuedCoupon> couponsBy = userCouponService.getCouponsBy(user.getId());
        // then
        assertThat(couponsBy.size()).isEqualTo(2);
        assertThat(couponsBy.get(0).getCreatedDate().getDayOfMonth()).isEqualTo(LocalDate.now().getDayOfMonth());
    }

    @Test
    public void useCoupon() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = Coupon.createCoupon(code1);
        couponJpaRepository.save(coupon1);
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = Coupon.createCoupon(code2);
        couponJpaRepository.save(coupon2);
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        // when
        userCouponService.useCoupon(user.getId(), coupon1.getId());
        UserCoupon usedCoupon = userCouponRepository.findByCouponIdAndUserId(coupon1.getId(), user.getId());
        // then
        assertThat(usedCoupon).isNotNull();
        assertThat(usedCoupon.isUsedYn()).isTrue();
    }

    @Test(expected = AlreadyUsedCouponException.class)
    public void useCoupon_fail() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = Coupon.createCoupon(code1);
        couponJpaRepository.save(coupon1);
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = Coupon.createCoupon(code2);
        couponJpaRepository.save(coupon2);
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        userCouponService.useCoupon(user.getId(), coupon1.getId());

        // when
        userCouponService.useCoupon(user.getId(), coupon1.getId());
    }

    @Test
    public void cancelUsingCoupon() throws Exception {
        // given
        User user = saveAndGetUser();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = Coupon.createCoupon(code1);
        couponJpaRepository.save(coupon1);
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = Coupon.createCoupon(code2);
        couponJpaRepository.save(coupon2);
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));

        userCouponService.useCoupon(user.getId(), coupon1.getId());

        // when
        userCouponService.cancelUsingCoupon(user.getId(), coupon1.getId());
        UserCoupon canceledCoupon = userCouponRepository.findByCouponIdAndUserId(coupon1.getId(), user.getId());
        // then
        assertThat(canceledCoupon).isNotNull();
        assertThat(canceledCoupon.isUsedYn()).isFalse();
    }

    // todo
//    @Test(expected = TimeOutToCancelCouponException.class)
//    public void cancelUsingCoupon_fail() throws Exception {
//        // given
//        User user = saveAndGetUser();
//
//        String code1 = LocalDateTime.now().toString();
//        Coupon coupon1 = Coupon.createCoupon(code1);
//        couponJpaRepository.save(coupon1);
//        String code2 = LocalDateTime.now().plusDays(10).toString();
//        Coupon coupon2 = Coupon.createCoupon(code2);
//        couponJpaRepository.save(coupon2);
//        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon1));
//        userCouponRepository.save(UserCoupon.createUserCoupon(user, coupon2));
//
//        userCouponService.useCoupon(user.getId(), coupon1.getId());
//        UserCoupon usedCoupon = userCouponRepository.findByCouponIdAndUserId(coupon1.getId(), user.getId());
////        usedCoupon.se
//
//        // when
//        userCouponService.cancelUsingCoupon(user.getId(), coupon1.getId());
//    }

    private User saveAndGetUser() {
        return userRepository.save(
            User.builder()
                .email("kkwonsytest@naver.com")
                .name("kkwonsytest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }

    @Test
    public void givenFixedClock_whenNow_thenGetFixedInstant() {
        Clock clock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC"));
        String dateTimeExpected = "2014-12-22T10:15:30";

        LocalDateTime dateTime = LocalDateTime.now(clock);

        assertThat(dateTime).isEqualTo(dateTimeExpected);
    }
}