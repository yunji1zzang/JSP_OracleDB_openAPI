<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주소 등록</title>
<script>
<%-- 주소검색 팝업 호출 --%>
function fn_openAddressPopup(){
	var url = "searchAddress.do";
	var name = "AddressPopup";
	var option = "width=600, height=300, top=100, left=200, location=no"
	window.open(url, name, option);
}
<%-- 주소검색 팝업 호출 CallBack --%>
function callback_openAddressPopup(aParam){
	document.getElementById("basic_address").value = aParam["roadAddr"];
}
</script>
</head>
<body>
<h4>[주소 등록]</h4>
<form action="insert.do" method="POST">
	기본 주소:<br>
	<input style="width:40%" type="text" id="basic_address" name="basic_address" class="form-control" 
	onkeypress="javascript:enterSearch();" />
	<input type="button" onclick="javascript:fn_openAddressPopup();" value="검색"/>
	<br><br>
	상세 주소:<br>
	<input style="width:40%" name ="detail_address" type="text"/><br><br>
	<input type="submit" value="주소 등록">
	<a href="/openapi"><input type="button" value="취소"></a>
</form>
</body>
</html>