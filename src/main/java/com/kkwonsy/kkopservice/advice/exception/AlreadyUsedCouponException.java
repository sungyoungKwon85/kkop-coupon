package com.kkwonsy.kkopservice.advice.exception;

public class AlreadyUsedCouponException extends RuntimeException {

    public AlreadyUsedCouponException() {
        super();
    }

    public AlreadyUsedCouponException(String message) {
        super(message);
    }

    public AlreadyUsedCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyUsedCouponException(Throwable cause) {
        super(cause);
    }

    protected AlreadyUsedCouponException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
