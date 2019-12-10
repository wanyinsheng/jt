package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;

	// http://localhost:8091/item/query?page=1&rows=20
	/**
	 * 查询商品列表信息,分页查询
	 * 
	 * @return
	 */
	@RequestMapping("/query")
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		return itemService.findItemByPage(page, rows);
	}

}
