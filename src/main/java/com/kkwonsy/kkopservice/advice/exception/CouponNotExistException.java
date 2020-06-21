package com.kkwonsy.kkopservice.advice.exception;

public class CouponNotExistException extends RuntimeException {

    public CouponNotExistException() {
        super();
    }

    public CouponNotExistException(String message) {
        super(message);
    }

    public CouponNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponNotExistException(Throwable cause) {
        super(cause);
    }

    protected CouponNotExistException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
