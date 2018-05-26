package com.hzhanghuan.hhCommonBase.dao;

import com.hzhanghuan.hhCommonBase.entity.PageBean;

import java.util.HashMap;
import java.util.List;



public interface BaseDao<T> {
	int save(T duixiang); //增加
	int delete(Long id); //删除
	int update(T object); //更新
	List<T> findAll(); //查询所有
	T getById(Long id); //按ID
	List<T> getByIds(Long[] ids); //多个ID
	int count(HashMap<Object,Object> hashMap); //总记录数
	PageBean<T> getPageBean(HashMap<Object,Object> hashMap); //分页
	List<T> getByCondition(HashMap<Object,Object> hashMap); //new
}
