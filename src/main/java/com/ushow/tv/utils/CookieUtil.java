package com.ushow.tv.utils;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtil {
	private final static Logger log = LoggerFactory.getLogger(CookieUtil.class);
	private static final int MAX_COOKIE_PAYLOAD = 4096 - "wset_contents01=".length() - "81920<".length() - 1; //$NON-NLS-1$ //$NON-NLS-2$

	public static final int COOKIE_LIFE_10 = 10 * 24 * 60 * 60;

	/***
	 * @return null or String
	 */
	public static String getCookieValue(String name, HttpServletRequest request) {
		String ret = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (name.equals(cookies[i].getName())) {
					ret = cookies[i].getValue();
					break;
				}
			}
		}

		return ret;
	}

	public static void setCookieValue(String name, String value, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(COOKIE_LIFE_10);
		cookie.setPath(getCookiePath(request));
		response.addCookie(cookie);
	}

	protected static String getCookiePath(HttpServletRequest request) {
		String path = request.getContextPath() + '/';
		log.info("cookie path:" + path);
		return path;
	}

	public static void deleteCookie(String name, HttpServletRequest request, HttpServletResponse response) {
		deleteCookieUsingPath(name, response, getCookiePath(request));
	}

	public static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				cookie.setPath(getCookiePath(request));
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	protected static void deleteCookieUsingPath(String name, HttpServletResponse response, String cookiePath) {
		Cookie cookie = new Cookie(name, ""); //$NON-NLS-1$
		cookie.setPath(cookiePath);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/***
	 * Saves string in multiple browser cookies. Cookies can store limited
	 * length string. This method will attemt to split string among multiple
	 * cookies. The following cookies will be set name1=length <substing1
	 * name2=substring2 ... namen=substringn
	 * 
	 * @param data
	 *            a string containing legal characters for cookie value
	 * @throws IOException
	 *             when data is too long.
	 */
	public static void saveString(String name, String data, int maxCookies, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int len = data.length();
		int n = len / MAX_COOKIE_PAYLOAD;
		if (n > maxCookies) {
			throw new IOException("CookieUtil.tooManyCookiesNeeded");
		}
		for (int i = 1; i <= n; i++) {
			if (i == 1) {
				setCookieValue(name + "1", //$NON-NLS-1$
						len + "<" + data.substring(0, MAX_COOKIE_PAYLOAD), //$NON-NLS-1$
						request, response);
			} else {
				setCookieValue(name + i, data.substring(MAX_COOKIE_PAYLOAD * (i - 1), MAX_COOKIE_PAYLOAD * i), request, response);
			}
		}
		if (len % MAX_COOKIE_PAYLOAD > 0) {
			if (n == 0) {
				setCookieValue(name + "1", //$NON-NLS-1$
						len + "<" + data.substring(0, len), //$NON-NLS-1$
						request, response);
			} else {
				setCookieValue(name + (n + 1), data.substring(MAX_COOKIE_PAYLOAD * n, len), request, response);
			}
		}

		// if using less cookies than maximum, delete not needed cookies from
		// last time
		for (int i = n + 1; i <= maxCookies; i++) {
			if (i == n + 1 && len % MAX_COOKIE_PAYLOAD > 0) {
				continue;
			}
			if (getCookieValue(name + i, request) != null) {
				deleteCookie(name + i, request, response);
			} else {
				break;
			}
		}
	}

	/***
	 * @return null or String
	 */
	public static String restoreString(String name, HttpServletRequest request) {
		String value1 = CookieUtil.getCookieValue(name + "1", request); //$NON-NLS-1$
		if (value1 == null) {
			// no cookie
			return null;
		}
		String lengthAndSubstring1[] = value1.split("<"); //$NON-NLS-1$
		if (lengthAndSubstring1.length < 2) {
			return null;
		}
		int len = 0;
		try {
			len = Integer.parseInt(lengthAndSubstring1[0]);
		} catch (NumberFormatException nfe) {
			return null;
		}
		if (len <= 0) {
			return null;
		}
		StringBuffer data = new StringBuffer(len);
		data.append(lengthAndSubstring1[1]);
		int n = len / MAX_COOKIE_PAYLOAD;
		for (int i = 2; i <= n; i++) {
			String substring = CookieUtil.getCookieValue(name + i, request);
			if (substring == null) {
				return null;
			}
			data.append(substring);
		}
		if (len % MAX_COOKIE_PAYLOAD > 0 && n > 0) {
			String substring = CookieUtil.getCookieValue(name + (n + 1), request);
			if (substring == null) {
				return null;
			}
			data.append(substring);
		}

		if (data.length() != len) {
			return null;
		}

		return data.toString();
	}
}
