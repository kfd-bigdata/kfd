package org.kdf.flow;

import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title: HttpClient.java 
 * @package org.kdf.flow 
 * @description: httpclient
 * @author: 、T
 * @date: 2019年9月29日 下午3:43:41 
 * @version: V1.0
 */
@Slf4j
public class HttpClient {

	/**
	 * httpclient发送get请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {
		// 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
        	// 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
		} catch (Exception e) {
			log.error("发送get请求异常={}", e.getMessage(), e);
		} finally {
			try {
				if (response != null) {
	                response.close();
	            }
	            httpclient.close();
			} catch (Exception e2) {
				log.error("httpclient关闭异常={}", e2.getMessage(), e2);
			}
		}
        return resultString;
	}
}
