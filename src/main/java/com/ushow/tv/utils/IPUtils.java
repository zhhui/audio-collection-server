package com.ushow.tv.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class IPUtils {
	/**
	 * 获取客户端IP地址，如果是内网ip则返回的是nginx机器的地址
	 * 
	 * @param request HttpServletRequest
	 * @return IP地址
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip)) {
						continue;
					}
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.")
							&& !"127.0.0.1".equals(tmpip)) {
						return tmpip;
					}
				}
			}
		}
		ip = request.getHeader("x-real-ip");
		if (isIPAddr(ip)) {
			return ip;
		}
		ip = request.getRemoteAddr();
		if (ip.indexOf('.') == -1) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	/**
	 * 如果是内网ip则直接返回
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		// log.info("X-Forwarded-For:{},getRemoteAddr:{}", new Object[] { ip, request.getRemoteAddr() });
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip)) {
						continue;
					}
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !StringUtils.startsWith(ip, "192.168.") || !"127.0.0.1".equals(ip)) {
						return tmpip;
					}
				}
			}
		}
		ip = request.getHeader("x-real-ip");
		if (isIPAddr(ip)) {
			return ip;
		}
		ip = request.getRemoteAddr();
		if (ip.indexOf('.') == -1) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	/**
	 * 是否是局域网ip
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLANIp(HttpServletRequest request) {
		String ip = IPUtils.getRemoteAddress(request);
		return StringUtils.startsWith(ip, "10.") || StringUtils.startsWith(ip, "192.168.") || "127.0.0.1".equals(ip);
	}

	/**
	 * 判断字符串是否是一个IP地址
	 * 
	 * @param addr 字符串
	 * @return true:IP地址，false：非IP地址
	 */
	public static boolean isIPAddr(String addr) {
		if (StringUtils.isEmpty(addr)) {
      return false;
    }
		String[] ips = StringUtils.split(addr, '.');
		if (ips.length != 4) {
      return false;
    }
		try {
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0 && ipc <= 255 && ipd >= 0 && ipd <= 255;
		} catch (Exception e) {
		}
		return false;
	}

	public static int[] parseIPMask(String ipmask) {
		String[] chunks = ipmask.split("/");
		if (chunks.length <= 0 || chunks.length > 2) {
			return null;
		}
		int[] ret = new int[2];
		ret[0] = ip2int(chunks[0]);
		if (ret[0] == 0) {
			return null;
		}
		if (chunks.length == 1) {
			ret[1] = 32;
		} else {
			ret[1] = NumberUtils.toInt(chunks[1]);
		}
		return ret;
	}

	public static int ip2int(String ip) {
		InetAddress address;
		try {
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return 0;
		}
		// 在给定主机名的情况下确定主机的 IP 址。
		byte[] bytes = address.getAddress();// 返回此 InetAddress 对象的原始 IP 地址
		int a, b, c, d;
		a = 0x0FF & (bytes[0]);
		b = 0x0FF & (bytes[1]);
		c = 0x0FF & (bytes[2]);
		d = 0x0FF & (bytes[3]);
		int result = (a << 24) | (b << 16) | (c << 8) | d;
		return result;
	}

	public static long ip2long(String ip) {
		int ipNum = ip2int(ip);
		return 0x0FFFFFFFF & ipNum;
	}

	public static String int2ip(int ip) {
		int a = 0x0FF & (ip >> 24);
		int b = 0x0FF & (ip >> 16);
		int c = 0x0FF & (ip >> 8);
		int d = 0x0FF & (ip >> 0);
		return new StringBuilder(20).append(a).append(".").append(b).append(".").append(c).append(".").append(d)
				.toString();
	}

	/**
	 * 获取客户端IP地址，支持proxy
	 * 
	 * @param req HttpServletRequest
	 * @return IP地址
	 */
	public static String getRemoteAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip)) {
            continue;
          }
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.")
							&& !"127.0.0.1".equals(tmpip)) {
						return tmpip.trim();
					}
				}
			}
		}
		ip = req.getHeader("x-real-ip");
		if (isIPAddr(ip)) {
      return ip;
    }
		ip = req.getRemoteAddr();
		if (ip.indexOf('.') == -1) {
      ip = "127.0.0.1";
    }
		return ip;
	}

	public static String getRemotePrivateAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip)) {
            continue;
          }
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip)) {
						return tmpip.trim();
					}
				}
			}
		}
		ip = req.getHeader("x-real-ip");
		if (isIPAddr(ip)) {
      return ip;
    }
		ip = req.getRemoteAddr();
		if (ip.indexOf('.') == -1) {
      ip = "127.0.0.1";
    }
		return ip;
	}
	
	private static final Pattern LAN_IP_START = Pattern.compile("^(?:10.|192.168.|127.0.0.1).*");

	/**
	 * 是否是局域网ip
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLANIp2(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip)) {
			ip = request.getHeader("x-real-ip");
			if (StringUtils.isBlank(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		if (StringUtils.isNotBlank(ip)) {
			return LAN_IP_START.matcher(ip).matches();
		} else {
			return false;
		}
	}

}