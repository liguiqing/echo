package org.echo.share.web.servlet.http;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
public interface ResponseTextFactory {
    default ResponseText lookup(String local){return new ResponseText(){};}
}