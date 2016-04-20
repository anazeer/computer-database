<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/resources/css/main.css"
	rel="stylesheet" media="screen">
<script
	src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/jquery.validate.js"></script>
<!-- <script src="resources/js/jquery.validation.js"></script> -->
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<mylib:link target="computer" classLink="navbar-brand"
				text=" Application - Computer Database " />
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<!-- Print the success alert -->
					<c:if test="${success}">
						<div class="alert alert-dismissible alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerSuccess">${vsuccess}</label>
						</div>
					</c:if>
					<!-- Print the failure alert -->
					<c:if test="${failure}">
						<div class="alert alert-dismissible alert-danger">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerError">${vfailure}</label>
						</div>
					</c:if>
					<h1>Add Computer</h1>
					<!-- Computer adding form -->
					<form:form action="addComputer" method="POST" id="computerForm" modelAttribute="computerDTO">
						<fieldset>
							<div class="form-group">
								<form:label for="computerName" path="name">Computer name</form:label>
								<form:input
									type="text" class="form-control" id="computerName"
									name="computerName" placeholder="Computer name"
									path="name" />
								<form:errors path="name" cssclass="error" />
							</div>
							<div class="form-group">
								<form:label for="introduced" path="introduced">Introduced date</form:label>
								<form:input
									type="date" class="form-control" id="introduced"
									name="introduced" placeholder="Introduced date (yyyy-MM-dd)"
									path="introduced"/>
							<form:errors path="introduced" cssclass="error" />
							</div>
							<div class="form-group">
								<form:label for="discontinued" path="discontinued">Discontinued date</form:label>
								<form:input
									type="date" class="form-control" id="discontinued"
									name="discontinued" placeholder="Discontinued date (yyyy-MM-dd)"
									path="discontinued" />
								<form:errors path="discontinued" cssclass="error" />
								<form:errors cssclass="error" />
							</div>
							<div class="form-group">
								<form:label for="companyId" path="companyId">Company</form:label>
								<!-- Browse companies -->
								<select class="form-control" id="companyId" name="companyId">
									<option value="0"><c:out value="--" /></option>
								<c:forEach items="${companies}" var="company">
										<option value="${company.id}"
											${company.id == computerDTO.companyId ? 'selected="selected"' : ''}>
											<c:out value="${company.name}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<!-- Add or Cancel the addition of the computer -->
						<div class="actions pull-right">
							<input type="submit" value="Add" class="btn btn-primary">
							or
							<mylib:link target="computer" classLink="btn btn-default"
								text="Cancel" />
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>