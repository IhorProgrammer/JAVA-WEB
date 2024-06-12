import ValidationError from "./ValidationError.js";

export default class ValidationUserClass {
    _name= "";
    _email = "";
    _password = "";
    _repeat = "";
    _avatar = ""


    validationData(name, email, password, repeat, avatar) {
        try {
            this.name = name.value; ValidationClass.inputValid( name, true );
            this.email = email.value; ValidationClass.inputValid( email, true );
            this.password = password.value; ValidationClass.inputValid( password, true );
            this.repeat = repeat.value; ValidationClass.inputValid( repeat, true );
            this.avatar = avatar.value; ValidationClass.inputValid( avatar, true );
        } catch ( er ) {
            if (er instanceof ValidationError) {
                let InvalidInput = null;
                switch ( er.fieldName ) {
                    case "name" : InvalidInput = name; break;
                    case "email" : InvalidInput = email; break;
                    case "password" : InvalidInput = password; break;
                    case "repeat" : InvalidInput = repeat; break;
                    case "avatar" : InvalidInput = avatar; break;
                }
                ValidationClass.inputValid( InvalidInput, false );
                return [false, er.message]
            }
        }
        return [true, "Validation successful"];
    }

    static inputValid ( input, isValid ) {
        if( isValid ) {
            input.classList.remove("invalid");
            input.classList.add("valid");
        }
        else {
            input.classList.remove("valid");
            input.classList.add("invalid");
        }
    }
    get name() {
        return this._name;
    }
    set name(value) {
        this._name = value;
        const regex = new RegExp("^[a-zA-Zа-яА-ЯіІїЇєЄ ']{2,50}$");
        switch (true) {
            case value === "": case value.length < 2:
                throw new ValidationError( "ім'я не може бути меньше 2 символів" , "name" );
            case !regex.test(value):
                throw new ValidationError( "ім'я не може включати цифри, або симовли іншого алфавіту", "name" );
        }
    }

    get email() {
        return this._email;
    }
    set email(value) {
        this._email = value;
        switch (true) {
            case value == "": case !value:
                throw new ValidationError( "email не може бути пустим" , "email");
            case /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value):
                throw new ValidationError ( "не правильно введена почта example@example.com" , "email");
        }
    }

    get repeat() {
        return this._repeat;
    }
    set repeat(value) {
        this._repeat = value;
        if( this._repeat !== this.password ) {
            throw new ValidationError("паролі не співпадають", "repeat");
        }
    }

    get password() {
        return this._password;
    }
    set password(value) {
        this._password = value;

        switch (true) {
            case value.length < 8:
                throw new ValidationError("Пароль повинен містити щонайменше 8 символів.", "password");
            case !/[A-Z]/.test(value):
                throw new ValidationError("Пароль повинен містити щонайменше одну велику літеру.", "password");
            case !/[a-z]/.test(value):
                throw new ValidationError("Пароль повинен містити щонайменше одну малу літеру.", "password");
            case !/[0-9]/.test(value):
                throw new ValidationError("Пароль повинен містити щонайменше одну цифру.", "password");
            case !/[\!\@\#\$\%\^\&\*\(\)\_\+\-\=]/.test(value):
                throw new ValidationError("Пароль повинен містити щонайменше один спеціальний символ, наприклад !, @, #.", "password");
        }
    }

    get avatar() {
        return this._avatar;
    }
    set avatar(value) {
        this._avatar = value;
    }
}

