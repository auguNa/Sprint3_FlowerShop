package nivel3_mongodb_persistance.exception;

public class InvalidInputException extends Exception{
    public InvalidInputException(String message) {
        super("Error" + message);
    }
}