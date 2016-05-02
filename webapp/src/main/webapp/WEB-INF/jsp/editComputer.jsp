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
<title><spring:message code="label.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Include the Bootstrap -->
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/resources/css/main.css"
	rel="stylesheet" media="screen">

<header class="navbar navbar-inverse navbar-fixed-top">

	<!-- The navbar can redirect to the dashboard -->
	<div class="container">
		<a class="navbar-brand" href="dashboard.html"> <spring:message
				code="label.app" />
		</a>
	</div>

	<!-- Show the language flags in the right of the navbar, they're used to change the page language -->
	<div align="right" id="flags">
		<a href="?id=${computerDTO.id}&lang=en"> <img alt="en"
			title="English"
			src=<spring:url value="/resources/images/gb.png"></spring:url>>
		</a> <a href="?id=${computerDTO.id}&lang=fr"> <img alt="fr"
			title="French"
			src=<spring:url value="/resources/images/fr.png"></spring:url>>
		</a>
	</div>

	<!-- Translate text in scripts -->
	<script type="text/javascript">
		var translate = new Array();
		translate['nameEmpty'] = "<spring:message code='error.name.empty' javaScriptEscape='true' />";
		translate['nameSize'] = "<spring:message code='error.name.size' javaScriptEscape='true' />";
		translate['nameContent'] = "<spring:message code='error.name.content' javaScriptEscape='true' />";
		translate['dateFormat'] = "<spring:message code='error.date.format' javaScriptEscape='true' />";
		translate['dateOld'] = "<spring:message code='error.date.old' javaScriptEscape='true' />";
		translate['dateTemporal'] = "<spring:message code='error.date.temporal' javaScriptEscape='true' />";
	</script>

	<!-- Scripts -->
	<script
		src="${pageContext.request.contextPath}/resources/js/computer.validation.js"></script>

</header>
<body>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">
						id:
						<c:out value="${computerDTO.id}" />
					</div>
					<!-- Print the success alert -->
					<c:if test="${success}">
						<div class="alert alert-dismissible alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerSuccess"><spring:message
									code="label.edit.success" /></label>
						</div>
					</c:if>
					<!-- Print the failure alert -->
					<c:if test="${failure}">
						<div class="alert alert-dismissible alert-danger">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerSuccess"><spring:message
									code="label.edit.failure" /></label>
						</div>
					</c:if>
					<h1>
						<spring:message code="label.edit" />
					</h1>
					<!-- Computer editing form -->
					<form:form action="editComputer?id=${computerDTO.id}" method="POST"
						modelAttribute="computerDTO">
						<input type="hidden" value="0" />
						<fieldset>
							<!-- Computer name form -->
							<div class="form-group">
								<form:label for="computerName" path="name">
									<spring:message code="label.computerName" />
								</form:label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.discontinued" var="compName" />
								<form:input type="text" class="form-control" id="computerName"
									name="computerName" placeholder="${compName}"
									value="${computerDTO.name}" path="name" />
								<form:errors path="name" cssclass="error" />
							</div>
							<!-- Introduced date form -->
							<div class="form-group">
								<form:label for="introduced" path="introduced">
									<spring:message code="label.introduced" />
								</form:label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.introduced" var="intro" />
								<form:input type="date" class="form-control" id="introduced"
									name="introduced" placeholder="${intro}"
									value="${computerDTO.introduced}" path="introduced" />
								<form:errors path="introduced" cssclass="error" />
							</div>
							<!-- Discontinued date form -->
							<div class="form-group">
								<form:label for="discontinued" path="discontinued">
									<spring:message code="label.discontinued" />
								</form:label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.discontinued" var="discon" />
								<form:input type="date" class="form-control" id="discontinued"
									name="discontinued" placeholder="${discon}"
									value="${computerDTO.discontinued}" path="discontinued" />
								<form:errors path="discontinued" cssclass="error" />
								<form:errors cssclass="error" />
							</div>
							<!-- Company form -->
							<div class="form-group">
								<form:label for="companyId" path="companyId">
									<spring:message code="label.company" />
								</form:label>
								<!-- Browse companies -->
								<select class="form-control" id="companyId" name="companyId">
									<option value="0"><c:out value="--" /></option>
									<c:forEach items="${companies}" var="company">
										<!-- Select the current company -->
										<option value="${company.id}"
											${company.id == computerDTO.companyId ? 'selected="selected"' : ''}>
											<c:out value="${company.name}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit"
								value=<spring:message code="label.editButton" />
								class="btn btn-primary">
							<spring:message code="label.or" />
							<a href="computer" class="btn btn-default"><spring:message
									code="label.cancel" /></a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>