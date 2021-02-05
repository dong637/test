package com.study.project.dao;

import java.util.List;
import java.util.Map;

public interface BoardDao {

	public List<Map<String, Object>> list(Map<String, Object> map); // 목록, 검색, 페이징

	public int insert(Map<String, Object> map); // 글쓰기

	public Map<String, Object> read(int seq); // 읽기

	public int update(Map<String, Object> map); // 수정

	// public int delete(List<Integer> list); // 삭제

}