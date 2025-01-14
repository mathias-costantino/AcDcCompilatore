package parser;

public class SyntacticException extends Exception {
    
    public SyntacticException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public SyntacticException(String message) {
        super(message);
    }
}