<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="static/js/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="static/css/style.css">
		<form id="searchForm" name="searchForm" method="post" 
			  class="navbar-form navbar-left" 
			  role="search" onsubmit="event.preventDefault();">
				<input type="hidden" name="currentPage" value="1"/> <!-- 요청 변수 설정 (현재 페이지. currentPage : n > 0) -->
				<input type="hidden" name="countPerPage" value="100"/> <!-- 요청 변수 설정 (페이지당 출력 개수. countPerPage 범위 : 0 < n <= 100) -->
				<input type="hidden" name="resultType" value="json"/> <!-- 요청 변수 설정 (검색결과형식 설정, json) --> 
				<input type="hidden" id="confmKey" name="confmKey" value="devU01TX0FVVEgyMDIxMTAwMTE1MzIxMzExMTcxMTQ="/> <!-- 요청 변수 설정 (승인키) -->
				<input style="width:50%" type="text" id="keyword" name="keyword"  placeholder="도로명+건물번호, 건물명, 지번을 입력하세요." onkeypress="javascript:enterSearch();" /><!-- 요청 변수 설정 (키워드) -->
				<input type="button"  onclick="getAddr();" value="검색"/>
		</form>
	<div>
		<table style="border:1px solid black; width:100%;">
			<thead>
				<tr>
					<th style="width:85%">주소</th>
					<th style="width:15%">우편번호</th>
				</tr>
			</thead>
			<tbody id="addressTableTbody">
			</tbody>
		</table>	
	</div>
<script>
	<%-- 주소명으로 검색 --%>
	function getAddr(){
		
		$.ajax({
			 url :"http://www.juso.go.kr/addrlink/addrLinkApiJsonp.do" //인터넷망
			,type:"post"
			,data:$("#searchForm").serialize()
			,dataType:"jsonp"
			,crossDomain:true
			,success:function(jsonStr){
				var errCode = jsonStr.results.common.errorCode;
				var errDesc = jsonStr.results.common.errorMessage;
				if(errCode != "0"){
					alert(errCode+"="+errDesc);
				}else{
					if(jsonStr != null){
						makeListJson(jsonStr);
					}
				}
			}
		    ,error: function(xhr,status, error){
		    	alert("에러발생");
		    }
		});
	}
	
	<%-- 결과 테이블 생성 --%>
	function makeListJson(jsonStr){
		var htmlStr = "";
		$(jsonStr.results.juso).each(function(){
			htmlStr += "<tr onclick=\"javascript:chooseAddress('"+this.roadAddr+"', '"+this.jibunAddr+"', '"+this.zipNo+"');\">";
			htmlStr += "<td>";
			htmlStr += "<dl>"+this.roadAddr+"</dl>";
			htmlStr += "<dl>"+this.jibunAddr+"</dl>";
			htmlStr += "</td>";
			htmlStr += "<td>"+this.zipNo+"</td>";
			htmlStr += "</tr>";
		});
		$("#addressTableTbody").html(htmlStr);
	}
	
	<%-- Enter 키 이벤트 --%>
	function enterSearch() {
		var evt_code = (window.netscape) ? ev.which : event.keyCode;
		if (evt_code == 13) {    
			event.keyCode = 0;  
			fn_search();
		} 
	}
	
	<%-- 주소 선택 --%>
	function chooseAddress(roadAddr, jibunAddr, zipNo){
		var aParam = [];
		aParam["roadAddr"] = roadAddr;
		aParam["jibunAddr"] = jibunAddr;
		aParam["zipNo"] = zipNo;

		opener.callback_openAddressPopup(aParam);
		window.close();
	}
</script>