package com.ushow.tv.controller;

import com.ushow.tv.controller.common.Consts;
import com.ushow.tv.controller.response.ApiResponse;
import com.ushow.tv.controller.response.ResponseCode;
import com.ushow.tv.utils.IPUtils;
import com.ushow.tv.utils.JSONUtils;
import com.ushow.tv.utils.RequestUtils;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UrlPathHelper;

/**
 * Created by zhonghui on 2017/6/29.
 */
public class AbstractController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static UrlPathHelper urlPathHelper = new UrlPathHelper();

    private void reportException(Exception ex, HttpServletRequest request, boolean printStack) {
        String lookupPath = urlPathHelper.getLookupPathForRequest(request);
        String queryString = urlPathHelper.getOriginatingQueryString(request);
        String message = String
          .format("ip:%s, lookupPath:%s, queryString:%s, exception:%s", IPUtils.getRealIp(request), lookupPath, queryString,
                ex.getMessage());
        if (printStack) {
            log.error(message, ex);
        } else {
            log.error(message);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String handleException(Exception ex, HttpServletRequest request) {
        this.reportException(ex, request, false);
        return this.createResponse(Consts.ERR_SYS, ex.getMessage(), request);
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String handleNumberFormatException(NumberFormatException ex, HttpServletRequest request) {
        this.reportException(ex, request, true);
        return this.createResponse(ResponseCode.ERR_ILLEGAL_PARAM.getCode(), "试图将一个字符串转换为数字类型时发生异常，" + ex.getMessage(), request);
    }

    @ExceptionHandler({TypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String handleTypeMismatchException(TypeMismatchException ex, HttpServletRequest request) {
        this.reportException(ex, request, false);
        return this.createResponse(ResponseCode.ERR_ILLEGAL_PARAM.getCode(), "参数不匹配：" + ex.getMessage(), request);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        this.reportException(ex, request, false);
        return this.createResponse(ResponseCode.ERR_ILLEGAL_PARAM.getCode(), ex.getMessage(), request);
    }


    public String createSuccessResponse(HttpServletRequest request) {
        return this.createSuccessResponse(null, request);
    }

    public String createSuccessResponse(Object data, HttpServletRequest request) {
        return this.createResponseWithData(ResponseCode.SUCCESS, data, request);
    }

    public String createFailureResponse(HttpServletRequest request) {
        return this.createResponseWithData(ResponseCode.FAILURE, null, request);
    }

    public String createFailureResponse(HttpServletRequest request, String msg) {
        return this.createResponse(ResponseCode.FAILURE.getCode(), msg, null, request);
    }

    public String createResponse(ResponseCode code, HttpServletRequest request) {
        return this.createResponseWithData(code, null, request);
    }

    private String createResponseWithData(ResponseCode code, Object data, HttpServletRequest request) {
        return this.createResponse(code.getCode(), code.getMsg(), data, request);
    }

    public String createResponse(String code, String message, HttpServletRequest request) {
        return this.createResponse(code, message, null, request);
    }

    public String createResponse(String code, String message, Object data, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", message);
        if (data != null) {
            result.put("data", data);
        }
        return this.createResponse(result, request);
    }

    /**
     * 返回指定code的带有数据、附加数据的json串
     *
     * @param code
     * @param data
     * @param ext
     * @param msg
     * @return
     */
    public String createResponse(String code, Object data, Map<String, Object> ext, String msg, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        if (data != null) {
            result.put("data", data);
        }
        if (ext != null) {
            result.putAll(ext);
        }
        return this.createResponse(result, request);
    }

    /**
     * 返回data的json格式的字符串
     *
     * @param data
     * @return
     */
    public String createResponse(Map<String, Object> data, HttpServletRequest request) {
        String json = "";
        if (data != null) {
            json = JSONUtils.writeValueAsString(data);
        }
        StringBuilder sb = new StringBuilder();
        String cb = RequestUtils.xssEncode(request.getParameter("cb"));
        String jsonpcb = RequestUtils.xssEncode(request.getParameter("jsonpcb"));
        String callback = RequestUtils.xssEncode(request.getParameter("callback"));
        String uploadcb = RequestUtils.xssEncode(request.getParameter("uploadcb"));
        if (StringUtils.isNotBlank(jsonpcb) || StringUtils.isNotBlank(callback)) {
            sb.append("try{").append(StringUtils.defaultIfBlank(callback, jsonpcb)).append("(").append(json).append(");}catch(e){}");
        } else if (StringUtils.isNotBlank(cb)) {
            sb.append("var ").append(cb).append(" = ").append(json);
        } else if (StringUtils.isNotBlank(uploadcb)) {
            sb.append("<script>try{").append(
              String.format("document.domain=\"iqiyi.com\";window.parent." + uploadcb + "(%s);", json))
                    .append("}catch(e){}</script>");
        } else {
            sb.append(json);
        }
        return sb.toString();
    }

    public Map<String, Object> createSuccessMapResponse() {
        return this.createMapResponse(Consts.SUCCESS, null, null);
    }

    public Map<String, Object> createSuccessMapResponse(String message) {
        return this.createMapResponse(Consts.SUCCESS, null, message);
    }

    public Map<String, Object> createFailureMapResponse(String message) {
        return this.createMapResponse(Consts.FAILURE, null, message);
    }

    /**
     * 返回指定code的带有数据、附加数据的map对象
     *
     * @param code
     * @param data
     * @param msg
     * @return
     */
    public Map<String, Object> createMapResponse(String code, Object data, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        if (data != null) {
            result.put("data", data);
        }
        return result;
    }

    /**
     * 返回指定code的带有数据、附加数据的ApiResponse对象
     *
     * @param code
     * @param data
     * @param msg
     * @return
     */
    public ApiResponse createApiResponse(String code, String msg, Object data) {
        ApiResponse result = ApiResponse.create(code, msg, data);
        return result;
    }

    /**
     * 返回指定code的带有数据、附加数据的ApiResponse对象
     *
     * @param data
     * @return
     */
    public ApiResponse createApiResponse(ResponseCode responseCode, Object data) {
        ApiResponse result = createApiResponse(responseCode.getCode(), responseCode.getMsg(), data);
        return result;
    }

}
