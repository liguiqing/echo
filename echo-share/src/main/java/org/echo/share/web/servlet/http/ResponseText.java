package org.echo.share.web.servlet.http;

/**
 * 响应文本
 * @author Liguiqing
 * @since V1.0
 */

public interface ResponseText {
    default String getText(String code){return code;}
}