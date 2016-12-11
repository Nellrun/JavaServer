package errors;

/**
 * Created by root on 12/11/16.
 */
abstract class Error {
    protected int errorCode;
    protected String errorName;
    protected String errorText;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getErrorText() {
        return errorText;
    }
}
