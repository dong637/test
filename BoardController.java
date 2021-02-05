package com.study.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.study.project.service.BoardService;

@Controller
public class BoardController {

	@Resource(name = "service")
	private BoardService boardService;

	@Inject
	public SqlSessionTemplate sqlSession;

	// 글목록, 검색, 페이징
	@RequestMapping("list") // 안에 list는 url
	public String list(@RequestParam Map<String, Object> map, Model model) {
		// Map으로 데이터 갖고 왔으면 Model로 넘겨줘서 화면 구현
		// map으로 하면 input의 name값이 변수가 됨.
		// @RequestParam을 추가해서 검색 기능 추가

		if (map.isEmpty()) { // 페이징의 초기값을 넣어줌
			map.put("pageNo", 1);
			map.put("listSize", 10);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // map은 한 건, list는 여러건
		list = boardService.list(map); // 나중을 위해서 맵도 달아놓음. 데이터 갖고온거

		int count = Integer.parseInt(list.get(0).get("TOTAL").toString());

		Map<String, Object> pageMap = paging(map, count); // 아래에서 paging 정리

		model.addAttribute("list", list); // 안에 왼쪽 list는 데이터를 넘겨주기 위한 임의의 키값
		model.addAttribute("pageMap", pageMap);

		return "list"; // list.jsp
	}

	public Map<String, Object> paging(Map<String, Object> map, int count) { // 페이징
		
		int PAGE_SCALE = Integer.parseInt(map.get("listSize").toString());
		int curPage = Integer.parseInt(map.get("pageNo").toString());
		int BLOCK_SCALE = 5;
		int totPage = (int) Math.ceil(count * 1.0 / PAGE_SCALE);
		int totBlock = (int) Math.ceil(totPage / BLOCK_SCALE);

		// *현재 페이지가 몇번째 페이지 블록에 속하는지 계산
		int curBlock = (int) Math.ceil((curPage - 1) / BLOCK_SCALE) + 1;
		// *현재 페이지 블록의 시작, 끝 번호 계산
		int blockBegin = (curBlock - 1) * BLOCK_SCALE + 1;
		// 페이지 블록의 끝번호
		int blockEnd = blockBegin + BLOCK_SCALE - 1;
		// *마지막 블록이 범위를 초과하지 않도록 계산
		if (blockEnd > totPage) {	blockEnd = totPage;}
		// *이전을 눌렀을 때 이동할 페이지 번호
		int prevPage = (curPage == 1) ? 1 : (curBlock - 1) * BLOCK_SCALE;
		// *다음을 눌렀을 때 이동할 페이지 번호
		int nextPage = curBlock > totBlock ? (curBlock * BLOCK_SCALE) : (curBlock * BLOCK_SCALE) + 1;
		// 마지막 페이지가 범위를 초과하지 않도록 처리
		if (nextPage >= totPage) {nextPage = totPage;}

		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("PAGE_SCALE", PAGE_SCALE);
		pageMap.put("curPage", curPage);
		pageMap.put("BLOCK_SCALE", BLOCK_SCALE);
		pageMap.put("totPage", totPage);
		pageMap.put("totBlock", totBlock);
		pageMap.put("curBlock", curBlock);
		pageMap.put("blockBegin", blockBegin);
		pageMap.put("blockEnd", blockEnd);
		pageMap.put("prevPage", prevPage);
		pageMap.put("nextPage", nextPage);

		return pageMap;
	}

	// 글 쓰기 화면으로
	@RequestMapping("writeView")
	public String write() {
		return "write";
	}

	//경로명 
	private static final String uploadPath = "C:/Users/hdong/Desktop/test/";
	
	// 글 쓰고, 파일 업로드하고 리스트로
	@RequestMapping("insert")
	public String insert(@RequestParam Map<String, Object> map, MultipartHttpServletRequest mRequest) throws IllegalStateException, IOException { //MultipartHttpServletRequest : 파일 + 텍스트
		// @RequestParam Map<String, Object> map 또는 @ModelAttribute BoardVO vo
		// HttpServletRequest request로 쓰고 하단에 request 요소들 받고 보내기 쓸 수 있으나 길어짐
		Iterator<String> itr = mRequest.getFileNames(); //파일명을 추출해서 iterator로 담고
		while(itr.hasNext()) { //while로 input을 돌아다니면서 itr 다음꺼로 계속 감. 거짓이 될 때까지 돔. hasNext()는 다음것이 있으면 참, 없으면 거짓.
			// UUID uuid = UUID.randomUUID(); 파일명을 위해 사용. 임의의 숫자
			// int rand = (int)(Math.random()*1000); 임의의 정수.
			// 파일 하나씩 빼서 이름 바꾸는 과정
			MultipartFile mFile = mRequest.getFile(itr.next()); //itr을 받아서 mFile에 넣고 while의 힘을 얻어 다음거로.
			String originName = mFile.getOriginalFilename(); //파일 원래이름은 fileName에 넣고
			String savedName = System.currentTimeMillis() + "_" + originName; // 파일명 다 다르게 해줘야 함. 날짜 대신 uuid를 쓸 수도 있긴 함.
			
			mFile.transferTo(new File(uploadPath + savedName)); //업로드할 당시의 파일명이 담긴 mFile은 uploadPath + savedName으로 변경 
		}
		int insert = boardService.insert(map); // 글이 잘 올라갔는지 확인. 0이면 잘 안 올라간 것. 1이면 잘 올라간 것. int로 해줘야 함.
		return "redirect:list";
	}
	
	// 상세보기로 + 상세보기에서 업로드한 파일 확인하는 방법
	@RequestMapping("read") // url
	public String read(@RequestParam int seq, Model model) { // 상세보기에 map으로 불러온 값들을 넣어줘 보여줘야하기 때문에 model이 들어가야 함
		Map<String, Object> map = boardService.read(seq);
		model.addAttribute("map", map); // map으로 불러온 값들을 넣어주기 때문에 map
		return "write";
	}

	// 상세보기에서 글, 파일 새로 업로드하고 리스트로
	@RequestMapping("update")
	public String update(@RequestParam Map<String, Object> map, MultipartHttpServletRequest mRequest) throws IllegalStateException, IOException {
		// @RequestParam Map<String, Object> map 또는 @ModelAttribute BoardVO vo
		// HttpServletRequest request로 쓰고 하단에 request 요소들 받고 보내기 쓸 수 있으나 길어짐
		Iterator<String> itr = mRequest.getFileNames(); //파일명을 추출해서 iterator로 담고
		while(itr.hasNext()) { //while로 input을 돌아다니면서 itr 다음꺼로 계속 감. 거짓이 될 때까지 돔. hasNext()는 다음것이 있으면 참, 없으면 거짓.
			// UUID uuid = UUID.randomUUID(); 파일명을 위해 사용. 임의의 숫자
			// int rand = (int)(Math.random()*1000); 임의의 정수.
			// 파일 하나씩 빼서 이름 바꾸는 과정
			MultipartFile mFile = mRequest.getFile(itr.next()); //itr을 받아서 mFile에 넣고 while의 힘을 얻어 다음거로.
			String originName = mFile.getOriginalFilename(); //파일 원래이름은 fileName에 넣고
			String savedName = System.currentTimeMillis() + "_" + originName; // 파일명 다 다르게 해줘야 함. 날짜 대신 uuid를 쓸 수도 있긴 함.
			
			mFile.transferTo(new File(uploadPath + savedName)); //업로드할 당시의 파일명이 담긴 mFile은 uploadPath + savedName으로 변경 
		}		
		int update = boardService.update(map);
		return "redirect:list";
	}

	// 글 삭제하기
	@RequestMapping("delete")
	public String delete(Integer[] chk) { // @RequestParam은 map에서는 필수! 키값이 자동 생성되어야 하기 때문
		List<Integer> list = Arrays.asList(chk);
		int delete = sqlSession.delete("mapper.delete", list);
		return "redirect:list";
	}

	/* @RequestMapping("delete") // 글 삭제하기
	  	public String delete(List<Integer> list) { 
	 	int delete = boardService.delete(list);
		return "redirect:list"; } */
	// int seq 대신 Integer[] seq 배열로 받거나 List<integer> list 리스트로 받아서 실행
	// mybatis에서는 List<integer> list가 좋음

	
    // 업로드 버튼클릭 -> 임시디렉토리에 업로드 -> 지정된 디렉토리에 저장 -> 파일정보가 file에 저장
	/*@RequestMapping("uploadForm")// 업로드 폼으로
	public String uploadForm() {
		return "uploadForm";
	}*/
	
	/*private static final String uploadPath = "C:/Users/hdong/Desktop/test/";//경로명
	@RequestMapping("fileUpload")// 업로드 클릭하면 실제로 저장
	public String fileUpload(MultipartHttpServletRequest mRequest) throws IllegalStateException, IOException { // MultipartFile는 파일만. 이건 텍스트도
		// FileMap은 FileName는 input 단일로 여러개를 받을 때, Files는 다중으로 할 때
		
		// 단일과 다중을 같이 쓰는 경우는 별로 없음. 둘 중 하나만 선택해서 사용하는게 좋음. 여기서는 단일을 사용하며 다중은 주석처리
		/* List<MultipartFile> mFileList = mRequest.getFiles("file1"); // Files는 다중파일. file1에만 multiple 넣음
			for(int i = 0; i < mFileList.size(); i++) { // 다중에 넣은 파일들을 하나씩 하나씩 뺌.
				MultipartFile mFile = mFileList.get(i);
				String originName = mFile.getOriginalFilename(); //파일 원래이름은 fileNamedp 넣고
				String saveName = System.currentTimeMillis() + "_" + originName; // 파일명 다 다르게 해줘야 함. 날짜 대신 uuid를 쓸 수도 있긴 함.
				mFile.transferTo(new File(uploadPath + saveName)); //업로드할 당시의 파일명이 담긴 mFile은 uploadPath + saveName으로 변경
				
				mFile.transferTo(new File(uploadPath + saveName)); //업로드할 당시의 파일명이 담긴 mFile은 uploadPath + saveName으로 변경 
			}
			return "uploadForm";
		} 여기까지 다중*/
		
		/* Iterator<String> itr = mRequest.getFileNames(); */ //파일명을 추출해서 iterator로 담고
		
		/* while(itr.hasNext()) { //while로 input을 돌아다니면서 itr 다음꺼로 계속 감. 거짓이 될 때까지 돔. hasNext()는 다음것이 있으면 참, 없으면 거짓.
			// UUID uuid = UUID.randomUUID(); 파일명을 위해 사용. 임의의 숫자
			// int rand = (int)(Math.random()*1000); 임의의 정수.
			// 파일 하나씩 빼서 이름 바꾸는 과정
			MultipartFile mFile = mRequest.getFile(itr.next()); //itr을 받아서 mFile에 넣고 while의 힘을 얻어 다음거로.
			String originName = mFile.getOriginalFilename(); //파일 원래이름은 fileName에 넣고
			String savedName = System.currentTimeMillis() + "_" + originName; // 파일명 다 다르게 해줘야 함. 날짜 대신 uuid를 쓸 수도 있긴 함.
			
			mFile.transferTo(new File(uploadPath + savedName)); //업로드할 당시의 파일명이 담긴 mFile은 uploadPath + savedName으로 변경 
		}
		return "uploadForm";
	} */
}