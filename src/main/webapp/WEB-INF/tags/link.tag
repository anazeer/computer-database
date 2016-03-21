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
<%@ attribute name="id" required="false" type="java.lang.String"
	description="The computer id"%>

<c:choose>
	<c:when test="${target != '#'}">
		<c:choose>
			<c:when test="${not empty page}">
				<a href="${target}?page=${page}&limit=${size}" class="${classLink}"><c:out
						value="${text}" /></a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty id}">
						<a href="${target}?id=${id}" class="${classLink}"><c:out
								value="${text}" /></a>
					</c:when>
					<c:otherwise>
						<a href="${target}" class="${classLink}"><c:out
								value="${text}" /></a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<a href="#" class="${classLink}"><c:out value="${text}" /></a>
	</c:otherwise>
</c:choose>