<!DOCTYPE html>
<%@page import="com.excilys.cdb.servlets.computer.ListServlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
            <c:out value="${countComputer}"/> Computer<c:if test="${countComputer gt 1}"><c:out value="s"/></c:if> found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="computer.add">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
               	<c:forEach var="computer" items="${pagination.listFromOffset}">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="0">
                        </td>
                        <td>
                            <a href="editComputer.html" onclick=""><c:out value="${computer.name}"/> </a>
                        </td>
                        <td><c:out value="${computer.introduced}"/> </td>
                        <td><c:out value="${computer.discontinued}"/> </td>
                        <td><c:out value="${computer.company.name}"/> </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
            <c:if test="${pagination.currentPage gt 1}">
                <li>
                    <a href="computer.show?page=${pagination.currentPage - 1}" aria-label="Previous">
                      <span aria-hidden="true">&laquo;</span>
                  </a>
              	</li>
              </c:if>
              <c:choose>
	              <c:when test="${pagination.currentPage - 5 gt 1}">
	              	<c:set var="begin" scope="page" value="${pagination.currentPage - 5}"/>
	              </c:when>
	              <c:otherwise>
	              	<c:set var="begin" scope="page" value="1"/>
	              </c:otherwise>
              </c:choose>
              <c:choose>
	              <c:when test="${pagination.currentPage + 5 ge pagination.countPages}">
	              	<c:set var="end" scope="page" value="${pagination.countPages}"/>
	              </c:when>
	              <c:when test="${begin lt 5}">
	              	<c:set var="end" scope="page" value="${pagination.currentPage + 5 + 5 - begin + 1}"/>
	              </c:when>
	              <c:otherwise>
	              	<c:set var="end" scope="page" value="${pagination.currentPage + 5}"/>
	              </c:otherwise>
              </c:choose>
              <c:forEach begin="${begin}" end="${end}" var="i">
              	<c:choose>
	              	<c:when test="${pagination.currentPage eq i}">
	              		<li><a href="">${i}</a></li>
	              	</c:when>
	              	<c:otherwise>
	              		<li><a href="computer.show?page=${i}">${i}</a></li>
	              	</c:otherwise>
              	</c:choose>
              </c:forEach>
              <c:if test="${pagination.currentPage lt pagination.countPages}">
              	<li>
              		<a href="computer.show?page=${pagination.currentPage + 1}" aria-label="Next">
                		<span aria-hidden="true">&raquo;</span>
                	</a>
                </li>
              </c:if>

        </ul>
		</div>
        <div class="btn-group btn-group-sm pull-right" role="group" >
            <button type="button" class="btn btn-default">10</button>
            <button type="button" class="btn btn-default">50</button>
            <button type="button" class="btn btn-default">100</button>
        </div>
    </footer>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/dashboard.js"></script>

</body>
</html>