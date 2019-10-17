package com.kdf.etl.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * ip地址解析工具
 * 
 * @date 2017-05-09
 * @author 王者の南が少ない
 *
 */
public class IpDataHandler {

	private static long fileLength = -1;
	private static int dataLength = -1;
	private static Map<String, String> cacheMap = null;
	private static byte[] allData = null;

	static {
		File file = new File("src/main/resources/17monipdb.dat");
		DataInputStream inputStream = null;
		try {
			inputStream = new DataInputStream(new FileInputStream(file));
			fileLength = file.length();
			cacheMap = new HashMap<String, String>();
			if (fileLength > Integer.MAX_VALUE) {
				// throw new Exception("the filelength over 2GB");
			}

			dataLength = (int) fileLength;
			allData = new byte[dataLength];
			inputStream.read(allData, 0, dataLength);
			dataLength = (int) getbytesTolong(allData, 0, 4, ByteOrder.BIG_ENDIAN);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static long getbytesTolong(byte[] bytes, int offerSet, int size, ByteOrder byteOrder) {
		if ((offerSet + size) > bytes.length || size <= 0) {
			return -1;
		}
		byte[] b = new byte[size];
		for (int i = 0; i < b.length; i++) {
			b[i] = bytes[offerSet + i];
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(b);
		byteBuffer.order(byteOrder);

		long temp = -1;
		if (byteBuffer.hasRemaining()) {
			temp = byteBuffer.getInt();
		}
		return temp;
	}

	private static long ip2long(String ip) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ip);
		byte[] bytes = address.getAddress();
		long reslut = getbytesTolong(bytes, 0, 4, ByteOrder.BIG_ENDIAN);
		return reslut;
	}

	private static int getIntByBytes(byte[] b, int offSet) {
		if (b == null || (b.length < (offSet + 3))) {
			return -1;
		}

		byte[] bytes = Arrays.copyOfRange(allData, offSet, offSet + 3);
		byte[] bs = new byte[4];
		bs[3] = 0;
		for (int i = 0; i < 3; i++) {
			bs[i] = bytes[i];
		}

		return (int) getbytesTolong(bs, 0, 4, ByteOrder.LITTLE_ENDIAN);
	}

	private static String findGeography(String address) {
		String res = "";
		if (address == null) {
			return null;
		}

		if (dataLength < 4 || allData == null) {
			return null;
		}

		String ip = "127.0.0.1";
		try {
			ip = Inet4Address.getByName(address).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// String[] ipArray = {"8","8","8","8"};
		String[] ipArray = ip.split("\\.");
		int ipHeadValue = Integer.parseInt(ipArray[0]);
		if (ipArray.length != 4 || ipHeadValue < 0 || ipHeadValue > 255) {
			return null;
		}

		if (cacheMap.containsKey(ip)) {
			res = cacheMap.get(ip);
		} else {
			long numIp = 1;
			try {
				numIp = ip2long(address);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}

			int tempOffSet = ipHeadValue * 4 + 4;
			long start = getbytesTolong(allData, tempOffSet, 4, ByteOrder.LITTLE_ENDIAN);
			int max_len = dataLength - 1028;
			long resultOffSet = 0;
			int resultSize = 0;

			for (start = start * 8 + 1024; start < max_len; start += 8) {
				if (getbytesTolong(allData, (int) start + 4, 4, ByteOrder.BIG_ENDIAN) >= numIp) {
					resultOffSet = getIntByBytes(allData, (int) (start + 4 + 4));
					resultSize = (char) allData[(int) start + 7 + 4];
					break;
				}
			}

			if (resultOffSet <= 0) {
				return null;
			}

			byte[] add = Arrays.copyOfRange(allData, (int) (dataLength + resultOffSet - 1024),
					(int) (dataLength + resultOffSet - 1024 + resultSize));
			try {
				if (add == null) {
					cacheMap.put(ip, new String("no data found!!"));
				} else {
					cacheMap.put(ip, new String(add, "UTF-8"));
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			res = cacheMap.get(ip);
		}

		return res;
	}

	/**
	 * 地址
	 * 
	 * @param address
	 * @return
	 */
	public static IpResponse getIpInfo(String address) {
		IpResponse ipResponse = null;
		String res = "";
		if (InternalIpUtil.innerIP(address)) {
			ipResponse = new IpResponse();
			ipResponse.setArea(GlobalConstants.IP_LAN);
			ipResponse.setCity(GlobalConstants.IP_LAN);
			ipResponse.setCountry(GlobalConstants.IP_LAN);
			ipResponse.setRegion(GlobalConstants.IP_LAN);
			
			return ipResponse;
		}

		try {
			res = findGeography(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(res)) {
			String[] array = res.split("\t");
			if (array != null && array.length > 2) {
				ipResponse = new IpResponse();
				ipResponse.setCountry(array[0].trim());
				ipResponse.setRegion(array[1].trim());
				ipResponse.setCity(array[2].trim());
			}
		}
		// 淘宝api经常time out 注释掉
//        if (null == ipResponse) {
//            // 调用淘宝api
//            try {
//                Map<String, String> addressMap = AddressUtils.getAddressesTaobao("ip=" + address, "utf-8");
//                ipResponse = new IpResponse();
//                ipResponse.setCountry(addressMap.get("all"));
//                ipResponse.setRegion(addressMap.get("region").replace("省", ""));
//                ipResponse.setCity(addressMap.get("city").replace("市", ""));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }

		return ipResponse;
	}

	public static void main(String[] args) {
		IpResponse ipResponse = IpDataHandler.getIpInfo("121.35.211.41");
		BaseResponse a = new BaseResponse();
		a.setData(ipResponse);
		a.setCode("01");
		String returnStr = JSONObject.toJSONString(a);
		System.out.println(returnStr);

	}
}