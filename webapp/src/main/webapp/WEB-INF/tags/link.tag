<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="target" required="true" type="java.lang.String"
	description="The target page"%>
<%@ attribute name="text" required="true" type="java.lang.String"
	description="The link text"%>
<%@ attribute name="classLink" required="false" type="java.lang.String"
	description="The class application"%>
<%@ attribute name="limit" required="false" type="java.lang.String"
	description="The page limit"%>
<%@ attribute name="page" required="false" type="java.lang.String"
	description="The page number"%>
<%@ attribute name="id" required="false" type="java.lang.String"
	description="The computer id"%>
<%@ attribute name="search" required="false" type="java.lang.String"
	description="The search text"%>
<%@ attribute name="order" required="false" type="java.lang.String"
	description="The order of the elements, asc or dsc"%>
	
<c:set var="pageText" scope="page"
	value="page=${page}" />
<c:set var="limitText" scope="page"
	value="&limit=${limit}" />
<c:set var="searchText" scope="page"
	value="&search=${search}" />
<c:set var="orderText" scope="page"
	value="&order=${order}" />

<c:set var="pageInfo" scope="page"
	value="${not empty page ? pageText : ''}" />
<c:set var="limitInfo" scope="page"
	value="${not empty limit ? limitText : ''}" />
<c:set var="searchInfo" scope="page"
	value="${not empty search ? searchText : ''}" />
<c:set var="orderInfo" scope="page"
	value="${not empty order ? orderText : ''}" />
	
<c:choose>
	<c:when test="${target != '#'}">
		<c:choose>
			<c:when test="${not empty id}">
				<a href="${target}?id=${id}" class="${classLink}"><c:out
						value="${text}" /></a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty page}">
						<a href="${target}?${pageInfo}${limitInfo}${searchInfo}${orderInfo}"
							class="${classLink}"><c:out value="${text}" /></a>
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