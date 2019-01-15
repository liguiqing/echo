package org.echo.test;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class PrivateConstractorBean {

    private PrivateConstractorBean(){
        throw new AssertionError("No org.echo.test.TestBean instances for you!");
    }
}