package app.tapsoffire.player;

import java.io.Closeable;
import java.io.IOException;

public interface PCMDecoder extends Closeable {

    void close();
    boolean isOpened();

    boolean isSeekable();
    int getRate();
    int getChannels();

    void seekToTime(int time) throws IOException;
    int read(byte[] buffer, int offset, int length) throws IOException;

}
