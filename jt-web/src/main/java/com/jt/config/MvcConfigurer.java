package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.intercepter.UserIntercepter;

/**
 * 很重要的一个类，识别自己的html
 * 
 * @author wanyinsheng
 *
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
	@Autowired
	private UserIntercepter userIntercepter;

	// 开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {

		configurer.setUseSuffixPatternMatch(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// url:www.jt.com/cart/xxxxxx
		// url:www.jt.com/order/xxxxx
		// *只能拦截一级，，**可以拦截多级
		registry.addInterceptor(userIntercepter).addPathPatterns("/cart/**", "/order/**");// 设置拦截器和拦截路径,拦截路径是可变参数
	}
}
