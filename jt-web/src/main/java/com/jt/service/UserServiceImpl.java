package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.jt.pojo.User;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private HttpClientService httpClient;
	@Override
	public void saveUser(User user) {
		//自己定义url，实现跨域
		String url="http://sso.jt.com/user/register";
		//将密码加密
		String md5password=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5password);//将加密后的密码重新赋值给user对象
		//将参数封装到map中
		Map<String, String> params=new HashMap<String, String>();
		//将对象转为jso格式
		String userJson=ObjectMapperUtil.toString(user);
		//整个json串存入map，，，避免大量属性直接map的put操作
		//userJSON为map的key
		params.put("userJSON", userJson);
		String result = httpClient.doPost(url, params);
		//判断返回值是否正确
		SysResult sysResult = ObjectMapperUtil.toObject(result, SysResult.class);
		if (sysResult.getStatus()==201) {
			//说明后台程序出错
			throw new RuntimeException();
		}

	}
}
