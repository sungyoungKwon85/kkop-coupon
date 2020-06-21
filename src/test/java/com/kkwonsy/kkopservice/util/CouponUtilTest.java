package com.kkwonsy.kkopservice.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CouponUtilTest {

    @Test
    public void print_result_of_generateCouponCode() {
        Set<String> set = new HashSet<>();
        int loopCount = 100;
        for (int i = 0; i < loopCount; i++) {
            set.add(CouponUtil.generateCouponCode());
        }

//        Assertions.assertThat(set.size()).isEqualTo(loopCount);
        System.out.println("Code Set Size: " + set.size());
    }

    @Test
    public void asdf() throws Exception {
        // given
        LocalDateTime hourBefore = LocalDateTime.now().minusHours(1);
        LocalDateTime tenMinBefore = LocalDateTime.now().minusMinutes(10);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(hourBefore.isBefore(tenMinBefore));
        // when
        // then
    }
}