package Nivel1_txt_persistance.exception;


public class InvalidDecorationType extends Exception {
    public InvalidDecorationType(String s) {
        super("Invalid decoration type. Please enter wood or plastic: ");
    }
}
