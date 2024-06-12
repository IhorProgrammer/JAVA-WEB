import ValidationClass from "./validation/ValidationUserClass.js";


var modal;

document.addEventListener( 'DOMContentLoaded', () => {
    // шукаємо кнопку реєстрації, якщо знаходимо - додаємо обробник
    const signupButton = document.getElementById("signup-button");
    if(signupButton) { signupButton.onclick = signupButtonClick; }

    // шукаємо кнопку автентифікації, якщо знаходимо - додаємо обробник
    const authButton = document.getElementById("auth-button");
    if(authButton) { authButton.onclick = authButtonClick; }
    // налаштування модальних вікон
    var modalElement = document.querySelectorAll('.modal');
    if( modalElement != null) {
        modal = M.Modal.init(modalElement, {
            "opacity": 	    	0.5, 	// Opacity of the modal overlay.
            "inDuration": 		250, 	// Transition in duration in milliseconds.
            "outDuration": 		250, 	// Transition out duration in milliseconds.
            "preventScrolling": true,	// Prevent page from scrolling while modal is open.
            "dismissible": 		true,	// Allow modal to be dismissed by keyboard or overlay click.
            "startingTop": 		'4%',	// Starting top offset
            "endingTop": 		'10%'	// Ending top offset
        });

    }

    checkAuth();
});



function serveCartButtons() {
    // шукаємо id користувача (з його аватарки)
    const userId = document.querySelector('[data-user-id]').getAttribute('data-user-id');
    // шукаємо всі кнопки "додати до кошику" за ознакою data-product="..."
    for( let btn of document.querySelectorAll('[data-product]') ) {
        btn.onclick = () => {
            // вилучаємо id з атрибута
            let productId = btn.getAttribute('data-product');
            // при натисненні надсилаємо запит до API
            fetch(`/${getContext()}/shop-api?user-id=${userId}&product-id=${productId}`, {
                    method: 'PUT'}
            )
                .then(r => r.json())
                .then(console.log);
        }
    }
}



function getContext() {
    return window.location.pathname.split("/")[1]
}

function openModal( head, content, buttonOKFunction ) {
    if( !modal && modal.length > 0 ) return;
    const modalElement = document.querySelector('.modal');
    const headElement = modalElement.querySelector(".head");
    const contentElement = modalElement.querySelector(".content");
    const buttonOk = document.getElementById("modal1_button_ok");
    const clickHeadler = (e) => {
        buttonOKFunction(e);
        buttonOk.removeEventListener(clickHeadler);
    }
    buttonOk.addEventListener("click", clickHeadler)

    headElement.innerText = head;
    contentElement.innerText = content;
    modal[0].open();
}


function signupButtonClick(e) {
    // шукаємо форму - батьківській елемент кнопки (e.target)
    const signupForm = e.target.closest('form') ;
    if( ! signupForm ) {
        throw "Signup form not found" ;
    }
    const invalidText = document.getElementById("invalid_text");
    if( ! invalidText ) {
        throw "#invalid_text not found" ;
    }
    invalidText.textContent = "";

    // всередині форми signupForm знаходимо елементи
    const nameInput = signupForm.querySelector('input[name="user-name"]');
    if( ! nameInput ) { throw "nameInput not found" ; }
    const emailInput = signupForm.querySelector('input[name="user-email"]');
    if( ! emailInput ) { throw "emailInput not found" ; }
    const passwordInput = signupForm.querySelector('input[name="user-password"]');
    if( ! passwordInput ) { throw "passwordInput not found" ; }
    const repeatInput = signupForm.querySelector('input[name="user-repeat"]');
    if( ! repeatInput ) { throw "repeatInput not found" ; }
    const avatarInput = signupForm.querySelector('input[name="user-avatar"]');
    if( ! avatarInput ) { throw "avatarInput not found" ; }



    const vl = new ValidationClass()
    const [isFormValid, message] = vl.validationData( nameInput, emailInput, passwordInput, repeatInput, avatarInput );

    if( ! isFormValid ) {
        invalidText.textContent = "* " + message;
        return;
    }
    /// кінець валідації

    // формуємо дані для передачі на бекенд
    const formData = new FormData() ;
    formData.append( "user-name", nameInput.value ) ;
    formData.append( "user-email", emailInput.value ) ;
    formData.append( "user-password", passwordInput.value ) ;
    if( avatarInput.files.length > 0 ) {
        formData.append( "user-avatar", avatarInput.files[0] ) ;
    }

    // передаємо - формуємо запит
    fetch( window.location.href, { method: 'POST', body: formData } )
        .then( r => r.json() )
        .then( j => {
            if ( j != null ) {
                if( j.meta.status == "success" ) {
                    openModal( "Реєстрація", "Реєстрація пройшла успішно", () => {
                        window.location.href = ".";
                    } );
                }
                else  {
                    invalidText.textContent = "* " + j.meta.message;
                    passwordInput.value = "";
                    repeatInput.value = "";
                    return;
                }
            }
        }) ;
}


