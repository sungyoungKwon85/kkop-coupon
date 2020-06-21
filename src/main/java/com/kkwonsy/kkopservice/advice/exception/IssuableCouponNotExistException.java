package com.kkwonsy.kkopservice.advice.exception;

public class IssuableCouponNotExistException extends RuntimeException {

    public IssuableCouponNotExistException() {
        super();
    }

    public IssuableCouponNotExistException(String message) {
        super(message);
    }

    public IssuableCouponNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssuableCouponNotExistException(Throwable cause) {
        super(cause);
    }

    protected IssuableCouponNotExistException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
