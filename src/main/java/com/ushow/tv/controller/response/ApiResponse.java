package com.ushow.tv.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

@Data
@Builder
@ToString
@Log4j2
public class ApiResponse {

    protected String code;
    protected String msg;
    protected Object data;

    public static ApiResponse create(ResponseCode code) {
        return create(code.getCode(), code.getMsg(), null);
    }

    public static ApiResponse create(ResponseCode code, Object data) {
        return create(code.getCode(), code.getMsg(), data);
    }

    public static ApiResponse create(String code, String msg) {

        return create(code, msg, null);
    }

    public static ApiResponse createError(final ResponseCode code, final Exception e) {

        final ApiResponse resp = ApiResponse.builder()
            .code(code.getCode()).msg(code.getMsg())
            .build();
        if (log.isDebugEnabled() && e != null) {
            resp.setData(e.getMessage());
        }
        return resp;
    }

    public static ApiResponse createError(final ResponseCode code, final String msg, final Exception e) {

        final ApiResponse resp = ApiResponse.builder()
            .code(code.getCode()).msg(code.getMsg())
            .build();
        if (!StringUtils.isEmpty(msg)) {
            resp.setMsg(msg);
        }
        if (log.isDebugEnabled() && e != null) {
            resp.setData(e.getMessage());
        }
        return resp;
    }

    public static ApiResponse create(String code, String msg, Object data) {

        return ApiResponse.builder()
            .code(code).msg(msg)
            .data(data)
            .build();
    }

    public static ApiResponse createSuccess() {

        return create(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg());
    }

    public static ApiResponse createSuccess(Object data) {

        return create(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

  public static ApiResponse createFailure(Object data) {

    return create(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMsg(), data);
  }

    public static ApiResponse createSuccess(String msg, Object data) {

        return create(ResponseCode.SUCCESS.getCode(), msg, data);
    }
}
