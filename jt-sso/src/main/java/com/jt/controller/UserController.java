package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JedisCluster jedis;

	/**
	 * 检查注册的用户名，手机号是否可用，使用JSONP技术实现
	 * 
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param, @PathVariable Integer type, String callback) {
		JSONPObject object = null;
		try {
			boolean flag = userService.checkUser(param, type);
			object = new JSONPObject(callback, SysResult.ok(flag));
		} catch (Exception e) {
			object = new JSONPObject(callback, SysResult.fail());
		}
		return object;
	}

	/**
	 * 处理前台传过来的用户注册，，使用了httpClient技术 url="http://sso.jt.com/user/register";
	 */
	@RequestMapping("/register")
	public SysResult saveUser(String userJSON) {
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail();
		}
	}

	/**
	 * 利用跨域实现用户信息回显
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket, String callback) {
		String userJSON = jedis.get(ticket);
		if (StringUtils.isEmpty(userJSON)) {
			return new JSONPObject(callback, SysResult.fail());
		} else {

			return new JSONPObject(callback, SysResult.ok(userJSON));
		}

	}

}
