package org.saavatech.inventorymgtsystem.exceptions;

public class NameValueRequiredException extends RuntimeException{
    public NameValueRequiredException(String message){
        super(message);
    }
}
