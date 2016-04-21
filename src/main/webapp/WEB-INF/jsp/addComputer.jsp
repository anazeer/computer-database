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
					<!-- Print the success alert -->
					<c:if test="${success}">
						<div class="alert alert-dismissible alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerSuccess"><spring:message code="label.add.success" /></label>
						</div>
					</c:if>
					<!-- Print the failure alert -->
					<c:if test="${failure}">
						<div class="alert alert-dismissible alert-danger">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<label id="computerError"><spring:message code="label.add.failure" /></label>
						</div>
					</c:if>
					<h1>
						<spring:message code="label.add" />
					</h1>
					<!-- Computer adding form -->
					<form:form action="addComputer" method="POST" id="computerForm"
						modelAttribute="computerDTO">
						<fieldset>
							<!-- Computer name form -->
							<div class="form-group">
								<form:label for="computerName" path="name">
									<spring:message code="label.computerName" var="compName" />
								</form:label>
								<!-- Placeholder text -->
								<spring:message code="tooltip.computerName" var="compName" />
								<form:input type="text" class="form-control" id="computerName"
									name="computerName" placeholder="${compName}" path="name" />
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
									name="introduced" placeholder="${intro}" path="introduced" />
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
									name="discontinued" placeholder="${discon}" path="discontinued" />
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
						<!-- Add or Cancel the addition of the computer -->
						<div class="actions pull-right">
							<input type="submit"
								value=<spring:message code="label.addButton" />
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