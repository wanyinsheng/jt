package com.jt.intercepter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisCluster;

@Component // 将拦截器交给spring容器管理
public class UserIntercepter implements HandlerInterceptor {
	@Autowired
	private JedisCluster jedis;
	/**
	 * 在spring4中，必须实现三个方法，，spring5中，接口方法添加了default，，可以不用实现
	 */

	/**
	 * 处理之前拦截
	 * 
	 * true表示拦截放行
	 * 
	 * false表示拦截，，指一个明路，，登陆页面
	 * 
	 * 判断用户是否登陆： 1.获取cookie 2.获取登陆凭证token(TICKET) 3.判断redis中是否有缓存
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取cookie信息
		Cookie[] cookies=request.getCookies();
		String token=null;
		for (Cookie cookie : cookies) {
			if ("JT_TICKET".equals(cookie.getName())) {
				token=cookie.getValue();
				break;
			}
		}
		//判断token是否有效
		if (!StringUtils.isEmpty(token)) {
			//判断redis中是否有数据
			String userJSON=jedis.get(token);
			//redis中有数据，放行
			if (!StringUtils.isEmpty(userJSON)) {
				return true;
			}
			
		}
		//以上有一个不满足就重定向到登陆页面
		response.sendRedirect("/user/login");
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	/**
	 * 处理之后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 视图解析之后拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
