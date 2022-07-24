package app.tapsoffire.library;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import app.tapsoffire.utils.DataStreamHelpers;

public class FinishedSongInfo extends SongInfo {

    private int m_score;
    private int m_longestStreak;
    private float m_accuracy;

    public static final String BUNDLE_KEY = "app.tapsoffire.FinishedSongInfo";
    private static final int DATA_TAG = 0x46534E46;

    public FinishedSongInfo(SongInfo info) {
        super(info);
    }

    public FinishedSongInfo(DataInput dataIn) throws IOException {
        super(dataIn);
        DataStreamHelpers.checkTag(dataIn,DATA_TAG);
        m_score = dataIn.readInt();
        m_longestStreak = dataIn.readInt();
        m_accuracy = dataIn.readFloat();
    }

    public void saveState(DataOutput dataOut) throws IOException {
        super.saveState(dataOut);
        dataOut.writeInt(DATA_TAG);
        dataOut.writeInt(m_score);
        dataOut.writeInt(m_longestStreak);
        dataOut.writeFloat(m_accuracy);
    }

    public int getScore() {
        return m_score;
    }

    public void setScore(int score) {
        m_score = score;
    }

    public int getLongestStreak() {
        return m_longestStreak;
    }

    public void setLongestStreak(int streak) {
        m_longestStreak = streak;
    }

    public float getAccuracy() {
        return m_accuracy;
    }

    public void setAccuracy(float accuracy) {
        m_accuracy = accuracy;
    }

}
