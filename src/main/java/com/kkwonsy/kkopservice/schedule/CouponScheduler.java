package com.kkwonsy.kkopservice.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kkwonsy.kkopservice.model.response.v1.UserCouponExpiry;
import com.kkwonsy.kkopservice.service.UserCouponService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CouponScheduler {

    private final UserCouponService userCouponService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkAndPushExpiringCoupon() {
        List<UserCouponExpiry> userCouponExpiries = userCouponService.getUserCouponExpiryWithin3Days();
        for (UserCouponExpiry uce : userCouponExpiries) {
            StringBuilder sb = new StringBuilder();
            sb.append(uce.getUserName())
                .append("님, 쿠폰 ")
                .append(uce.getCouponCode())
                .append("의 사용 기간이 3일 후 만료 됩니다. ");
            System.out.println(sb.toString());
        }
    }

}
