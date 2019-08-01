package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 通过请求路径用来打开网页！
 */
@Controller
public class PageController {
	/**
	 * 打开首页
	 * 
	 * @author 12742
	 *
	 */
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	/**
	 * 打开其他页面
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
