export default class ValidationProductClass {
    static nameValid(name) {
        if( !name )             return this.returnInfo( false, "Ім'я порожнє");
        if( name.length < 2)    return this.returnInfo( false, "Ім'я менше 2 символів");
        return this.returnInfo(true, "OK");
    }

    static describeValid(describe) {
        if( !describe )             return this.returnInfo( false, "Опис порожній");
        if( describe.length < 10)   return this.returnInfo( false, "Опис менше 10 символів");
        return this.returnInfo(true, "OK");
    }

    static priceValid(price) {
        if( !price )     return this.returnInfo( false, "Ціна не може бути порожня");
        if( price < 0)   return this.returnInfo( false, "Ціна не може бути менше чим 0");
        return this.returnInfo(true, "OK");
    }

    static imageValid(imageName) {
        const regex = /^[a-zA-Z0-9\s_-]+\.(jpg|jpeg|png|webp)$/i;
        if( !regex.test(imageName) ) return this.returnInfo( false, "Не правильний формат зображення");
        return this.returnInfo(true, "OK");
    }


    static returnInfo(valid, desc) {
        return {valid: valid, desc: desc}
    }
}



