package com.kkwonsy.kkopservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kkwonsy.kkopservice.model.response.CommonResult;
import com.kkwonsy.kkopservice.model.response.ListResult;
import com.kkwonsy.kkopservice.model.response.SingleResult;

@Service
public class ResponseService {

    public enum CommonResponse {
        SUCCESS(0, "성공"),
        FAIL(-1, "실패"),
        INTERNAL_SERVER_ERROR(-9999, "알 수 없는 오류 발생"),
        USER_NOT_FOUND(-1000, "존재하지 않는 회원"),
        SIGNIN_FAILED(-1001, "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않음"),
        ENTRY_POINT(-1002, "해당 리소스에 접근하기 위한 권한이 없음"),
        ACCESS_DENIED(-1003, "보유한 권한으로 접근할수 없는 리소스"),

        CANT_CANCEL_COUPON(-2002, "사용 취소 기간이 지난 쿠폰"),
        ALREADY_USED_COUPON(-2000, "이미 사용한 쿠폰");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult(CommonResponse code) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
