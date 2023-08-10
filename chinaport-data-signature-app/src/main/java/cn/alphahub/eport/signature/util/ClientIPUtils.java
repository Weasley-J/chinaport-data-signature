package cn.alphahub.eport.signature.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 获取客户端的真实 IP 地址
 *
 * @author weasley
 * @since 1.1.0
 */
public final class ClientIPUtils {
    static final String UNKNOWN = "unknown";

    private ClientIPUtils() {
    }

    public static String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (null == ipAddress || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (null == ipAddress || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ipAddress || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (null == ipAddress || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (null == ipAddress || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 如果客户端经过了多级反向代理，那么 X-Forwarded-For 中的值是以逗号分隔的 IP 地址列表，取第一个非 unknown 的 IP 地址
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress;
    }
}
