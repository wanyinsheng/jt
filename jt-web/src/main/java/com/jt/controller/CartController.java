package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout = 3000, check = false)
	private DubboCartService cartService;

	/**
	 * 点击购物车跳转cart.jsp
	 * 
	 * 实现商品列表信息展现 页面取值 ${cartList} http://www.jt.com/cart/show.html
	 */
	@RequestMapping("/show")
	public String findCartList(Model model) {
		Long userId = 7L;
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	/**
	 * 实现购物车数量的修改
	 * 
	 * http://www.jt.com/cart/update/num/562379/9
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {// 如果URL中使用restful获取数据时，接收的是对象，并且属性匹配，可以直接使用对象传参
		try {
			Long userId = 7L;
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail();
		}

	}

	/**
	 * 实现购物车删除操作
	 * 
	 * http://www.jt.com/cart/delete/562379.html
	 * 
	 * 请求成功之后需要重定向到购物车展示页面
	 */
	@RequestMapping("delete/{itemId}")
	public String deletCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show";// 或者redirect：/cart/show.html 跳转的是展现购物车页面的骑请求url
	}

	/**
	 * 新增购物车
	 * 
	 * url: http://www.jt.com/cart/add/1474391972.html
	 * 
	 */
	public String insertCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.insertCart(cart);
		// 重定向到购物车页面
		return "redirect:/cart/show";
	}

}
