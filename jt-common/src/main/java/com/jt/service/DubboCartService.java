package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

/**
 * 购物车的公共接口
 * 
 * @author wanyinsheng
 *
 */
public interface DubboCartService {

	List<Cart> findCartListByUserId(Long userId);

	void updateCartNum(Cart cart);

	void deleteCart(Cart cart);

	void insertCart(Cart cart);

}
