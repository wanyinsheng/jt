package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	/**
	 * 根据商品id查询后台服务数据 1.在前台service中实现httpclient调用 2.后台将根据itemId查询的数据返回json串
	 * 3.将json转化为item对象 4.将item对象保存在request域中 5.页面返回
	 */
//	http://www.jt.com/items/562379.html
	@RequestMapping("/items/{itemId}")
	public String findItemById(@PathVariable Long itemId, Model model) {
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		// 传值到页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
