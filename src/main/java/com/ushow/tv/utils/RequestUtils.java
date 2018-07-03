package com.ushow.tv.utils;

import com.google.common.collect.Maps;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 用于处理HTTP请求的工具类
 *
 * @author zhonghui@qiyi.com
 * @date 2017年06月29日 下午3:16:54
 */
public class RequestUtils {

    /**
     * 判断是否为搜索引擎
     *
     * @param req HttpServletRequest
     * @return ture:robot, false,is not robot
     */
    public static boolean isRobot(HttpServletRequest req) {
        String ua = req.getHeader("user-agent");
        if (StringUtils.isBlank(ua))
            return false;
        return (ua != null && (ua.indexOf("Baiduspider") != -1 || ua.indexOf("Googlebot") != -1
                || ua.indexOf("sogou") != -1 || ua.indexOf("sina") != -1 || ua.indexOf("iaskspider") != -1
                || ua.indexOf("ia_archiver") != -1 || ua.indexOf("Sosospider") != -1 || ua.indexOf("YoudaoBot") != -1
                || ua.indexOf("yahoo") != -1 || ua.indexOf("yodao") != -1 || ua.indexOf("MSNBot") != -1
                || ua.indexOf("spider") != -1 || ua.indexOf("Twiceler") != -1 || ua.indexOf("Sosoimagespider") != -1
                || ua.indexOf("naver.com/robots") != -1 || ua.indexOf("Nutch") != -1 || ua.indexOf("spider") != -1));
    }

    /**
     * 获取用户访问URL中的根域名 例如: www.dlog.cn -> dlog.cn
     *
     * @param host 域名
     * @return 根域名
     */
    public static String getDomainOfServerName(String host) {
        if (IPUtils.isIPAddr(host))
            return null;
        String[] names = StringUtils.split(host, '.');
        int len = names.length;
        if (len == 1)
            return null;
        if (len == 3) {
            return makeup(names[len - 2], names[len - 1]);
        }
        if (len > 3) {
            String dp = names[len - 2];
            if (dp.equalsIgnoreCase("com") || dp.equalsIgnoreCase("gov") || dp.equalsIgnoreCase("net")
                    || dp.equalsIgnoreCase("edu") || dp.equalsIgnoreCase("org"))
                return makeup(names[len - 3], names[len - 2], names[len - 1]);
            else
                return makeup(names[len - 2], names[len - 1]);
        }
        return host;
    }

    /**
     * 把字符串数组串起来，中间加“.”，用于域名的串接
     *
     * @param ps 字符串数组
     * @return 字符串
     */
    private static String makeup(String... ps) {
        StringBuilder s = new StringBuilder();
        for (int idx = 0; idx < ps.length; idx++) {
            if (idx > 0)
                s.append('.');
            s.append(ps[idx]);
        }
        return s.toString();
    }

    /**
     * 获取HTTP端口
     *
     * @param req HttpServletRequest
     * @return 端口数值
     * @throws MalformedURLException
     */
    public static int getHttpPort(HttpServletRequest req) {
        try {
            return new URL(req.getRequestURL().toString()).getPort();
        } catch (MalformedURLException excp) {
            return 80;
        }
    }

    /**
     * 获取浏览器提交的整形参数
     *
     * @param req          HttpServletRequest
     * @param param        参数名
     * @param defaultValue 默认值
     * @return int
     */
    public static int getParam(HttpServletRequest req, String param, int defaultValue) {
        return NumberUtils.toInt(req.getParameter(param), defaultValue);
    }

    /**
     * 获取浏览器提交的整形参数
     *
     * @param req          HttpServletRequest
     * @param param        参数名
     * @param defaultValue 默认值
     * @return long
     */
    public static long getParam(HttpServletRequest req, String param, long defaultValue) {
        return NumberUtils.toLong(req.getParameter(param), defaultValue);
    }

