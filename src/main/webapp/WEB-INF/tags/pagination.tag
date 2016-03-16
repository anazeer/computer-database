<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags"%>

<%@ attribute name="currentpage" required="true" type="java.lang.String"
	description="The current page"%>
<%@ attribute name="count" required="true" type="java.lang.String"
	description="The number of elements per page"%>
<%@ attribute name="countpage" required="true" type="java.lang.String"
	description="The total number of pages"%>

<c:set var="offset" scope="page" value="5" />

<c:if test="${currentpage gt 1}">
	<li><a href="computer?page=${currentpage - 1}"
		aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
	</a></li>
</c:if>
<c:choose>
	<c:when test="${currentpage - offset gt 1}">
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
	<c:when test="${begin lt offset}">
		<c:set var="end" scope="page"
			value="${currentpage + 2 * offset - begin + 1}" />
	</c:when>
	<c:otherwise>
		<c:set var="end" scope="page" value="${currentpage + offset}" />
	</c:otherwise>
</c:choose>
<c:forEach begin="${begin}" end="${end}" var="i">
	<c:choose>
		<c:when test="${currentpage eq i}">
			<li><a href="">${i}</a></li>
		</c:when>
		<c:otherwise>
			<li><a href="computer?page=${i}&limit=${count}">${i}</a></li>
		</c:otherwise>
	</c:choose>
</c:forEach>
<c:if test="${currentpage lt countpage}">
	<li><a href="computer?page=${currentpage + 1}&limit=${count}"
		aria-label="Next"> <span aria-hidden="true">&raquo;</span>
	</a></li>
</c:if>