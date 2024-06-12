<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page import="step.learning.models.CartPageModel" %>
<%@ page import="step.learning.dal.dto.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
//    CartItem[] cartItems = (CartItem[]) request.getAttribute("carts");
    CartPageModel model = (CartPageModel) request.getAttribute("model");
    String context = request.getContextPath();

%>

<div class="row">
    <div class="col s8">
        <div class="row">
        <% for(Product product : model.getProducts() ) { %>
            <div class="col s6 m4 l3">
                <div class="card medium">
                    <div class="card-image">
                        <img src="<%=context%>/img/products/<%=product.getImage() == null ? "no_image.png" : product.getImage()%>" alt="Image">
                        <a data-product="<%=product.getId().toString()%>"  class="product-cart-btn btn-floating halfway-fab waves-effect waves-light red">
                            <i class="material-icons">shopping_cart</i>
                        </a>
                    </div>
                    <div class="card-content">
                        <span class="card-title"><%=product.getName()%></span>
                        <p><%=product.getDescription()%></p>
                    </div>
                </div>
            </div>
        <% } %>
        </div>
    </div>
    <div class="col s4" id="user_cart">
        <h1>Мій кошик</h1>
<%--        <% for (CartItem item : model.getCartItems()) { %>--%>
<%--        <div class="col s12 m7">--%>
<%--            <div class="card horizontal">--%>
<%--                <div class="card-image">--%>
<%--                    <img src="<%=context%>/img/no_image.png" alt="img">--%>
<%--                </div>--%>
<%--                <div class="card-stacked">--%>
<%--                    <div class="card-content">--%>
<%--                        <p><%=item.getId()%></p>--%>
<%--                        <p><%=item.getProductId()%></p>--%>
<%--                        <p><%=item.getCount()%></p>--%>
<%--                    </div>--%>
<%--                    <div class="card-action">--%>
<%--                        <a href="#">Видалити з кошику</a>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <% } %>--%>
    </div>
</div>


<script src="<%=context%>/js/cart.js"></script>
