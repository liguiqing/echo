package org.echo.lock;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class LockFailureException extends RuntimeException {

    public LockFailureException(){
        super("Lock Failure");
    }

    public LockFailureException(String message){
        super(message);
    }
}