package edu.miu.cs.cs544.exception;


public class AppException extends RuntimeException{
    private String message;
    public AppException(String message) {
        super(message);
        this.message = message;
    }
}
