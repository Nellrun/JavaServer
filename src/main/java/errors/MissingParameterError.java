package errors;

/**
 * Created by root on 12/11/16.
 */
public class MissingParameterError extends ParameterError {

    public MissingParameterError(String parameterName) {
        this.errorCode = 1;
        this.parameterName = parameterName;
        this.errorName = "Ошибка отсуствия параметра";
        this.errorText = "Отсуствует обязательный параметр " + parameterName;
    }

}
