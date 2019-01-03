package org.echo.exception;

import org.echo.lang.Closer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception to String
 *
 * @author Liguiqing
 * @since V1.0
 */

public class ThrowableToString {

    private ThrowableToString(){
        throw new IllegalArgumentException();
    }

    public static String toString(Throwable t) {
        if (t == null)
            return "Throwable is null";
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        t.printStackTrace(pwriter);
        String s = swriter.toString();
        Closer.close(pwriter);
        return s;
    }

    public static String toHtml(Throwable t) {
        String msg = toString(t);
        return "<div class='exception'><pre>".concat(msg.replaceAll("\n\t", "<br/>")).concat("</pre></div>");
    }

    public static String cleanExceptionString(Throwable t) {
        String msg = t.getMessage();
        if(msg != null && msg.contains("Exception")) {
            return msg.substring(msg.indexOf("Exception:") + "Exception:".length());
        }else {
            return msg;
        }
    }

    public static String toString(Exception t) {
        if (t == null)
            return "Exception is null";

        return toString(t.getCause() == null ? t : t.getCause());
    }
}