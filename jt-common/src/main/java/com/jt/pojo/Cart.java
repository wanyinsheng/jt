package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_cart")
public class Cart extends BasePojo {
	private Long id;
	private Long userId;
	private Long itemId;
	private String itemTitle;
	private String itemImage; // 保存的是第一张图片信息，购物车的图只展示了商品的第一张图
	private Long itemPrice;
	private Integer num;

}
