document.addEventListener( 'DOMContentLoaded', () => {
    const authToken = localStorage.getItem("auth-token");
    getCartUser(authToken);
});

function getContext() {
    return window.location.pathname.split("/")[1]
}

function getCartUser( authToken ) {
    if ( authToken ) {
        fetch(
            `/${getContext()}/cart?token=${authToken}`,
            {
                method: 'POST'
            }
        )
            .then( r => r.json() )
            .then( j => {
                if( j.meta.status === "200") {
                     const userCart = document.getElementById("user_cart");
                     userCart.setAttribute("cart-id", j.data.id );

                     j.data.cart_items.forEach( (item) => {
                         const cartItemHTML = CartItemToHTML( item );
                         userCart.appendChild(cartItemHTML);
                     } )
                }
                else {
                    console.error(j.meta)
                }
            } ) ;
    }
}


function CartItemToHTML(cartItem) {
    const div = document.createElement("div")
    div.className = "col s12 m7";
    div.innerHTML = `
            <div class="card small">
                <div class="card-image">
                    <img src="img/products/${cartItem.image}" alt="img">
                </div>
                <div class="card-stacked">
                    <div class="card-content">
                        <p>Назва: ${cartItem.productName}</p>
                        <p>Кількість: ${cartItem.count}</p>
                    </div>
                    <div class="card-action">
                        <a data-cart-product="${cartItem.productId}">Видалити з кошику</a>
                    </div>
                </div>
            </div>
    `;
    return div;
}
