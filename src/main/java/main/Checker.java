package main;

import errors.BadParameterFormatError;
import errors.MissingParameterError;

/**
 * Created by root on 12/15/16.
 */
public class Checker {
    public static String check(String arg, String argName) throws MissingParameterError, BadParameterFormatError {
        if (arg == null) {
            throw new MissingParameterError(argName);
        }
        if (arg.equals("")) {
            throw new BadParameterFormatError(argName);
        }
        return arg;
    }

    public static String check(String arg, String argName, int length) throws MissingParameterError, BadParameterFormatError {
        if (arg == null) {
            throw new MissingParameterError(argName);
        }
        if (arg.equals("")) {
            throw new BadParameterFormatError(argName);
        }
        if (arg.length() > length) {
            throw new BadParameterFormatError(argName);
        }
        return arg;
    }

    public static int toInt(String arg, String argName) throws MissingParameterError, BadParameterFormatError {
        check(arg, argName);
        try {
            return Integer.valueOf(arg);
        }
        catch (NumberFormatException e) {
            throw new BadParameterFormatError(argName);
        }
    }

    public static int toInt(String arg, String argName, int min, int max) throws MissingParameterError, BadParameterFormatError {
        int i = toInt(arg, argName);
        if (i >= min && i <= max) {
            return i;
        }
        throw new BadParameterFormatError(argName);
    }
}
