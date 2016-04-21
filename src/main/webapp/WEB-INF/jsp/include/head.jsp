<!-- The header for the JSP pages include the navbar and the language flags -->

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


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
		<a href="?lang=en"> <img alt="en" title="English"
			src=<spring:url value="/resources/images/gb.png"></spring:url>>
		</a> <a href="?lang=fr"> <img alt="fr" title="French"
			src=<spring:url value="/resources/images/fr.png"></spring:url>>
		</a>
	</div>

</header>