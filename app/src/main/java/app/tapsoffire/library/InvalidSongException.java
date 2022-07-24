package app.tapsoffire.library;

public class InvalidSongException extends Exception {

    public InvalidSongException(String detailMessage, Throwable throwable) {
        super(detailMessage,throwable);
    }

    public InvalidSongException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidSongException(Throwable throwable) {
        super(throwable);
    }

}
