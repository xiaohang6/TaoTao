package com.taotao.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import com.taotao.utils.JsonUtils;

/**
 * 广告位内容管理
 * 
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;

	@Override
	public List<TbContent> getContentList(long contentCid) {
		// 从缓存中取数据(如果取成功了就不用 执行try..catch以下的代码了)
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid + "");
			if (result != null) {
				// 取值
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				// 直接返回
				return resultList;
			}
		} catch (Exception e) {
			System.out.println("==================" + e);
		}

		// 根据内容分类id查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		// 像缓存中添加内容（因为redis没有所以在这需要存一下）
		try {
			// 需要先把list转化为字符串
			String catchString = JsonUtils.objectToJson(list);
			// 存 常量+key名字+结果value
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY, contentCid + "", catchString);
		} catch (Exception e) {
			System.out.println("==================" + e);
		}
		return list;
	}

}
