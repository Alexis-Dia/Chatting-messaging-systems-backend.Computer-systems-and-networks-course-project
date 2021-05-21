package exceptions;

public class UserStatusNotFoundException  extends RuntimeException {

    public UserStatusNotFoundException() {
    }

    public UserStatusNotFoundException(String message) {
        super(message);
    }

    public UserStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserStatusNotFoundException(Throwable cause) {
        super(cause);
    }
}

