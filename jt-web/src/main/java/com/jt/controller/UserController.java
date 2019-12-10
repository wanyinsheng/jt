package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder.PublisherEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	// 没有使用框架
//	@Autowired
//	private UserService userService;
	// 这里是消费者
	// 使用dubbo，依赖公共接口
	@Reference(timeout = 3000, check = false)
	private DubboUserService userService;

	@Autowired
	private JedisCluster jedis;

	/**
	 * 登陆注册页面跳转
	 * 
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/{moduleName}") // restful风格
	public String index(@PathVariable String moduleName) {
		return moduleName;
	}

	/**
	 * 点击登录之后：url=http://www.jt.com/user/doRegister 使用httpClient技术实现
	 */
	@RequestMapping("/doRegister")
	@ResponseBody // 返回的是字符串
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail();
		}
	}

	/**
	 * 实现用户登陆 http://www.jt.com/user/doLogin?r=0.6338983183200047
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user, HttpServletResponse response) {
		try {
			// 调用sso系统获取秘钥
			String token = userService.findUserByUP(user);
			// 如果数据不为空，将数据保存到cookie中
			// cookie中的key为JT_TICKET
			if (!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET", token);//jedis.setex(token, 7*24*3600, userJSON);//tocken为键
				cookie.setMaxAge(7 * 24 * 3600);// cookie的生命周期，，设置为0 表示立即删除，小于0表示会话结束后删除
				cookie.setDomain("jt.com");// 二级域名也可以使用，，实现数据共享
				cookie.setPath("/");// cookie的权限，一般默认写'/'
				response.addCookie(cookie);// 将cookie保存在客户端
				return SysResult.ok();
			}

		} catch (Exception e) {

		}
		return SysResult.fail();
	}

	/**
	 * 点击退出，返回到首页 http://www.jt.com/user/logout.html
	 * 
	 * 实现删除redis，，删除cookie
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// 当用户退出登录时,重定向到首页
		Cookie[] cookies = request.getCookies();
		// 存在cooki才循环遍历
		if (cookies.length != 0) {
			String token = null;
			for (Cookie cookie : cookies) {
				if ("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					System.out.println(token);
					break;// 找到数据直接跳出循环
				}
			}
			// 判断cookie数据是否有值，删除redis，，删除cookie
			if (!StringUtils.isEmpty(token)) {
				jedis.del(token);
				// 删除cookie时，，是新new一个cookie对象，，设置空串，设置立即删除
				Cookie cookie = new Cookie("JT_TICKET", "");
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setDomain("jt.com");// 将共享的cookie也删除，，与单点登录的cookie保持一致
				response.addCookie(cookie);// 将cookie发送到客户端
			}
		}
		return "redirect:/";
	}

}
