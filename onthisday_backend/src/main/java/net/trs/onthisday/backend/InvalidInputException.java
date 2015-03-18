package net.trs.onthisday.backend;

/**
 * Created by steve on 3/18/15.
 */
public class InvalidInputException extends Exception{

    public InvalidInputException(){

    }

    public InvalidInputException(String message){
        super(message);
    }

    public InvalidInputException(Throwable cause){
        super(cause);
    }

    public InvalidInputException(String message, Throwable cause){
        super(message, cause);
    }
}
