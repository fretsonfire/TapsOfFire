package app.tapsoffire.midi;

public class InvalidMidiDataException extends Exception {

    private static final long serialVersionUID = 4383604322513385272L;

    public InvalidMidiDataException() {
        super();
    }

    public InvalidMidiDataException(String message) {
        super(message);
    }

}
