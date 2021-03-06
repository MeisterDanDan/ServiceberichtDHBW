<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Liste der Serviceberichte
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/tasks/task/new/"/>">Servicebericht anlegen</a>
        </div>

    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Bezeichnung"/>

            <select name="search_category">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${param.search_category == category.id ? 'selected' : ''}>
                        <c:out value="${category.name}" />
                    </option>
                </c:forEach>
            </select>

            <script type="text/javascript">
                function handleClick(elm) {
                    let url = window.location.href.split("app/tasks/list/")[0] + "api/Tickets/status/";
                    let oSelected = document.getElementById("selected");
                    let sValue = oSelected.options[oSelected.selectedIndex].value;
                    url = url + sValue;
                    window.open(url, '_blank');
                }

            </script>

            <select name="search_status" id="selected" onchange="">
                <option name="restInhalt" value="">Alle Status</option>

                <!--Ja die Mehrzahl von Status ist nicht Stati sondern (die) Status! :)-->

                <c:forEach items="${statuses}" var="status">
                    <option value="${status}" ${param.search_status == status ? 'selected' : ''}>
                        <c:out value="${status.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
            <INPUT TYPE="BUTTON" VALUE="  Rest-Schnittstelle Status Filter" ONCLICK="handleClick()">


        </form>
        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty tasks}">
                <p>
                    Es wurden keine Serviceberichte gefunden.
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.servicebericht.common.web.WebUtils"/>

                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Eigentümer</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <c:forEach items="${tasks}" var="task">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/tasks/task/${task.id}/"/>">
                                    <c:out value="${task.shortText}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${task.category.name}"/>
                            </td>
                            <td>
                                <c:out value="${task.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${task.status.label}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>