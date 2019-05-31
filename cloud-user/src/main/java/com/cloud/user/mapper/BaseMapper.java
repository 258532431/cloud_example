package com.cloud.user.mapper;

import java.util.List;
import java.util.Map;

/**
 * 基础Mapper
 * */
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
	 * 按主键删除
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

	/**
	 * 查询列表
	 * */
	List<T> page(Map map);

	/**
	 * 查询列表分页总数
	 * @param map
	 * @return
	 */
    long pageCount(Map map);
	
}
