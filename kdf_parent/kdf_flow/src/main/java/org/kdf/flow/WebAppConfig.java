package org.kdf.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @title: WebAppConfig.java 
 * @package org.kdf.flow 
 * @description: 拦截器加载
 * @author: 、T
 * @date: 2019年9月29日 下午3:43:07 
 * @version: V1.0
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Autowired
	private RequestHandler requestHandler;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(requestHandler)
       		.excludePathPatterns("/error")
       		.addPathPatterns("/**");
    }

	
}
