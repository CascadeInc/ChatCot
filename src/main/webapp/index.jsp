<%--
  Created by IntelliJ IDEA.
  User: Alexey
  Date: 13.02.2018
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="en" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />
<html lang="${language}">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Hello, world!</title>
</head>
<body>
<br/>
<fmt:message key="welcome" var="wel"/>
<h2 class="text-center">${wel}</h2>
<br/>
<form>
<select id="language" class="form-control" name="language" onchange="submit()">
    <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
    <option value="ru" ${language == 'ru' ? 'selected' : ''}>Russian</option>
</select>
</form>
<fmt:message key="send" var="se"/>
<form action="main" method="post" enctype="multipart/form-data">
    <input type="text" class="text" name="quote">
    <button type="submit" class="btn btn-primary">${se}</button>
</form>
<h1 class="alert-info"><fmt:message key="result" /> = ${answer}</h1>
<fmt:message key="save" var="sa"/>
<% if ("NEW".equals(request.getAttribute("answer"))) { %>
<form action="main" method="post" enctype="multipart/form-data">
    <select id="type" class="form-control" name="type">
        <option value="standard greetings">standard greetings</option>
        <option value="special greetings">special greetings</option>
        <option value="question greetings">question greetings</option>
        <option value="greetings answer">greetings answer</option>
        <option value="yes">yes</option>
        <option value="no">no</option>
        <option value="default">default</option>
        <option value="initialize">initialize</option>
        <option value="filter">filter</option>
        <option value="adding">adding</option>
    </select>
    <button type="submit" class="btn btn-primary">${sa}</button>
</form>
<% } %>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="${request.contextPath}/WEB-INF/lib/http_code.jquery.com_jquery-3.2.1.slim.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="${request.contextPath}/WEB-INF/lib/http_cdnjs.cloudflare.com_ajax_libs_popper.js_1.12.9_umd_popper.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="${request.contextPath}/WEB-INF/lib/http_maxcdn.bootstrapcdn.com_bootstrap_4.0.0_js_bootstrap.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
