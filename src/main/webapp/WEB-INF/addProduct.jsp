<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Додати продукт</h1>
<div class="row">
    <form class="col s12" method="post">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">local_cafe</i>
                <input id="icon_prefix" type="text" name="product-name"
                       class="" >
                <label for="icon_prefix">Апельсин</label>
                <span class="helper-text"
                      data-error="Це необхідне поле"
                      data-success="Правильно">Назва товару</span>
            </div>
            <div class="file-field input-field col s6">
                <div class="btn pink darken-3">
                    <i class="material-icons">image</i>
                    <input type="file" name="product-photo">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" placeholder="Фото">
                </div>
            </div>
        </div>
        <div class="input-field col s6">
            <button type="button" id="add-product-button" class="btn pink darken-3"><i class="material-icons left">task_alt</i>Додати</button>
        </div>
    </form>
</div>