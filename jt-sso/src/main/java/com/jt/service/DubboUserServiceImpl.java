package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

/**
 * 该类是Dubbo公共接口的实现类，提供服务
 * 
 * @author wanyinsheng
 *
 */
@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper UserMapper;
	@Autowired
	private JedisCluster jedis;

	@Transactional
	@Override
	public void saveUser(User user) {
		// 将密码加密
		String md5pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		// 设置其他属性
		user.setPassword(md5pass).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(new Date());
		// 入库
		UserMapper.insert(user);
	}

	/**
	 * 登陆时获取token对象
	 * 校验用户名或面密码是否正确
	 * 如果数据不正确   返回null
	 * 数据正确  :
	 *      生成加密秘钥：JT_TICKET+username+当前毫秒数
	 *      将userDB数据转为json
	 *      将数据保存到redis中7天超时
	 * 
	 */
	@Override
	public String findUserByUP(User user) {
		String token=null;
		//将密码加密
		String md5pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5pass);
		QueryWrapper<User> wrapper=new QueryWrapper<User>(user);//这里直接将对象传进去，就不用使用eq方法
		User userDB=UserMapper.selectOne(wrapper);
		if (user!=null) {
			token="JT_TOKEN"+userDB.getUsername()+System.currentTimeMillis();
			token=DigestUtils.md5DigestAsHex(token.getBytes());
			//脱敏处理，，，不保存密码
			userDB.setPassword(null);
			String userJSON=ObjectMapperUtil.toString(userDB);
			jedis.setex(token, 7*24*3600, userJSON);
		}
		return token;
	}

}
