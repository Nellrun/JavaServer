package errors;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 12/11/16.
 */
abstract class Error extends Throwable{
    protected int errorCode;
    protected String errorName;
    protected String errorText;
//    transient private StackTraceElement[] stackTrace;
//    transient protected Throwable[] suppressedExceptions;


    public int getErrorCode() { return errorCode; }

    public String getErrorName() {
        return errorName;
    }

    public String getErrorText() {
        return errorText;
    }
}
