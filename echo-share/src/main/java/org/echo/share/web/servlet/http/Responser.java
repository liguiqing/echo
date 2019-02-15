package org.echo.share.web.servlet.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Getter
public class Responser {
    private boolean success = true;

    private String code = "000000";

    private String msg = "";

}