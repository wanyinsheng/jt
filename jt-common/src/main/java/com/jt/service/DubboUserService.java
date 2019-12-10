package com.jt.service;

import com.jt.pojo.User;

/**
 * Dubbo中立的接口
 * 
 * @author wan_ys
 *
 */
public interface DubboUserService {

	void saveUser(User user);

	String findUserByUP(User user);

}