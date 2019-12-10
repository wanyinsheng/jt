package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClient;

	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById";// 与manage的controller的url一致
		// 为了满足get方式，定义id=....
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", itemId + "");
		String result = httpClient.doGet(url, params);
		// 将请求获得的json串转为对应的对象
		return ObjectMapperUtil.toObject(result, Item.class);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById";
		// 为了满足get方式，定义id=....
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", itemId + "");
		String result = httpClient.doGet(url, params);
		// 将请求获得的json串转为对应的对象
		return ObjectMapperUtil.toObject(result, ItemDesc.class);
	}

}
