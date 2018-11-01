package org.echo.commons.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception to String
 *
 * @author Liguiqing
 * @since V1.0
 */

public class ThrowableToString {

    public static String toString(Throwable t) {
        if (t == null)
            return "Throwable is null";
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        t.printStackTrace(pwriter);
        String s = swriter.toString();
        pwriter.close();
        return s;
    }

    public static String toHtml(Throwable t) {
        String msg = toString(t);
        return msg.replaceAll("\n\t", "<br/>");
    }

    public static String cleanExceptionString(Throwable t) {
        String msg = t.getMessage();
        if(msg != null && msg.contains("Exception")) {
            return msg.substring(msg.indexOf("Exception:")+"Exception:".length(),msg.length());
        }else {
            return msg;
        }
    }

    public static String toString(Exception t) {
        return toString(t.getCause() == null ? t : t.getCause());
    }
}