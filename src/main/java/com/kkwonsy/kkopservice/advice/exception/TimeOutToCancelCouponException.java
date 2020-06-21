package com.kkwonsy.kkopservice.advice.exception;

public class TimeOutToCancelCouponException extends RuntimeException {

    public TimeOutToCancelCouponException() {
        super();
    }

    public TimeOutToCancelCouponException(String message) {
        super(message);
    }

    public TimeOutToCancelCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOutToCancelCouponException(Throwable cause) {
        super(cause);
    }

    protected TimeOutToCancelCouponException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
