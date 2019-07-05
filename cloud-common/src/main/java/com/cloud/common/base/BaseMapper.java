package com.cloud.common.base;

/**
 * @program: cloud_example
 * @description: 基础Mapper
 * @author: yangchenglong
 * @create: 2019-05-31 16:02
 */
public interface BaseMapper<T> {
	/**
	 * 新增
	 * @param t
	 * @return
	 */
	int insert(T t);

	/**
	 * 选择性新增
	 * @param t
	 * @return
	 */
	int insertSelective(T t);

	/**
	 * 按主键物理删除
	 * @param key
	 * @return
	 */
	int deleteByPrimaryKey(Object key);

	/**
	 * 选择性修改
	 * @param t
	 * @return
	 */
	int updateByPrimaryKeySelective(T t);

	/**
	 * 修改
	 * @param t
	 * @return
	 */
	int updateByPrimaryKey(T t);

	/**
	 * 按主键查询
	 * @param key
	 * @return
	 */
	T selectByPrimaryKey(Object key);
	
}
