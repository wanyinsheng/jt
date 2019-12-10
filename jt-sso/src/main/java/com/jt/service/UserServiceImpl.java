package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean checkUser(String param, Integer type) {
		String column = type == 1 ? "username" : (type == 2 ? "phone" : "email");
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq(column, param);// 这里不用封装，只计数
		int count = userMapper.selectCount(wrapper);
		return count == 0 ? false : true;
	}

	/**
	 * 注册用户，实现入库操作，，添加事务机制
	 */
	@Transactional
	@Override
	public void saveUser(User user) {
		user.setEmail(user.getPhone()).setCreated(new Date()).setUpdated(new Date());// 设置user的其他属性
		userMapper.insert(user);

	}

}
