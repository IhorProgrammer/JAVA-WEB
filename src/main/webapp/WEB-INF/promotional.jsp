<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page import="step.learning.dal.dto.PromotionalItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    PromotionalItem[] promotionalItems = (PromotionalItem[]) request.getAttribute("promotionals");
    String context = request.getContextPath();

%>

<h1>Акційні товари</h1>

<% for (PromotionalItem item : promotionalItems) { %>
    <div class="col s12 m7">
        <div class="card horizontal">
            <div class="card-image">
                <img src="<%=context%>/img/<%=item.getImageUrl()%>" alt="img">
            </div>
            <div class="card-stacked">
                <div class="card-content">
                    <p>Назва: <%=item.getProductName()%></p>
                    <p>Ціна по акції: <%=item.getPromotionalPrice()%></p>
                </div>
                <div class="card-action">
                    <a href="#">Купити</a>
                </div>
            </div>
        </div>
    </div>
<% } %>
