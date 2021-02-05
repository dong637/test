<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
</head>

<script>
	$(function() {
		$("#searchBtn").click(function() {
			$("#searchForm").attr("action", "list").attr("method",	"get").submit();

/* 				$.ajax({
					type: "POST",
					url: "list",
					dataType: 'html',
					data: $("#searchForm").serialize(),
					success: function(data){
						alert("검색성공");
					}
				}); */
		})

		$("#delBtn").click(function() {
			$("#delForm").attr("action", "delete").attr("method",	"post").submit();
		})
				
		$("#dateFrom").datepicker({
			changeMonth : true,
			changeYear : true,
			dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
			monthNamesShort : [ '1월', '2월', '3월', '4월', '5월', '6월',
										'7월', '8월', '9월', '10월', '11월', '12월' ],
			dateFormat : "yy-mm-dd"
		})

		$("#dateTo").datepicker(	{
			changeMonth : true,
			changeYear : true,
			dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
			monthNamesShort : [ '1월', '2월', '3월', '4월', '5월', '6월',
										'7월', '8월', '9월', '10월', '11월', '12월' ],
			dateFormat : "yy-mm-dd"
		})
	});
	
	function list(pageNo){
		$("#pageNo").val(pageNo);
		$("#searchBtn").click();
	}
</script>

<body>
	<form name="searchForm" id="searchForm">
		<input type="hidden" name="pageNo" id="pageNo" value="1">
		<input type="hidden" name="listSize" id="listSize" value="10">
		<div style="line-height: 25px;">
			<select name="searchOption">
				<option>선택</option>
				<%-- <option value="name" <c:out value="${list.searchOption == 'name'?'selected':''}"/>>작성자</option>
						검색 결과로 입력창에 값 나오게 하는것 추후 시도 --%>
				<option value="name">작성자</option>
				<option value="subject">제목</option>
				<option value="content">제목+내용</option>
			</select>
			<input type="text" name="search" id="search">
			<input type="button" name="searchBtn" id="searchBtn" value="검색"><br>
			<!-- <input type="text" autocomplete="off"> autocomplete="off"는 자동완성 기능 끄기-->
			작성일 : 
			<input type="text" name="dateFrom" id="dateFrom" autocomplete="off">~<input type="text" name="dateTo" id="dateTo" autocomplete="off"><br>
			<input type="button" name="homeBtn" id="homeBtn" value="HOME" onClick="location.href='list'">&nbsp;
			<input type="button" name="insertBtn" id="insertBtn" value="글쓰기" onClick="location.href='writeView'">&nbsp;
			<input type="button" name="delBtn" id="delBtn" value="삭제" onClick=""><br>
		</div>
	</form>

	<form name="delForm" id="delForm">
		<table border="1">
			<tr>
				<th><input type="checkbox" name="chkAll" id="chkAll"></th>
				<th>글번호</th>
				<th>작성자(ID)</th>
				<th>제목</th>
				<th>작성일</th>
				<th>수정일</th>
				<th>조회수</th>
			</tr>

			<c:forEach items="${list }" var="list" varStatus="num">
				<%-- ${list}는 controller의 model.addAttribute("list", list);에서 왼쪽 list --%>
				<tr>
					<td align=center><input type="checkbox" name="chk" id="chk"
						value="${list.seq}"></td>
					<td align=center>${list.seq }</td>
					<td>${list.memName }(${list.memId })</td>
					<td><a href="/read?seq=${list.seq }">${list.boardSubject }</a></td>
					<td>${list.regDate }</td>
					<td>${list.uptDate }</td>
					<td align=center>${list.viewCnt }</td>
					<!-- 컨트롤러에서 model.addAttribute("list", list);에 list를 썼으므로, 표현어에 list. 으로 넣어줌 -->
				</tr>
			</c:forEach>

			<tr align="center";>
				<td colspan="7">
					<!-- 현재 페이지가 1보다 크면  [처음][이전]을 화면에 출력-->
					<c:if test="${pageMap.curBlock > 1}">
						<a href="javascript:list('1')"><input type="button" value="처음"/></a>
						<a href="javascript:list('${pageMap.prevPage}')"><input type="button" value="이전"/></a>
					</c:if>
					
					<!-- **하나의 블럭에서 반복문 수행 시작페이지부터 끝페이지까지 -->
					<c:forEach var="num" begin="${pageMap.blockBegin}" end="${pageMap.blockEnd}">
						<!-- **현재페이지이면 하이퍼링크 제거 -->
						<c:choose>
							<c:when test="${num == pageMap.curPage}">
								<span style="color: red">${num}</span>&nbsp;
	         			   </c:when>
							<c:otherwise>
								<a href="javascript:list('${num}')" style="text-decoration: none;">${num}</a>&nbsp;
	            			</c:otherwise>
						</c:choose>
					</c:forEach>
					
					<!-- 현재 페이지 블럭이 전체 페이지 블럭보다 작거나 같으면 [다음][끝]을 화면에 출력 -->
					<c:if test="${pageMap.curBlock <= pageMap.totBlock}">
						<a href="javascript:list('${pageMap.nextPage}')"><input type="button" value="다음"/></a>
						<a href="javascript:list('${pageMap.totPage}')"><input type="button" value="끝"/></a>
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
