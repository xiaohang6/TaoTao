package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;
import com.taotao.utils.JsonUtils;

/**
 * 上传图片处理
 * <p>
 * Title: PictureController
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
public class PictureController {

	@Autowired
	private PictureService pictureService;

	@RequestMapping("/pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile) {
		Map result = pictureService.uploadPicture(uploadFile);
		// 为了保证功能的兼容性（任何浏览器都可以接收），需要把Result转换成json格式的字符串。
		String json = JsonUtils.objectToJson(result);
		// 把图片路径给前台，通过src来访问互联网路径<img src=""></img>
		return json;
	}
}
