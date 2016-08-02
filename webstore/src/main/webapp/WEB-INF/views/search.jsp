<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
			</div>
		</div>
	</section>
	<section class="container">
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
	</section>
</body>
</html>