<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags" %>
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
            <mylib:link target="computer" classLink="navbar-brand" text=" Application - Computer Database "/>
        </div>
    </header>
    <section id="main">
        <div class="container">
        	<!-- Print the success alert -->
            <c:if test="${deleteSuccess}"><div class="alert alert-success" id="computerSuccess" role="alert">${deleteMsg}</div></c:if>
            <h1 id="homeTitle">
                <!-- Print computers count -->
                <c:out value="${countComputer}"/> Computer<c:if test="${countComputer gt 1}"><c:out value="s"/></c:if> found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="computer" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" value="${search}" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="computerAdd">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="computerDelete" method="POST">
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
                        	<c:set var="newOrder" scope="page" value="${not empty order ? (order eq 'dsc' ? 'asc' : 'dsc') : 'asc'}"/>
                        	<mylib:link text="Computer name" target="computer" limit="${pagination.limit}"
                        		page="${pagination.currentPage}" search="${search}" order="${newOrder}" />
                        	<c:choose>
	                        	<c:when test="${newOrder eq 'dsc' }"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span></c:when>
	                        	<c:otherwise><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span></c:otherwise>
                        	</c:choose>
                        </th>
                        <th>
                            Introduced date <span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date <span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company <span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
               	<c:forEach var="computer" items="${pagination.listFromPage}">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="${computer.id}">
                        </td>
                        <td>
                        	<mylib:link target="computerEdit" text="${computer.name}" id="${computer.id}"/>
                        </td>
                        <td><c:out value="${computer.introduced}"/> </td>
                        <td><c:out value="${computer.discontinued}"/> </td>
                        <td><c:out value="${computer.companyName}"/> </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <!-- Links to other pages with pagination -->
            <ul class="pagination">
            	<mylib:pagination countpage="${pagination.lastPage}" limit="${pagination.limit}" currentpage="${pagination.currentPage}" search="${search}" />
        	</ul>
            <!-- Page limit buttons -->
            <div class="btn-group btn-group-sm pull-right" role="group" >
                <button type="button" class="btn btn-default" onclick="javascript:window.location='computer?page=1&limit=10&search=${search}'">10</button>
                <button type="button" class="btn btn-default" onclick="javascript:window.location='computer?page=1&limit=20&search=${search}'">20</button>
                <button type="button" class="btn btn-default" onclick="javascript:window.location='computer?page=1&limit=50&search=${search}'">50</button>
            </div>
      </div>
    </footer>
    <!-- Scripts -->
    <script src="resources/js/jquery.min.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/dashboard.js"></script>
    <script src="resources/js/jquery.validate.js"></script>

    </body>
</html>