<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<title>관리</title>
</head>
   <body>
   <div class="container w-75 mt-5 mx-auto">
	   <h2>${sns.title}</h2>
	   <hr>
	   <div class="card w-75 mx-auto">
	    <c:forEach var="img" items="${imglist}" varStatus="status">
	    	<p>[${status.count}]</p>
		   <img class="card-img-top" src="/image/${img}"> 
		</c:forEach> 
	   
	    <div class="card-body">
	    	<h4 class="card-title">Date: ${sns.date}</h4>
	    	<p class="card-text">Content: ${sns.content}</p>
	    </div>
	   </div>
	   <hr>
	  	<a href="javascript:history.back()" class="btn btn-primary">Back</a>
	   	<button class="btn btn-outline-info" type="button" data-bs-toggle="collapse" data-bs-target="#addForm" aria-expanded="false" aria-controls="addForm">수정 </button>
	   
		<div class="collapse" id="addForm">
		  <div class="card card-body">
			<form method="post" action="/sns/update/${sns.sid}" enctype="multipart/form-data">
				<%-- <input type="hidden" name="sid" class="form-control" value ="${sns.sid}"> --%>
				<label class="form-label">제목</label>
				<input type="text" name="title" class="form-control" value ="${sns.title}">
<!-- 				<label class="form-label">이미지</label>
				<input type="file" name="file" class="form-control" > -->
				<label class="form-label">내용</label>
				<textarea cols="50" rows="5" name="content" class="form-control"> ${sns.content}</textarea>
				<button type="submit" class="btn btn-success mt-3">저장</button>
			</form>
		  </div>
		</div>
   </div>
   </body>
</html>