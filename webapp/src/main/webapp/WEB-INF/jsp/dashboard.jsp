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

<!-- Logout link -->
<div align="right" id="login">
	<c:if test="${not empty user}">
		<c:out value="${user}" />
		<a href="logout"><c:out value="(Logout)" /> </a>
	</c:if>
</div>

<body>
	<section id="main">
		<div class="container">
			<!-- Print the success alert -->
			<c:if test="${deleteSuccess}">
				<div class="alert alert-success" id="computerSuccess" role="alert">${deleteMsg}</div>
			</c:if>
			<h1 id="homeTitle">
				<!-- Print computers totalCount -->
				<c:out value="${page.totalCount}" />
				<spring:message code="label.computer" />
				<spring:message code="label.found" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="computer" method="GET"
						class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control"
							placeholder=<spring:message code="tooltip.searchForm" />
							value="${page.filter}" /> <input type="submit" id="searchsubmit"
							value=<spring:message code="label.searchButton" />
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">
						<spring:message code="label.add" />
					</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();"><spring:message
							code="label.editButton" /></a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="computer" method="POST">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <input type="hidden" name="selection"
				value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<!-- Table header for Computer name -->
						<th>
							<!-- Retrieve the current ordering for computer name --> <c:set
								var="newOrder" scope="page"
								value="${not empty page.order ? (page.order eq 'name_dsc' ? 'name_asc' : 'name_dsc') : 'name_asc'}" />

							<spring:message code="label.computerName" var="compName" /> <mylib:link
								text="${compName}" target="computer" limit="${page.limit}"
								page="${page.currentPage}" search="${page.filter}"
								order="${newOrder}" /> <c:choose>
								<c:when test="${newOrder eq 'name_dsc' }">
									<span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-chevron-down"
										aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</th>
						<!-- Table header for Introduced date -->
						<th>
							<!-- Retrieve the current ordering for introduced date --> <c:set
								var="newOrder" scope="page"
								value="${not empty page.order ? (page.order eq 'introduced_dsc' ? 'introduced_asc' : 'introduced_dsc') : 'introduced_asc'}" />
							<spring:message code="label.introduced" var="intro" /> <mylib:link
								text="${intro}" target="computer" limit="${page.limit}"
								page="${page.currentPage}" search="${page.filter}"
								order="${newOrder}" /> <c:choose>
								<c:when test="${newOrder eq 'introduced_dsc' }">
									<span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-chevron-down"
										aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</th>
						<!-- Table header for Discontinued date -->
						<th>
							<!-- Retrieve the current ordering for discontinued date --> <c:set
								var="newOrder" scope="page"
								value="${not empty page.order ? (page.order eq 'discontinued_dsc' ? 'discontinued_asc' : 'discontinued_dsc') : 'discontinued_asc'}" />
							<spring:message code="label.discontinued" var="discon" /> <mylib:link
								text="${discon}" target="computer" limit="${page.limit}"
								page="${page.currentPage}" search="${page.filter}"
								order="${newOrder}" /> <c:choose>
								<c:when test="${newOrder eq 'discontinued_dsc' }">
									<span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-chevron-down"
										aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</th>
						<!-- Table header for Company -->
						<th>
							<!-- Retrieve the current ordering for company --> <c:set
								var="newOrder" scope="page"
								value="${not empty page.order ? (page.order eq 'company_dsc' ? 'company_asc' : 'company_dsc') : 'company_asc'}" />
							<spring:message code="label.company" var="company" /> <mylib:link
								text="${company}" target="computer" limit="${page.limit}"
								page="${page.currentPage}" search="${page.filter}"
								order="${newOrder}" /> <c:choose>
								<c:when test="${newOrder eq 'company_dsc' }">
									<span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-chevron-down"
										aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="computer" items="${page.elements}">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><mylib:link target="editComputer"
									text="${computer.name}" id="${computer.id}" /></td>
							<td><c:out value="${computer.introduced}" /></td>
							<td><c:out value="${computer.discontinued}" /></td>
							<td><c:out value="${computer.companyName}" /></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<!-- Links to other pages with page -->
			<ul class="pagination">
				<mylib:pagination countpage="${page.lastPage}" limit="${page.limit}"
					currentpage="${page.currentPage}" search="${page.filter}"
					order="${page.order}" />
			</ul>
			<!-- Page limit buttons -->
			<div class="btn-group btn-group-sm pull-right" role="group">
				<button type="button" class="btn btn-default"
					onclick="javascript:window.location='computer?page=1&limit=10&search=${page.filter}&order=${page.order}'">10</button>
				<button type="button" class="btn btn-default"
					onclick="javascript:window.location='computer?page=1&limit=20&search=${page.filter}&order=${page.order}'">20</button>
				<button type="button" class="btn btn-default"
					onclick="javascript:window.location='computer?page=1&limit=50&search=${page.filter}&order=${page.order}'">50</button>
			</div>
		</div>
	</footer>

	<!-- Translate text in scripts -->
	<script type="text/javascript">
		var translate = new Array();
		translate['view'] = "<spring:message code='js.view' javaScriptEscape='true' />";
		translate['edit'] = "<spring:message code='js.edit' javaScriptEscape='true' />";
		translate['confirm'] = "<spring:message code='js.confirm' javaScriptEscape='true' />";
	</script>

	<!-- Scripts -->
	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/dashboard.js"></script>

</body>
</html>