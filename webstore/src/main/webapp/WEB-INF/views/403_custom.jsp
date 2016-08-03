<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Error 403</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Acces Denied</h1>
				<p>User: ${user} do not have permission to this operation. This
					incident will be reported.</p>
				<p>
					<a href=" <spring:url value="/" /> " class="btn btn-primary"> <span
						class="glyphicon glyphicon-home" /></span> Home
					</a>
				</p>
			</div>
		</div>
	</section>
</body>
</html>
