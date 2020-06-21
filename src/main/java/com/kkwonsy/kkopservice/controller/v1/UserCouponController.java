package com.kkwonsy.kkopservice.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkwonsy.kkopservice.model.response.CommonResult;
import com.kkwonsy.kkopservice.service.ResponseService;
import com.kkwonsy.kkopservice.service.UserCouponService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"3. USER/COUPON"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class UserCouponController {

    private final ResponseService responseService;
    private final UserCouponService userCouponService;

    @ApiOperation(value = "쿠폰 생성", notes = "쿠폰을 N개 생성")
    @PostMapping(value = "/coupons")
    public CommonResult createCoupons(@ApiParam(value = "생성개수", required = true) @RequestParam Integer n) {
        userCouponService.createCoupons(n);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "쿠폰 발급", notes = "유저에게 쿠폰을 발급")
    @PostMapping(value = "/users/{userId}/coupons/issues")
    public CommonResult issueCoupon(@ApiParam(value = "유저 id", required = true) @PathVariable Long userId) {
        return responseService.getSingleResult(userCouponService.issueCoupon(userId));
    }

    @ApiOperation(value = "쿠폰 조회", notes = "유저에게 발급된 쿠폰 조회")
    @GetMapping(value = "/users/{userId}/coupons")
    public CommonResult getCoupons(@ApiParam(value = "유저 id", required = true) @PathVariable Long userId) {
        return responseService.getListResult(userCouponService.getCouponsBy(userId));
    }

    @ApiOperation(value = "쿠폰 사용/사용 취소", notes = "유저에게 발급된 쿠폰 사용/사용 취소")
    @PutMapping(value = "/users/{userId}/coupons/{couponId}")
    public CommonResult useCoupon(
        @ApiParam(value = "유저 id", required = true) @PathVariable Long userId,
        @ApiParam(value = "쿠폰 id", required = true) @PathVariable Long couponId,
        @ApiParam(value = "사용 여부", defaultValue = "true") @RequestParam(defaultValue = "true") boolean useYn) {
        if (useYn) {
            userCouponService.useCoupon(userId, couponId);
        } else {
            userCouponService.cancelUsingCoupon(userId, couponId);
        }
        return responseService.getSuccessResult();
    }
}
