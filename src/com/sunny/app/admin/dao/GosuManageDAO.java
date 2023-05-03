package com.sunny.app.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.mybatis.config.MyBatisConfig;
import com.sunny.app.admin.vo.GosuManageVO;

public class GosuManageDAO {
	
	public SqlSession sqlSession;
	
	public GosuManageDAO() {
		sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true);
	}
	
	public List<GosuManageVO> gosuInfo(Map<String, Object> pageMap){
		System.out.println("gosuInfo메서드");
		return sqlSession.selectList("admin.gosuInfo", pageMap);
	}
	
	public int getTotal() {
		return sqlSession.selectOne("admin.getTotal2");
	}
}
