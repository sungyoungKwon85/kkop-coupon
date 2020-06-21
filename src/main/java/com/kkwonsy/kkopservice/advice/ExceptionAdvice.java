package com.kkwonsy.kkopservice.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kkwonsy.kkopservice.advice.exception.AlreadyUsedCouponException;
import com.kkwonsy.kkopservice.advice.exception.CAuthenticationEntryPointException;
import com.kkwonsy.kkopservice.advice.exception.CEmailSigninFailedException;
import com.kkwonsy.kkopservice.advice.exception.CUserNotFoundException;
import com.kkwonsy.kkopservice.advice.exception.TimeOutToCancelCouponException;
import com.kkwonsy.kkopservice.model.response.CommonResult;
import com.kkwonsy.kkopservice.service.ResponseService;
import com.kkwonsy.kkopservice.service.ResponseService.CommonResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(CommonResponse.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(CommonResponse.USER_NOT_FOUND);
    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e) {
        return responseService.getFailResult(CommonResponse.SIGNIN_FAILED);
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest request,
        CAuthenticationEntryPointException e) {
        return responseService.getFailResult(CommonResponse.ENTRY_POINT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(CommonResponse.ACCESS_DENIED);
    }

    @ExceptionHandler(AlreadyUsedCouponException.class)
    public CommonResult AlreadyUsedCouponException(HttpServletRequest request, AlreadyUsedCouponException e) {
        return responseService.getFailResult(CommonResponse.ALREADY_USED_COUPON);
    }

    @ExceptionHandler(TimeOutToCancelCouponException.class)
    public CommonResult TimeOutToCancelCouponException(HttpServletRequest request, TimeOutToCancelCouponException e) {
        return responseService.getFailResult(CommonResponse.CANT_CANCEL_COUPON);
    }
}