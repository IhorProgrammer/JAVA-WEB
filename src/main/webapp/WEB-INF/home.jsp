<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Домашня сторінка</h1>
<p>
    Контроль хеш-сервісу: <%=request.getAttribute("hash")%>
</p>
<p>
    Контроль db-сервісу: <%=request.getAttribute("db")%>
</p>