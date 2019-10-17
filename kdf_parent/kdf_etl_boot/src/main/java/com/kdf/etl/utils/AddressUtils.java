package com.kdf.etl.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class AddressUtils {
	
	public static String getIp;
	
    /**
     * 
     * @param content 请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> getAddresses(String content, String encodingString)
            throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        String returnStr = AddressUtils.getResult(content, encodingString);
        if (returnStr != null && !"".equals(returnStr)) {
            BaseResponse baseResponse = (BaseResponse) JSONObject.parseObject(returnStr, BaseResponse.class);
            IpResponse ipResponse = baseResponse.getData();
            map.put("region", ipResponse.getRegion());
            map.put("city", ipResponse.getCity());
            map.put("all", ipResponse.getRegion() + ipResponse.getCity());
            return map;
        }
        return null;
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String getResult(String content, String encodingString) {
        String url = getIp + "?" + content + "&type=json";
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性"
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("X-Powered-By", "ASP.NET");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                // System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * unicode 转换成 中文
     * 
     * @author fanhui 2007-3-15
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException("Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public static Map getAddressesTaobao(String content, String encodingString) throws UnsupportedEncodingException {
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = getResultTaobao(urlStr, content, encodingString);
        if (returnStr != null) {
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return null;// 鏃犳晥IP锛屽眬鍩熺綉娴嬭瘯
            }
            String region = decodeUnicode((temp[5].split(":"))[1].replaceAll("\"", ""));
            String city = decodeUnicode((temp[7].split(":"))[1].replaceAll("\"", ""));
            String Address = region + city;

            Map<String, String> map = new HashMap<String, String>();
            map.put("region", region);
            map.put("city", city);
            map.put("all", region + city);

            return map;
        }

        return null;
    }

    private static String getResultTaobao(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 鏂板缓杩炴帴瀹炰緥
            connection.setConnectTimeout(2000);// 璁剧疆杩炴帴瓒呮椂鏃堕棿锛屽崟浣嶆绉�
            connection.setReadTimeout(2000);// 璁剧疆璇诲彇鏁版嵁瓒呮椂鏃堕棿锛屽崟浣嶆绉�
            connection.setDoOutput(true);// 鏄惁鎵撳紑杈撳嚭娴� true|false
            connection.setDoInput(true);// 鏄惁鎵撳紑杈撳叆娴乼rue|false
            connection.setRequestMethod("POST");// 鎻愪氦鏂规硶POST|GET
            connection.setUseCaches(false);// 鏄惁缂撳瓨true|false
            connection.connect();// 鎵撳紑杩炴帴绔彛
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 鎵撳紑杈撳嚭娴佸線瀵圭鏈嶅姟鍣ㄥ啓鏁版嵁
            out.writeBytes(content);// 鍐欐暟鎹�,涔熷氨鏄彁浜や綘鐨勮〃鍗� name=xxx&pwd=xxx
            out.flush();// 鍒锋柊
            out.close();// 鍏抽棴杈撳嚭娴�
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 寰�瀵圭鍐欏畬鏁版嵁瀵圭鏈嶅姟鍣ㄨ繑鍥炴暟鎹�
            // ,浠ufferedReader娴佹潵璇诲彇
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 鍏抽棴杩炴帴
            }
        }
        return null;
    }

}
