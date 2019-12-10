package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.pojo.User;

import javassist.expr.NewArray;

/**
 * Dubbo公共接口的实现类，，提供服务
 * 
 * @author wanyinsheng
 *
 */
@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * 更新的信息。。num/updated 判断条件 where user_id， item_id
	 * 
	 */
	@Transactional
	@Override
	public void updateCartNum(Cart cart) {
		// 通过创建一个新的对象，，来修改数据
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		cartMapper.update(tempCart, updateWrapper);

	}

	/**
	 * 将对象中不为null的属性值当做where条件，
	 * 
	 * 前提：保证cart中只能有两个属性不为null
	 */
	@Transactional
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);

	}

	/**
	 * 用户第一次新增，，入库
	 * 
	 * 不是第一次入库，只需要修改数量
	 */
	@Transactional
	@Override
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();

		queryWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if (cartDB == null) {
			// 第一次加入购物车
			cart.setCreated(new Date()).setUpdated(new Date());// 补充其他属性信息
			cartMapper.insert(cart);// 入库

		} else {
			// 表示用户多次加入购物车，只做数量上的修改
			int num = cart.getNum() + cartDB.getNum();// 总的数量
			cartDB.setNum(num).setUpdated(new Date());
			cartMapper.updateById(cartDB);// 修改操作时，，除了主键以外的其他都要更新
		}
	}

}
