package com.kkwonsy.kkopservice.repository;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kkwonsy.kkopservice.domain.Coupon;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CouponJpaRepositoryTest {

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Test
    public void save_coupon() throws Exception {
        // given
        String code = LocalDateTime.now().toString();
        Coupon coupon = Coupon.createCoupon(code);
        // when
        Coupon save = couponJpaRepository.save(coupon);
        // then
        assertThat(save.getCode()).isEqualTo(code);
    }

    @Test
    public void findFirstByIssuedYnIsFalse() throws Exception {
        // given
        String code = LocalDateTime.now().toString();
        couponJpaRepository.save(Coupon.createCoupon(code));
        // when
        Coupon coupon = couponJpaRepository.findFirstByIssuedYnIsFalse();
        // then
        assertThat(coupon).isNotNull();
        assertThat(coupon.isIssuedYn()).isEqualTo(false);
    }
}