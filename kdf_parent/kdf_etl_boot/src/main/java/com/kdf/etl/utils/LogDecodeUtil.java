package com.kdf.etl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.kdf.etl.utils.ua.Device;

import net.sf.json.JSONObject;

public class LogDecodeUtil {

	static Resource resource = null;
	static Map<String, List<Map<String, String>>> matchers = null;
	// 静态块加载配置文件
	static {
		resource = new ClassPathResource("uamatchers.json");
		matchers = getMatchers();
	}

//	private static String testStr = "GET /?user_agent=Mozilla%2F5.0+%28Windows+NT+6.1%3B+Win64%3B+x64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F77.0.3865.90+Safari%2F537.36&method=GET&port=62000&ip=192.168.31.8&url=http%3A%2F%2Flocalhost%3A8080%2Fadd HTTP/1.1";

	private static String testStr = "GET /?request_time=1570698913&method=GET&port=49577&ip=192.168.16.104&appid=pbkj_123&url=http%3A%2F%2F192.168.31.8%3A8080%2FDddddddd%2Ffffff&user_agent=Mozilla%2F5.0+%28Linux%3B+U%3B+Android+8.1.0%3B+zh-cn%3B+OPPO+R11+Build%2FOPM1.171019.011%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F66.0.3359.126+MQQBrowser%2F9.7+Mobile+Safari%2F537.36 HTTP 1.1";
//	private static String testStr = "Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3";

	public static void main(String[] args) {
//		System.out.println(isMobileDevice(testStr));
		System.out.println(log2Map(testStr));
	}

	/**
	 * 判断请求来自手机端还是电脑端
	 */
	public static String getDevice(String requestHeader) {
		/*
		 * android :所有安卓设备 mas os :iphone windows phone :windows系统手机
		 */
		String[] deviceArray = new String[] { "android", "mas os", "windows phone" };
		if (null == requestHeader) {
			return GlobalConstants.DEFAULT_VALUE;
		}
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return GlobalConstants.DEVICE_TYPE_MOBILE;
			}
		}
		return GlobalConstants.DEVICE_TYPE_PC;
	}

	public static Map<String, String> log2Map(String logStr) {
		System.out.println("====================" + logStr);
		Map<String, String> resultMap = new HashMap<>();
		try {
			String subLogStr = logStr.substring(logStr.indexOf("?") + 1, logStr.lastIndexOf("H")).trim();
			String decodeLogStr = URLDecoder.decode(subLogStr, "utf-8");

			String[] decodeLogArray = decodeLogStr.split("&");
			for (String s : decodeLogArray) {
				String[] mapStr = s.split("=");

				switch (mapStr[0]) {
				case "user_agent":
					Device d = new Device(mapStr[1], matchers);
//					UserAgentInfo info = UserAgentUtil.analyticUserAgent(mapStr[1]);
					resultMap.put("os_name", d.getOs());
					resultMap.put("os_version", d.getOsVersion());
					resultMap.put("browser_name", d.getClient());
					resultMap.put("browser_version", d.getVersion());
					resultMap.put("device_type", d.getDevice());
					break;
				case "ip":
					resultMap.put("ip", mapStr[1]);
					IpResponse ipResponse = IpDataHandler.getIpInfo(mapStr[1]);
					String country = GlobalConstants.DEFAULT_VALUE;
					String province = GlobalConstants.DEFAULT_VALUE;
					String city = GlobalConstants.DEFAULT_VALUE;

					if (null != ipResponse) {
						country = ipResponse.getCountry();
						province = ipResponse.getRegion();
						city = ipResponse.getCity();
					}
					resultMap.put("country", country);
					resultMap.put("province", province);
					resultMap.put("city", city);
					break;

				default:
					resultMap.put(mapStr[0], mapStr[1]);
					break;
				}
			}

		} catch (Exception e) {
//			e.printStackTrace();
		}

		return resultMap;
	}

	public static String log2Str(String logStr) {
		String resultStr = "";
		try {
			String subLogStr = logStr.substring(logStr.indexOf("?") + 1, logStr.lastIndexOf("H")).trim();
			resultStr = URLDecoder.decode(subLogStr, "utf-8");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return resultStr;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, List<Map<String, String>>> getMatchers() {
		Map<String, List<Map<String, String>>> matchers = new HashMap<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			StringBuilder message = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				message.append(line);
			}
			String result = message.toString().replace("\r\n", "");
			matchers = JSONObject.fromObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matchers;
	}

}
