package Nivel1_txt_persistance.exception;

public class InvalidInputException extends Exception{
    public InvalidInputException(String message) {
        super("Error" + message);
    }
}
