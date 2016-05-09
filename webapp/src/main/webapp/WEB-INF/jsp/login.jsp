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
<jsp:include page="include/head.jsp" />

</head>
<body>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<!-- Print the success alert for logout -->
					<c:if test="${param.logout != null}">
						<div class="alert alert-dismissible alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="logout"><spring:message
									code="security.action.logout" /></label>
						</div>
					</c:if>
					<!-- Print the failure alert -->
					<c:if test="${param.error != null}">
						<div class="alert alert-dismissible alert-danger">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="loginError"><spring:message
									code="security.error.login" /></label>
						</div>
					</c:if>
					<h1>
						<spring:message code="security.login" />
					</h1>
					<!-- Login form -->

					<form:form action="login" method="POST" id="loginForm">
						<fieldset>
							<!-- User name form -->
							<div class="form-group">
								<label for="username"><spring:message
										code="security.username" /></label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.username" var="login" />
								<input type="text" class="form-control" id="username"
									name="username" placeholder="${login}" />
							</div>
							<!-- Password form -->
							<div class="form-group">
								<label for="password"><spring:message
										code="security.password" /> </label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.password" var="pwd" />
								<input type="password" class="form-control" id="password"
									name="password" placeholder="${pwd}" />
							</div>
						</fieldset>
						<!-- Validate credentials -->
						<div class="actions pull-right">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" id="_csrf" /> <input type="submit"
								value=<spring:message code="security.login.button" />
								class="btn btn-primary">
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>