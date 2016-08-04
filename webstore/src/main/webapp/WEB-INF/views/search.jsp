<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Search</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Search for books</h1>
				<p>Here you can search for books by title and author.</p>
				<p>
					<a href=" <spring:url value="/" /> " class="btn btn-primary"> <span
						class="glyphicon glyphicon-home" /></span> Home
					</a>
				</p>
			</div>
		</div>
	</section>
	<section class="container">
		<!--
		<div class="row">
			<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
				<div class="thumbnail">
					<div class="caption">
						<form action="/webstore/books/found" method="post">
							<h3>Title</h3>
							<p>Enter book title</p>
							<input type="text" name="bookTitle" />
							<h3>Author</h3>
							<p>Enter book author</p>
							<input type="text" name="bookAuthor" /> <input type="submit"
								value="Search by clicking this button" />
						</form>
					</div>
				</div>
			</div>
		</div>
		  -->
		<div>
			<form action="/webstore/books/found" method="post">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Title</span> <input
						type="text" name="bookTitle" class="form-control"
						placeholder="Enter book title" aria-describedby="basic-addon1">
					<span class="input-group-addon" id="basic-addon1">Author</span> <input
						type="text" name="bookAuthor" class="form-control"
						placeholder="Enter book author" aria-describedby="basic-addon1">
				</div>
				<div class="text-center">
					<p>
					<div class="btn-group " role="group" aria-label="...">
						<button type="submit" class="btn btn-primary">
							<span class="glyphicon glyphicon-search"></span> Search
						</button>
					</div>
				</div>
			</form>
		</div>
	</section>
</body>
</html>