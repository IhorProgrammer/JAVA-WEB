export default class ValidationError extends Error {
    constructor(message, fieldName) {
        super(message);  // Виклик конструктора базового класу Error
        this.name = "ValidationError";  // Зміна імені помилки
        this.fieldName = fieldName;
        this.status = 400;  // Ви можете додати статус код, якщо це помилка HTTP
    }
}