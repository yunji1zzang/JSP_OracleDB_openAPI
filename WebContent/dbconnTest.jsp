<%@page import="java.sql.SQLException"%>
<%@page import="jdbc.connection.ConnectionProvider"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>연결 테스트</title>
</head>
<body>
<%
	// try-with-resource 구문 사용
	// try 블록의 실행이 끝나면 괄호 안에 생성한 자원의 close()를 실행해 줌
	// ConnectionProvider.getConnerction() 메서드를 사용해 Connection을 구함
	try (Connection conn = ConnectionProvider.getConnection()) {
		out.println("커넥션 연결 성공함");
	} catch (SQLException ex) {
		out.println("커넥션 연결 실패함 : " + ex.getMessage());
		application.log("커넥션 연결 실패", ex);
	}
%>
</body>
</html>