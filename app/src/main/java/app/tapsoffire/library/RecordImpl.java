package app.tapsoffire.library;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RecordImpl implements Record {

    public long timeFirstPlayed;
    public long timeLastPlayed;
    public Score[] scores = new Score[Song.SKILL_COUNT];

    public RecordImpl() {
    }

    public RecordImpl(DataInput dataIn) throws IOException {
        load(dataIn);
    }

    public long getFirstPlayedTime() {
        return timeFirstPlayed;
    }

    public long getLastPlayedTime() {
        return timeLastPlayed;
    }

    public Score getScore(int skill) {
        int skillIndex = Song.skillToIndex(skill);
        if (skillIndex == -1) {
            return null;
        }
        Score score = scores[skillIndex];
        if (score == null) {
            return null;
        } else {
            return new Score(score);
        }
    }

    public void store(DataOutput dataOut) throws IOException {
        dataOut.writeLong(timeFirstPlayed);
        dataOut.writeLong(timeLastPlayed);
        for (int i=0;i!=Song.SKILL_COUNT;++i) {
            SongDB.storeScore(dataOut,scores[i]);
        }
    }

    public void load(DataInput dataIn) throws IOException {
        timeFirstPlayed=dataIn.readLong();
        timeLastPlayed=dataIn.readLong();
        for (int i = 0; i != Song.SKILL_COUNT; ++i) {
            scores[i] = SongDB.loadScore(dataIn);
        }
    }

}
