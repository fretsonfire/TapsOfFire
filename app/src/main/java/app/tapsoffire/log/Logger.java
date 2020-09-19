package app.tapsoffire.log;

public interface Logger {

    /**
     * Logs a message for the provided tag
     *
     * @param tag the logger tag
     * @param message the message
     */
    void log(String tag, String message);

    /**
     * Logs the message and throwable for the provided tag
     *
     * @param tag the logger tag
     * @param message the message
     * @param throwable the throwable
     */
    void log(String tag, String message, Throwable throwable);
}
