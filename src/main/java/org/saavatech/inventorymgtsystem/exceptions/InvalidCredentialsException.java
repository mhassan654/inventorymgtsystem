package org.saavatech.inventorymgtsystem.exceptions;

public class InvalidCredentialsException extends  RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
