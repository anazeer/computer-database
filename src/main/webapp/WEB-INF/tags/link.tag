<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="target" required="true" type="java.lang.String"
	description="The target page"%>
<%@ attribute name="text" required="true" type="java.lang.String"
	description="The link text"%>
<%@ attribute name="classLink" required="false" type="java.lang.String"
	description="The class application"%>
<%@ attribute name="size" required="false" type="java.lang.String"
	description="The page size"%>
<%@ attribute name="page" required="false" type="java.lang.String"
	description="The page number"%>

<c:choose>
	<c:when test="${target != '#'}">
		<a href="${target}?page=${page}&size=${size}" class="${classLink}">${text}</a>
	</c:when>
	<c:otherwise>
		<a href="#" class="${classLink}">${text}</a>
	</c:otherwise>
</c:choose>