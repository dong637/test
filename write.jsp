<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Write</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
</head>

<script>
	$(function(){
		$("#insertBtn").click(function(){
			$("#insert").attr("action", "insert").attr("method", "post").attr("enctype", "multipart/form-data").submit(); //attr로 속성 부여 후 submit
		})
		
		$("#updateBtn").click(function(){
			$("#insert").attr("action", "update").attr("method", "post").attr("enctype", "multipart/form-data").submit();
		})
	});
	
	function fncChange(upFile) { //이미지 파일 첨부
		var ext = $(upFile).val().split(".").pop().toLowerCase();
		
		 if($.inArray(ext, ['gif','png','jpg','jpeg','bmp']) == -1) {
			 alert('이미지 파일만 업로드 할 수 있습니다.');
			 $(upFile).val(""); //""넣어줘야 파일이 첨부가 되지 않음. 잘못된 파일 올라가면 삭제
			 return;
		 }
		 
		 var file = upFile.files[0]; //배열이라 0번째로 뺀 것
		 var _URL = window.URL || window.webkitURL; //윈도우는 창을 이야기함. 
		 var img = new Image();
		 
		 img.src = _URL.createObjectURL(file);
		 img.onload = function(){
			 if(img.width > 500 || img.height > 500) {
				 alert("이미지 크기를 확인하세요.");
				 $(upFile).val(""); // ""넣어줘야 파일이 첨부가 되지 않음.
				 return;
			 }
		 }
	};
</script>

<body>
	<h2>게시글 수정/삭제</h2>
	<form name="writeView" id="insert"> <!-- 스크립트에 따라 get post 변경되도록 함 -->
		<table border="1">
			<tr>
				<td align="center" style="width: 60px;">아이디</td>
				<td><input type="text" name="memId" id="memId" value="${map.memId }"></td>
			</tr>
			<tr>
				<td align="center">작성자</td>
				<td><input type="text" name="memName" id="memName" value="${map.memName }"></td>
			</tr>
			<tr>
				<td align="center">제목</td>
				<td><input type="text" name="boardSubject" id="boardSubject" value="${map.boardSubject }" ></td>
			</tr>
			<tr>
				<td align="center">내용</td>
				<td><textarea cols="50" rows="10" name="boardContent" id="boardContent">${map.boardContent }</textarea></td>
			</tr>
			<tr>
				<td  rowspan="2" align="center">첨부</td>
				<td>
					<!-- 파일같은 경우 네임값도 다르게 주는게 좋음. 배열이기 대문에 -->
					<input type="file" name="file1" id="file1" onchange="fncChange(this)"  title="이미지 파일"/> <!-- onChange : 변화가 있을 때 -->
				</td>
			</tr>
			<tr>
				<td><input type="file" name="file2" id="file2" multiple /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<!-- 버튼 변경을 위해 c:if잘 활용할 것 -->
					<c:if test="${empty map }"> <!-- map이 있으면 등록버튼으로. -->
						<input type="button" name="insert" id="insertBtn" value="등록" >
						<input type="button" name="boardList" id="listBtn" value="목록" onClick="location.href='list'"><br>
					</c:if>
					<c:if test="${not empty map }"> <!-- map이 있으면 수정버튼으로. -->
						<input type = "hidden" name = "seq" id = "seq" value = "${map.seq }"> <!-- DB로 map 데이터를 보낼 땐 seq가 기준이 되어야 하므로, 꼭 해당 값도 같이 넣을 것. -->
						<input type="button" name="update" id="updateBtn" value="수정" >
						<input type="button" name="boardList" id="listBtn" value="목록" onClick="location.href='list'"><br>
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>