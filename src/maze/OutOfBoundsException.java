package maze;

import java.io.Serializable;

/** Indicates that coordinates are out of range. */
public class OutOfBoundsException extends RuntimeException
        implements Serializable {

    private static final long serialVersionUID = -9142449090078209915L;

    /** Constructs an {@code OutOfBoundsException}. */
    public OutOfBoundsException() {
        super();
    }

    /**
     * Constructs an {@code OutOfBoundsException} with the specified
     * message.
     *
     * @param message the message
     */
    public OutOfBoundsException(String message) {
        super(message);
    }
}
