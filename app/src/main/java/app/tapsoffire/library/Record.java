package app.tapsoffire.library;

public interface Record {

    public long getFirstPlayedTime();
    public long getLastPlayedTime();
    public Score getScore(int skill);

}
