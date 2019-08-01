package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EUTreeNode;
import com.taotao.service.ItemCatService;

/**
 * 商品分类管理controller
 * <p>
 * Title: ItemCatController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping("/list")
	@ResponseBody
	// 使用它的read方法 在客户端上写值
	// 如果方法返回一个对象的话，springmvc会自动把返回的对象转化为一个json字符串，ResponseBody返回一个字符串
	// 如果返回值是一个字符串，就不用处理了，ResponseBody会自动返回一个字符串
	// 比如异步获取 json 数据，加上 @ResponseBody 后，会把万物转化为 json 数据返回到前台
	// @ResponseBody告诉控制器返回!对象!会被自动序列化成JSON，并且传回HttpResponse这个对象。
	private List<EUTreeNode> getCatList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		List<EUTreeNode> list = itemCatService.getCatList(parentId);
		return list;
	}
}
