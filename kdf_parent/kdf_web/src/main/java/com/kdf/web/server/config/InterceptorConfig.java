package com.kdf.web.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * InterceptorConfig 资源拦截器
 * 
 * @author mengpp
 * @date 2019年6月27日14:20:04
 */
@EnableWebMvc
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public HandlerInterceptor securityInterceptor() {
		return new SecurityInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration ir = registry.addInterceptor(securityInterceptor());

		ir.addPathPatterns("/**");
		ir.excludePathPatterns("/", "/static/**", "/js/**", "/loginAjax", "/defaultKaptcha");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/templates/js/");
	}

}
