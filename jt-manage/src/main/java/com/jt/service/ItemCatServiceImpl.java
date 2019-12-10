package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper itemCatMapper;

	@Override
	public String findItemCatNameById(Long itemCatId) {
		return itemCatMapper.selectById(itemCatId).getName();
	}

	/**
	 * 1.根据parentId查询数据库记录返回itemCatList集合
	 * 
	 * 2.将itemCatList集合中的数据按照指定的格式封装为 List<EasyUITree>
	 */
	@Override
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		List<ItemCat> cartList = findItemCatList(parentId);
		List<EasyUITree> treeList=new ArrayList<>();
		//遍历集合数据,实现数据的转换
		for (ItemCat itemCat : cartList) {
			EasyUITree easyUITree=new EasyUITree();
			easyUITree.setId(itemCat.getId());
			easyUITree.setText(itemCat.getName());
			//如果是父级则closed 不是则open
			String state=itemCat.getIsParent()?"closed":"open";
			easyUITree.setState(state);
			treeList.add(easyUITree);
		}
		
		return treeList;
	}

	/**
	 * 查询出分类列表数据
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}

}
