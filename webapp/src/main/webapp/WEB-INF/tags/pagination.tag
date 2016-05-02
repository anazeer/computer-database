<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags"%>

<%@ attribute name="currentpage" required="true" type="java.lang.String"
	description="The current page"%>
<%@ attribute name="limit" required="true" type="java.lang.String"
	description="The number of elements per page"%>
<%@ attribute name="countpage" required="true" type="java.lang.String"
	description="The total number of pages"%>
<%@ attribute name="search" required="false" type="java.lang.String"
	description="The search text"%>
<%@ attribute name="order" required="false" type="java.lang.String"
	description="The order of the elements, asc or dsc"%>
	
<c:set var="offset" scope="page" value="2" />

<c:if test="${currentpage gt 1}">
	<li><a href="computer?page=${currentpage - 1}&limit=${limit}&search=${search}&order=${order}"
		aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
	</a></li>
</c:if>
<c:choose>
	<c:when test="${currentpage - offset ge 1}">
		<c:set var="begin" scope="page" value="${currentpage - offset}" />
	</c:when>
	<c:otherwise>
		<c:set var="begin" scope="page" value="1" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${currentpage + offset ge countpage}">
		<c:set var="end" scope="page" value="${countpage}" />
	</c:when>
	<c:otherwise>
		<c:set var="end" scope="page" value="${currentpage + offset}" />
	</c:otherwise>
</c:choose>

<c:if test="${begin != 1}">
	<li><mylib:link target="computer" text="1" page="1" limit="${limit}" search="${search}" order="${order}"/></li>
</c:if>

<c:forEach begin="${begin}" end="${end}" var="i">
	<c:choose>
		<c:when test="${currentpage eq i}">
			<li><a href="">${i}</a></li>
		</c:when>
		<c:otherwise>
			<li><mylib:link target="computer" text="${i}" page="${i}" limit="${limit}" search="${search}" order="${order}"/></li>
		</c:otherwise>
	</c:choose>
</c:forEach>

<c:if test="${end != countpage}">
	<li><mylib:link target="computer" text="${countpage}" page="${countpage}" limit="${limit}" search="${search}" order="${order}"/></li>
</c:if>

<c:if test="${currentpage - countpage lt 0}">
	<li><a href="computer?page=${currentpage + 1}&limit=${limit}&search=${search}&order=${order}"
		aria-label="Next"> <span aria-hidden="true">&raquo;</span>
	</a></li>
</c:if>