// function addProductButtonClick(e) {
//
//     // шукаємо форму - батьківській елемент кнопки (e.target)
//     const productForm = e.target.closest('form') ;
//     if( ! productForm ) {
//         throw "Product form not found" ;
//     }
//
//     // всередині форми signupForm знаходимо елементи
//     const nameInput = productForm.querySelector('input[name="product-name"]');
//     if( ! nameInput ) { throw "product-name not found" ; }
//     const photoInput = productForm.querySelector('input[name="product-photo"]');
//     if( ! photoInput ) { throw "product-photo not found" ; }
//
//     /// Валідація даних
//     let isFormValid = true ;
//     isFormValid = isFormValid ? validateInput(nameInput,nameInput.value != "") : false;
//     if( ! isFormValid ) return ;
//
//
//     // формуємо дані для передачі на бекенд
//     const formData = new FormData() ;
//     formData.append( "product-name", nameInput.value ) ;
//     if( photoInput.files.length > 0 ) {
//         formData.append( "product-photo", photoInput.files[0] ) ;
//     }
//
//     // передаємо - формуємо запит
//     fetch( window.location.href, { method: 'POST', body: formData } )
//         .then( r => r.json() )
//         .then( j => {
//             console.log(j);
//         } ) ;
// }


function validateInput(input, isValidate) {
    if( isValidate ) {
        input.classList.remove("invalid");
        input.classList.add("valid");
        return true;
    }
    input.classList.remove("valid");
    input.classList.add("invalid");
    return false;
}


function authButtonClick(e) {
    const emailInput = document.querySelector('input[name="auth-email"]');
    if( ! emailInput ) { throw "'auth-email' not found" ; }
    const passwordInput = document.querySelector('input[name="auth-password"]');
    if( ! passwordInput ) { throw "'auth-password' not found" ; }

    fetch(
        `/${getContext()}/auth?email=${emailInput.value}&password=${passwordInput.value}`,
        {    method: 'GET'    }
    )
        .then( r => r.json() )
        .then( j => {
            if( j.data == null || typeof  j.data.token == "undefined") {
                // пишимо що у нас прийшла помилка
                console.log( j );
            }
            else {
                localStorage.setItem("auth-token", j.data.token );
                window.location.reload();
            }
        } ) ;
}

function checkAuth() {
    const authTokem = localStorage.getItem("auth-token");
    if ( authTokem ) {
        fetch(
            `/${getContext()}/auth?token=${authTokem}`,
            {    method: 'POST'    }
        )
            .then( r => r.json() )
            .then( j => {
                if( j.meta.status == "success" ) {
                    document.querySelector('[data-auth="avatar"]').innerHTML = `<img data-user-id="${j.data.id}" title="${j.data.name}" class="nav-avatar"  src="/${getContext()}/img/avatar/${j.data.avatar}" />`
                    const product = document.querySelector('[data-auth="product"]');
                    if( product ) {
                        fetch( `/${getContext()}/product.jsp` )
                            .then( r => r.text() )
                            .then( t => {
                                product.innerHTML = t
                                document.getElementById( "add-product-button" )
                                    .addEventListener("click", addProductClick)
                            } )
                    }
                    serveCartButtons();

                }
            } ) ;
    }
}
