package com.hzhanghuan.hhCommonBase.dao;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.hzhanghuan.hhCommonBase.dao.BaseDao;
import com.hzhanghuan.hhCommonBase.entity.PageBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		// 得到T的真实类型
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass(); // 获取当前new的对象的 泛型的父类 类型
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0]; // 获取第一个类型参数的真实类型
		System.out.println("clazz --->1" + clazz);
	}

	/**
	 * insert新增
	 */
	@Override
	public int save(T object) {
		String insert="insert"+clazz.getSimpleName();
		System.out.println("增加");
		System.out.println(clazz.getSimpleName());
		return sqlSessionTemplate.insert(insert, object);
	}
	
	/**
	 * delete删除
	 */
	@Override
	public int delete(Long id) {
		String delete="delete"+clazz.getSimpleName()+"ById";
		System.out.println(delete);
		return sqlSessionTemplate.delete(delete, id);
	}
	/**
	 * 更新
	 */
	@Override
	public int update(T duixiang) {
		String update="update"+clazz.getSimpleName()+"ById";
		System.out.println("更新"+update);
		return sqlSessionTemplate.update(update, duixiang);
	}

	@Override
	public List<T> findAll() {
		String findAll="find"+clazz.getSimpleName()+"All";
		return sqlSessionTemplate.selectList(findAll);
	}

	/**
	 * 查看一条数据(对象)
	 */
	@Override
	public T getById(Long id) {
		String getById="find"+clazz.getSimpleName()+"ById";//findSysUserById return method
		System.out.println(clazz.getSimpleName()+"按ID查");
		return sqlSessionTemplate.selectOne(getById, id);
	}
	
	/**
	 * 多条查询
	 */
	@Override
	public List<T> getByIds(Long[] ids) {
		String getByIds="find"+clazz.getSimpleName()+"ByIds";
		return sqlSessionTemplate.selectList(getByIds, ids);
	}

	/**
	 * 分页查询list总条目数
	 */
	@Override
	public int count(HashMap<Object,Object> hashMap) {
		String count = "count"+clazz.getSimpleName()+"sByConditions";
		System.out.println(count);
		return sqlSessionTemplate.selectOne(count, hashMap);
	}

	/**
	 * 分页
	 */
	@Override
	public PageBean<T> getPageBeanOld(HashMap<Object, Object> hashMap) {
		System.out.println("分页");
		String getPageBean = "select"+clazz.getSimpleName()+"sByConditions";
		System.out.println(getPageBean);
		
		// TODO: 命名需要变更
		int pageNum = Integer.parseInt(hashMap.get("pageNum").toString());
		int pageSize = Integer.parseInt(hashMap.get("pageSize").toString());
		hashMap.put("pageNum", (pageNum-1)*pageSize);
		hashMap.put("pageSize",((pageNum-1)*pageSize)+pageSize);
		
		int count = count(hashMap);
		List<T> list = sqlSessionTemplate.selectList(getPageBean,hashMap);
		PageBean<T> pageBean = new PageBean<T>(pageNum, pageSize, count, list);
		return pageBean;
	}
	@Override
	public List<T> getByCondition(HashMap<Object,Object> hashMap){
		String getPageBean = "get"+clazz.getSimpleName()+"sByConditions";
		return sqlSessionTemplate.selectList(getPageBean,hashMap);
	}
	/**
	 * 正确分页:currentPage pageSize
	 */
	@Override
	public PageBean<T> getPageBean(HashMap<Object, Object> hashMap) {
		System.out.println("分页");
		String getPageBean = "get"+clazz.getSimpleName()+"sByConditions";
		System.out.println(getPageBean);
		int currentPage=1;
		int pageSize=1;
		if(hashMap.get("currentPage")!=null)
			 currentPage = Integer.parseInt(hashMap.get("currentPage").toString());
		if(hashMap.get("pageSize")!=null)
			pageSize = Integer.parseInt(hashMap.get("pageSize").toString());
		
		// 查询所用参数：start,pageSize
		if(hashMap.get("currentPage")!=null&&hashMap.get("pageSize")!=null) {
			hashMap.put("start", (currentPage - 1) * pageSize);
			hashMap.put("end",(currentPage) * pageSize);
		}
		int count = count(hashMap);
		List<T> list = sqlSessionTemplate.selectList(getPageBean,hashMap);
		PageBean<T> pageBean = new PageBean<T>(currentPage, pageSize, count, list);
 		return pageBean;
	}


}
