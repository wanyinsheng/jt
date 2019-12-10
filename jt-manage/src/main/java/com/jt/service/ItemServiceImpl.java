package com.jt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUIData;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		// 获取商品总记录数
		int total = itemMapper.selectCount(null);
		/**
		 * 2.分页之后回传数据 sql:
		 * 
		 * select * from tb_item limit 起始位置,查询行数
		 * 
		 * 第1页: 20 select * from tb_item limit 0,20
		 * 
		 * 第2页: select * from tb_item limit 20,20
		 * 
		 * 第3页: select * from tb_item limit 40,20
		 * 
		 * 第N页: select * from tb_item limit (page-1)rows,rows
		 */
		// 手写分页查询
		int start = (page - 1) * rows;// 计算起始位置
		List<Item> itemList = itemMapper.findItemByPage(start, rows);
		return new EasyUIData(total, itemList);
	}

}
