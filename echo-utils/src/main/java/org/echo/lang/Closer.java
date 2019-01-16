package org.echo.lang;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;

/**
 * 资源释放器
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Closer {

    private Closer() {
        throw new AssertionError("No org.echo.lang.Closer instances for you!");
    }

    public static <T extends AutoCloseable> void close(T t){
        try{
            t.close();
        }catch (Exception e){
            log.error(ThrowableToString.toString(e));
        }
    }

}