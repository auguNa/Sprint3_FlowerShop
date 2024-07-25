package nivel3_mongodb_persistance.exception;

public class InvalidDecorationTypeException extends Exception {
    public InvalidDecorationTypeException(String s) {
        super("Invalid decoration type. Please enter wood or plastic: ");
    }
}