    /**
     * 对getParameterValues的封装，转换成long数组
     *
     * @param req  HttpServletRequest
     * @param name 参数名
     * @return long数组
     */
    public static long[] getParamValues(HttpServletRequest req, String name) {
        String[] values = req.getParameterValues(name);
        if (values == null)
            return null;
        return (long[]) ConvertUtils.convert(values, long.class);
    }

    /**
     * 获取浏览器提交的字符串参数
     *
     * @param req          HttpServletRequest
     * @param param        参数名
     * @param defaultValue 默认值
     * @return String
     */
    public static String getParam(HttpServletRequest req, String param, String defaultValue) {
        String value = req.getParameter(param);
        return (StringUtils.isEmpty(value)) ? defaultValue : value;
    }

    /**
     * 格式化中文字符，防止出现乱码
     *
     * @param str
     * @return
     */
    private static String codeToString(String str) {
        String strString = str;
        try {
            byte tempB[] = strString.getBytes("ISO-8859-1");
            strString = new String(tempB);
            return strString;
        } catch (Exception e) {
            return strString;
        }
    }

    /**
     * 获取完整的Url
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String getBackUrl(HttpServletRequest request) throws Exception {
        String strBackUrl = "";
        try {
            strBackUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + request.getServletPath() + "?" + codeToString(request.getQueryString());
            strBackUrl = java.net.URLEncoder.encode(strBackUrl, "gbk");
        } catch (Exception e) {
            throw e;
        }
        return strBackUrl;
    }

    /**
     * 获得POST 过来参数设置到新的params中
     *
     * @param requestParams POST 过来参数Map
     * @return 新的Map
     */
    public static Map<String, String> genMapByRequestParas(Map requestParams) {
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return params;
    }

    /**
     * 解析查询参数
     *
     * @param queryString
     * @return
     */
    public static Map<String, Object> parseQueryString(String queryString) {
        Map<String, Object> retMap = null;
        if (StringUtils.isNotBlank(queryString) && StringUtils.contains(queryString, "&")) {
            retMap = Maps.newHashMap();
            String[] params = StringUtils.split(queryString, "&");
            if (params != null && params.length > 0) {
                for (String param : params) {
                    String[] values = StringUtils.splitByWholeSeparator(param, "=");
                    if (values != null && values.length == 2) {
                        if (StringUtils.isNotBlank(values[0])) {
                            retMap.put(values[0], values[1]);
                        }
                    }
                }
            }
        }
        return retMap;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String XRequestedWith = request.getHeader("X-Requested-With");
        return StringUtils.isNotBlank(XRequestedWith) && "XMLHttpRequest".equalsIgnoreCase(XRequestedWith);
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    public static String xssEncode(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&prime;");// &acute;");
                    break;
                case '′':
                    sb.append("&prime;");// &acute;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '＂':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("＆");
                    break;
                case '#':
                    sb.append("＃");
                    break;
                case '\\':
                    sb.append('￥');
                    break;
                case '=':
                    sb.append("&#61;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static String toYinheUrl(String iqiyiUrl) {
        return StringUtils.replaceEach(iqiyiUrl, new String[]{"www.qiyipic.com", ".qiyipic.com", ".qiyi.com", ".iqiyi.com"}, new String[]{"pic.ptqy.gitv.tv", ".ptqy.gitv.tv", ".ptqy.gitv.tv", ".ptqy.gitv.tv"});
    }

    public static boolean isYinheUrl(String url) {
        return StringUtils.contains(url, "ptqy.gitv.tv");
    }

    public static <E> E getParam(HttpServletRequest request, String param, final Class<E> elementType) throws Exception {
        if (request == null) {
            return null;
        }
        String value = request.getParameter(param);

        if (elementType == Object.class) {
            return (E) value;
        } else {
            Object obj = elementType.getMethod("valueOf", String.class).invoke(null, value);
            return obj == null ? null : (E) obj;
        }
    }

    public static boolean isInternalInvoke(HttpServletRequest request) {
        return (StringUtils.equals("renzheng.qiyi.domain",
                request.getServerName())
                || request.getServerPort() == 8080) && IPUtils.isLANIp(request);
    }

}