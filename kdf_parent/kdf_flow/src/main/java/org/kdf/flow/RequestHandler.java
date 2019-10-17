package org.kdf.flow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title: RequestHandler.java 
 * @package org.kdf.flow 
 * @description: 拦截器
 * @author: 、T
 * @date: 2019年9月29日 下午3:42:11 
 * @version: V1.0
 */
@Component
@Slf4j
@Order(-1)
public class RequestHandler implements HandlerInterceptor {

	public static final String X_REAL_IP = "X-Real-IP";
    public static final String UNKNOWN = "unknown";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    public static final String LOCALHOST_IP = "127.0.0.1";
    public static final String LOCALHOST_IP_16 = "0:0:0:0:0:0:0:1";
    public static final int MAX_IP_LENGTH = 15;
    public static final String COMMA = ",";
	
    public static final String URL_KEY = "url";
    public static final String IP_ADDR_KEY = "ip";
    public static final String PORT_KET = "port";
    public static final String REQUEST_METHOD_KEY = "method";
	public static final String USER_AGENT = "user_agent";
    public static final String REQUEST_TIME = "request_time";
    public static final String APPID_KEY = "appid";
    public static final String APPID_VALUE = "kdf_web";
    
//    public static final String NGINX_REQUEST_URL = "http://47.93.53.233:80";
    public static final String NGINX_REQUEST_URL = "http://192.168.31.37";
    
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String url = request.getRequestURL().toString();
		String ipAddr = getIpAddress(request);
		int port = request.getRemotePort();
		String method = request.getMethod();
		String userAgent = request.getHeader("User-Agent");
		ThreadTask.getInstance().addTask(() -> {
			HttpClient.doGet(NGINX_REQUEST_URL, initParaMap(url, ipAddr, port, method, userAgent, String.valueOf(System.currentTimeMillis()).substring(0, 10)));
		});
		
		return true;
	}

	/**
	 * 获取真实ip
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader(X_REAL_IP);
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(HTTP_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST_IP.equals(ipAddress) || LOCALHOST_IP_16.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("获取IP地址, 出现异常={}", e.getMessage(), e);
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
//            log.info("获取IP地址 ipAddress={}", ipAddress);
        }
        // 对于通过多个代理的情况, 第一个IP为客户端真实IP,多个IP按照','分割 //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > MAX_IP_LENGTH) {
            if (ipAddress.indexOf(COMMA) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(COMMA));
            }
        }
        return ipAddress;
    }
	
	/**
	 * 拼接请求参数
	 * @param url					请求全路径
	 * @param ip					客户端请求真实ip
	 * @param port				客户端请求端口
	 * @param method			客户端请求方式
	 * @param clientType		客户端类型
	 * @return 
	 */
	public static Map<String, String> initParaMap(String url, String ip, int port, String method, String userAgent, String requestTime) {
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put(URL_KEY, url);
		paraMap.put(IP_ADDR_KEY, ip);
		paraMap.put(PORT_KET, String.valueOf(port));
		paraMap.put(REQUEST_METHOD_KEY, method);
		paraMap.put(USER_AGENT, userAgent);
		paraMap.put(REQUEST_TIME, requestTime);
		paraMap.put(APPID_KEY, APPID_VALUE);
		return paraMap;
	}
}
