<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- JSTL 3.0（Tomcat 10 / Jakarta EE）的 core 标签库 URI 是 jakarta.tags.core --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>班级分页查询</title>
    <style>
        table { border-collapse: collapse; margin: 16px 0; }
        th, td { border: 1px solid #333; padding: 6px 16px; }
        .disabled { color: #aaa; pointer-events: none; }
        a { margin: 0 8px; }
    </style>
</head>
<body>
<h3>班级分页查询</h3>
<%-- 循环遍历后端 request 域里的集合渲染表格 --%>
<table>
    <tr>
        <th>序号</th>
        <th>班级编号</th>
        <th>班级名称</th>
        <th>班主任编号</th>
    </tr>
    <c:forEach items="${clazzes}" var="clazz" varStatus="st">
        <tr>
            <td>${st.index + 1}</td>
            <td>${clazz.cno}</td>
            <td>${clazz.cname}</td>
            <td>${clazz.tid}</td>
        </tr>
    </c:forEach>
</table>

<p>
    共 ${totalRecord} 条记录，共 ${totalPage} 页，
    当前第 ${pageNum} 页，每页 ${pageSize} 条
</p>

<%-- 超链接必须用 pageContext.request.contextPath 拼上下文路径，否则路径错误 --%>
<%-- 第一页置灰上一页，最后一页置灰下一页（边界判断） --%>
<p>
    <c:choose>
        <c:when test="${pageNum <= 1}">
            <a class="disabled">上一页</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/queryClazzes?pageNumber=${pageNum - 1}&pageSize=${pageSize}">上一页</a>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${pageNum >= totalPage}">
            <a class="disabled">下一页</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/queryClazzes?pageNumber=${pageNum + 1}&pageSize=${pageSize}">下一页</a>
        </c:otherwise>
    </c:choose>
</p>
</body>
</html>
