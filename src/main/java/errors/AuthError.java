package errors;

/**
 * Created by root on 12/11/16.
 */
public class AuthError extends Error {
    public AuthError() {
        this.errorCode = 4;
        this.errorName = "Ошибка аутентификации";
        this.errorText = "Неверно заданы параметры логин или пароль";
    }
}
