package rk.utils.reflection.exception;

public class FieldNotFoundError extends RuntimeException {
    public FieldNotFoundError(String fieldName) {
        super("field with name " + fieldName + " wasn't found");
    }
}