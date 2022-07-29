<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%String greeting = (String)request.getAttribute("greeting"); %>
<div style="color:red"><%=greeting %></div>
</body>
</html>