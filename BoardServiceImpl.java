package com.study.project.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.study.project.dao.BoardDao;
import com.study.project.service.BoardService;

@Service("service") // 찾고 싶은 bean 아이디를 찾으면 됨
public class BoardServiceImpl implements BoardService {

	@Resource(name = "dao")
	private BoardDao boardDao; // 인터페이스를 호출함

	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) { // 리스트, 검색, 페이징
		// 페이징처리 로직만들기
		
		return boardDao.list(map);
	}

	@Override
	public int insert(Map<String, Object> map) { // 글쓰기
		return boardDao.insert(map);
	}

	@Override
	public Map<String, Object> read(int seq) { // 읽기
		return boardDao.read(seq);
	}

	@Override
	public int update(Map<String, Object> map) { // 수정
		return boardDao.update(map);
	}
	
	/*
	 * @Override public int delete(List<Integer> list) { // 삭제 return
	 * boardDao.delete(list); }
	 */

}