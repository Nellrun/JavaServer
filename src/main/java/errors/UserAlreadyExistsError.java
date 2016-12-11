package errors;

/**
 * Created by root on 12/11/16.
 */
public class UserAlreadyExistsError extends Error {

    public UserAlreadyExistsError() {
        this.errorCode = 3;
        this.errorName = "Ошибка существующего пользователя";
        this.errorText = "Ошибка. Данный пользователь уже существует.";
    }
}
