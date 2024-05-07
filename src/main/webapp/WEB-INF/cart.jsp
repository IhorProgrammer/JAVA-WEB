<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    CartItem[] cartItems = (CartItem[]) request.getAttribute("carts");
    String context = request.getContextPath();

%>

<h1>Мій кошик</h1>

<% for (CartItem item : cartItems) { %>
<div class="col s12 m7">
    <div class="card horizontal">
        <div class="card-image">
            <img src="<%=context%>/img/NoImage.png" alt="img">
        </div>
        <div class="card-stacked">
            <div class="card-content">
                <p><%=item.getId()%></p>
                <p><%=item.getProductId()%></p>
                <p><%=item.getCount()%></p>
            </div>
            <div class="card-action">
                <a href="#">Видалити з кошику</a>
            </div>
        </div>
    </div>
</div>
<% } %>
