<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadForm</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
</head>

<script>	
	function fncChange(upFile) {
			var ext = $(upFile).val().split(".").pop().toLowerCase();
			
			 if($.inArray(ext, ['gif','png','jpg','jpeg','bmp']) == -1) {
				 alert('gif,png,jpg,jpeg,bmp 파일만 업로드 할 수 있습니다.');
				 $(upFile).val(""); //""넣어줘야 파일이 첨부가 되지 않음.
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
	<form name="uploadForm" id="uploadForm" action="fileUpload" method="post" enctype="multipart/form-data">
		<!-- 파일같은 경우 네임값도 다르게 주는게 좋음. 배열이기 대문에 -->
		<input type="file" name="file1" id="file1" onchange="fncChange(this)" multiple /><br> <!-- onChange : 변화가 있을 때 -->
		<input type="file" name="file2" id="file2" multiple /><br>
		<input type="file" name="file3" id="file3" multiple /><br>
		<input type="file" name="file4" id="file4" multiple /><br>
		<input type="submit" name="submit" id="submit" value="업로드"> <!-- //form 안에 있어야 함. -->
	</form>
</body>
</html>
