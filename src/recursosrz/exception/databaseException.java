package recursosrz.exception;

public class databaseException extends RuntimeException {

 public databaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public databaseException(String message) {
        super(message);
    }
}
    

