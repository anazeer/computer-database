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
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
<script src="resources/js/jquery.min.js"></script>
<script src="resources/js/jquery.validate.js"></script>
<!-- <script src="resources/js/jquery.validation.js"></script> -->
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <mylib:link target="computer" classLink="navbar-brand" text=" Application - Computer Database "/>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                <c:if test="${success}"><div class="alert alert-success" role="alert">${vsuccess}</div></c:if>
                    <h1>Add Computer</h1>
                    <form action="computerAdd" method="POST" id ="computerForm">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name" value="${pname}">
                                <span id="computerNameError" style="color:red;">${vcomputerName}</span>
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date (yyyy-mm-dd)" value="${pintro}">
                                <span id="computerIntroError" style="color:red;">${vintroduced}</span>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date (yyyy-mm-dd)" value="${pdiscontinued}">
                                <span id="computerDiscontinuedError" style="color:red;">${vdiscontinued}</span>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId" >
                                	<option value="0"><c:out value="Ignore"/></option>
                               		<c:forEach items="${companies}" var="company">
                                    	<option value="${company.id}"><c:out value="${company.name}"/></option>
                                	</c:forEach>
                                </select>
                                <span id="computerError" style="color:red;">${vdate}</span>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <mylib:link target="computer" classLink="btn btn-default" text="Cancel"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>