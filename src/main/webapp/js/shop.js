import ValidationProductClass from "./validation/ValidationProductClass.js";

document.addEventListener( 'DOMContentLoaded', () => {
    const addProductButton = document.getElementById("add-product-button");
    if(addProductButton) { addProductButton.onclick = addProductClick; }
});

function addProductClick(e) {
    // Збираємо дані з форми додавання продукту
    const form = e.target.closest('form');
    if( !form ) throw "#form is not found";

    const nameInput = form.querySelector("#product-name");
    if( !nameInput ) throw "#product-name is not found";

    const priceInput = form.querySelector("#product-price");
    if( !priceInput ) throw "#product-price is not found";

    const descriptionInput = form.querySelector("#product-description");
    if( !descriptionInput ) throw "#product-description is not found";

    const fileInput = form.querySelector("#product-img");
    if( !fileInput ) throw "#product-img is not found";
    // валідація
    const file = fileInput.files[0];


    const nameValid = ValidationProductClass.nameValid( nameInput.value.trim() );
    inputFieldValidation(nameValid.valid, nameInput, nameValid.desc);
    const priceValid = ValidationProductClass.priceValid( priceInput.value.trim() );
    inputFieldValidation(priceValid.valid, priceInput, priceValid.desc);
    const descriptionValid = ValidationProductClass.describeValid( descriptionInput.value);
    inputFieldValidation(descriptionValid.valid, descriptionInput, descriptionValid.desc);
    let fileValid = null;
    if( file ) {
        fileValid = ValidationProductClass.imageValid( file.name );
        const inputField = fileInput.closest(".input-field");
        if( !inputField ) throw ".input-field not found";
        const inputValidate = inputField.querySelector(".validate");
        if( inputValidate )
            inputFieldValidation(fileValid.valid, inputValidate, fileValid.desc);
    }

    const formIsValid = (fileValid?true:fileValid.valid) && priceValid.valid && descriptionValid.valid && fileValid.valid ;
    if( formIsValid !== true ) return;

    // Формуємо дані для передачі на сервер
    const formData = new FormData();
    formData.append("name", nameInput.value.trim());
    formData.append("price", priceInput.value.trim());
    formData.append("description", descriptionInput.value );
    formData.append("image", file);
    formData.append("token", localStorage.getItem("auth-token"));

    //надсилаємо дані
    fetch(
        `/${window.location.pathname.split("/")[1]}/shop-api`,
        {
            method: "POST",
            body: formData
        }
    )
        .then( r => r.json() )
        .then( t => {
            console.log(t);
        } )
}

function inputFieldValidation( isValid, input, textHelper ) {
    if( !input ) throw ".input-field is null";

    const inputField = input.closest(".input-field");
    if( !inputField ) throw ".input-field not found";

    const helperText = inputField.querySelector(".helper-text");
    if( helperText ) {
        if( !isValid ) helperText.setAttribute("data-error", textHelper);
    }

    if( isValid ) {
        input.classList.add("valid")
        input.classList.remove("invalid")
    }
    else {
        input.classList.add("invalid")
        input.classList.remove("valid")
    }

}