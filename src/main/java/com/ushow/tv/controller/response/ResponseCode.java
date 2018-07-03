package com.ushow.tv.controller.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResponseCode {

    SUCCESS("A00000", "成功"),
    FAILURE("A00001", "失败"),
    ERR_SYS("A00002", "系统错误"),

    ERR_ILLEGAL_PARAM("A00100", "非法参数"),
    ERR_ILLEGAL_DATE("A00101", "日期格式不正确"),
    RESOURCE_ACCESS_EXCEPTION("A00102", "非法请求"),
    ERR_ILLEGAL_REQUEST("A00103", "非法请求"),
    ERR_DUPLICATE_REQUEST("A00104", "重复请求"),

    NOT_LOGIN("B00001", "用户未登录"),
    ILLEGAL_SECRET_KEY("B00002", "非法签名"),
    NO_AUTH("B00003","无权访问"),
    LIMIT_REQ("C00000","请求超限");

    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
