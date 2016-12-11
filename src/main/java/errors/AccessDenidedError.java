package errors;

/**
 * Created by root on 12/11/16.
 */
public class AccessDenidedError extends Error {
    public AccessDenidedError() {
        this.errorCode = 5;
        this.errorName = "Ошибка доступа";
        this.errorText = "Доступ запрещен";
    }
}
