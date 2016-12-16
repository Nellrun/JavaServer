package errors;

/**
 * Created by root on 12/11/16.
 */


public class ParameterError extends Error {
    protected String parameterName;

    public String getParameterName() {
        return parameterName;
    }
}
