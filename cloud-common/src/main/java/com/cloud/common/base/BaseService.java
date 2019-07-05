package com.cloud.common.base;

/**
 * @program: cloud_example
 * @description: 基础服务类
 * @author: yangchenglong
 * @create: 2019-05-31 16:02
 */
public interface BaseService<T> {

	int insert(T t);

	int insertSelective(T t);

	int deleteByPrimaryKey(Object key);

	int updateByPrimaryKeySelective(T t);

	int updateByPrimaryKey(T t);

	T selectByPrimaryKey(Object key);
	
}