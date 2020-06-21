package com.kkwonsy.kkopservice.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponUtil {

    public static String generateCouponCode() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
        long max = 10000000000l;

        long random = (long) (Math.random() * max);
        StringBuilder sb = new StringBuilder();

        while (random > 0) {
            sb.append(chars[(int) (random % chars.length)]);
            random /= chars.length;
        }

        String couponCode = sb.toString();
        log.debug("Coupon Code: " + couponCode);
        return couponCode;
    }

}
