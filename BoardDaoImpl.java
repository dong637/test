package com.study.project.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.project.dao.BoardDao;

@Repository("dao")
public class BoardDaoImpl implements BoardDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) { // 목록, 검색, 페이징
		return sqlSession.selectList("mapper.list", map); // 게시글 리스트는 selectList 사용. mapper.아이디 를 가져오면 됨
	}

	@Override
	public int insert(Map<String, Object> map) { // 글쓰기
		return sqlSession.insert("mapper.insertList", map);
	}

	@Override
	public Map<String, Object> read(int seq) { // 읽기
		return sqlSession.selectOne("mapper.read", seq); // 게시글 하나 선택은 selectOne 사용
	}

	@Override
	public int update(Map<String, Object> map) { // 수정
		return sqlSession.update("mapper.update", map);
	}

	/*
	 * @Override public int delete(List<Integer> list) { // 삭제 return
	 * sqlSession.delete("mapper.delete", list); }
	 */

}