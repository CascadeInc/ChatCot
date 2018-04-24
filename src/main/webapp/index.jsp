<%--
  Created by IntelliJ IDEA.
  User: Alexey
  Date: 13.02.2018
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="en" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>ChatCot</title>

    <!-- Bootstrap CSS -->
    <link href="${request.contextPath}/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<br/>
<fmt:message key="welcome" var="wel"/>
<h2 class="text-center">${wel} </h2>
<h2 class="text-center"><c:out value="${sessionScope.userName}"/></h2>
<br/>
<h2 class="text-center"><c:out value="${sessionScope.userScore}"/></h2>
<br/>
<% if (session.getAttribute("userName") == null) { %>
<form action="login" method="post">
    <h4>Login</h4>
    <input type="text" class="text" name="login">
    <br/>
    <h4>Password</h4>
    <input type="password" class="password-field" name="password">
    <br/>
    <button type="submit" class="btn btn-primary">login</button>
    <button type="button" class="btn btn-primary" onClick='location.href="pages/register.jsp"'>sign up</button>
    <br/>
</form>
<% } else { %>
<form action="logout" method="get">
    <br/>
    <button type="submit" class="btn btn-primary">logout</button>
</form>
<% } %>
<h4>${loginResult}</h4>
<h4>${registerResult}</h4>
<br/>
<form>
<select id="language" class="form-control" name="language" onclick="submit()">
    <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
    <option value="ru" ${language == 'ru' ? 'selected' : ''}>Russian</option>
</select>
</form>
<button type="button" class="btn btn-primary" onClick='location.href="/admin"'>View phrases</button>
<fmt:message key="send" var="se"/>
<form action="main" method="post">
    <input type="text" class="text" name="quote">
    <button type="submit" class="btn btn-primary">${se}</button>
    <% if ("NEW".equals(request.getAttribute("answer"))) { %>
    <select class="form-control" name="choiceType">
        <option value="standard greetings">standard greetings</option>
        <option value="special greetings">special greetings</option>
        <option value="question greetings">question greetings</option>
        <option value="greetings answer">greetings answer</option>
        <option value="yes">yes</option>
        <option value="no">no</option>
        <option value="default">default</option>
        <option value="initialize">initialize</option>
        <option value="filter">filter</option>
        <option value="filter by check">filter check</option>
        <option value="adding">adding</option>
    </select>
    <% } %>
</form>
<h1 class="alert-info"><fmt:message key="result" />: </h1>${answer}
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="${request.contextPath}/js/http_cdnjs.cloudflare.com_ajax_libs_popper.js_1.12.9_umd_popper.js"></script>
<script src="${request.contextPath}/js/jquery-latest.js"></script>
<script src="${request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
