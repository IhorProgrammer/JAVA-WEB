<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String context = request.getContextPath();
%>
<h1>Додати новий товар</h1>

<form method="post" id="add-product-form">
    <div class="row">
        <div class="col s12">
            <div class="row">

                <div class="input-field col s12 m6">
                    <input id="product-name" name="product-name" type="text" class="validate">
                    <label for="product-name">Назва товару</label>
                    <span class="helper-text" data-error="wrong"></span>
                </div>

                <div class="input-field col s12 m6">
                    <input id="product-price" name="product-price" type="number" class="validate">
                    <label for="product-price">Ціна товару</label>
                    <span class="helper-text" data-error="wrong"></span>
                </div>

                <div class="input-field col s12 m12">
                    <textarea id="product-description" name="product-description" class="materialize-textarea"></textarea>
                    <label for="product-description">Опис товару</label>
                    <span class="helper-text" data-error="wrong"></span>
                </div>

                <div class="file-field input-field col s12 m6">
                    <div class="btn  purple accent-4">
                        <span>Зображення товару</span>
                        <input id="product-img" name="product-img" type="file">
                    </div>
                    <div class="file-path-wrapper">
                        <input  id="product-img-path" class="file-path validate" type="text">
                        <span class="helper-text" data-error="wrong"></span>
                    </div>

                </div>

            </div>

        </div>

    </div>
    <div class="row">
        <div class="col s12 m12 center ">
            <button type="button" class="btn  purple accent-4" id="add-product-button">Зберегти</button>
            <div id="add-product-result"></div>
        </div>
    </div>
</form>

<script type="module" src="<%=context%>/js/shop.js"></script>