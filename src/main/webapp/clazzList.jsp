<%@ page import="lwy.study.mybatis.pojo.Clazz" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>班级分页</title>
    <style>
        table { border-collapse: collapse; width: 600px; }
        td, th { border: 1px solid #999; padding: 6px; text-align: center; }
    </style>
</head>
<body>

<%
    List<Clazz> clazzes = (List<Clazz>) request.getAttribute("clazzes");
    Integer pageNum = (Integer) request.getAttribute("pageNum");
    Integer pageSize = (Integer) request.getAttribute("pageSize");
    if (pageNum == null) {
        pageNum = 1;
    }
    if (pageSize == null) {
        pageSize = 5;
    }
%>

<table>
    <tr>
        <th>班级编号</th>
        <th>班级号</th>
        <th>班级名</th>
        <th>班主任id</th>
    </tr>
    <%
        if (clazzes != null) {
            for (Clazz clazz : clazzes) {
    %>
    <tr>
        <td><%= clazz.getCid() %></td>
        <td><%= clazz.getCno() %></td>
        <td><%= clazz.getCname() %></td>
        <td><%= clazz.getTid() %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<br/>

<%-- 翻页链接要用 request.getContextPath() 拼上项目路径，不然会 404 --%>
<a href="<%= request.getContextPath() %>/queryClazzes?pageNum=<%= pageNum - 1 %>&pageSize=<%= pageSize %>">上一页</a>

<a href="<%= request.getContextPath() %>/queryClazzes?pageNum=<%= pageNum + 1 %>&pageSize=<%= pageSize %>">下一页</a>

</body>
</html>
