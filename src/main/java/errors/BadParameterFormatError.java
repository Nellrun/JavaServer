package errors;

/**
 * Created by root on 12/11/16.
 */
public class BadParameterFormatError extends ParameterError {

    public BadParameterFormatError(String parameterName) {
        this.errorCode = 2;
        this.errorName = "Ошибка формата";
        this.parameterName = parameterName;
        this.errorText = "Ошибка. Неверный формат параметра " + parameterName;
    }

}
