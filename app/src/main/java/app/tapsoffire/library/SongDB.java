package app.tapsoffire.library;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import app.tapsoffire.configuration.Config;
import app.tapsoffire.utils.Simply;
import android.content.Context;
import android.util.SparseArray;

public class SongDB {

    static {
        m_timestamp = -1;
        m_records = new SparseArray<RecordImpl>();
        m_modified = false;
    }

    private static long m_timestamp;
    private static SparseArray<RecordImpl> m_records;
    private static boolean m_modified;

    public static void load(Context context) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(getFilePath(context));
            DataInputStream dataIn = new DataInputStream(stream);
            SparseArray<RecordImpl> records = new SparseArray<RecordImpl>();

            long timestamp = dataIn.readLong();
            if (m_timestamp>=timestamp) {
                return;
            }

            int count = dataIn.readInt();
            for (int i = 0; i != count; ++i) {
                int id = dataIn.readInt();
                RecordImpl record = new RecordImpl(dataIn);
                records.append(id,record);
            }
            m_timestamp = timestamp;
            m_records = records;
            m_modified = false;
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
        finally {
            Simply.close(stream);
        }
    }

    public static void store(Context context) {
        if (!m_modified) {
            return;
        }

        OutputStream stream = null;
        try {
            stream = new FileOutputStream(getFilePath(context));
            DataOutputStream dataOut = new DataOutputStream(stream);
            dataOut.writeLong(m_timestamp);
            dataOut.writeInt(m_records.size());
            for (int i = 0; i != m_records.size(); ++i) {
                dataOut.writeInt(m_records.keyAt(i));
                m_records.valueAt(i).store(dataOut);
            }
            dataOut.flush();
            stream.flush();
            m_modified = false;
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
        finally {
            Simply.close(stream);
        }
    }

    public static Record find(int songID) {
        return m_records.get(songID);
    }

    public static Record get(int songID) {
        RecordImpl record = m_records.get(songID);
        if (record == null) {
            record = new RecordImpl();
            record.timeFirstPlayed=System.currentTimeMillis();
            m_records.put(songID,record);
            setModified();
        }

        return record;
    }

    public static void update(int songID,int skill,Score score) {
        int skillIndex = Song.skillToIndex(skill);
        if (skillIndex == -1 || score == null) {
            return;
        }

        RecordImpl record=(RecordImpl)get(songID);
        record.timeLastPlayed=System.currentTimeMillis();
        Score oldScore = record.scores[skillIndex];
        if (score.isBetter(oldScore)) {
            record.scores[skillIndex] = new Score(score);
            setModified();
        }
    }

    private static void setModified() {
        m_timestamp = System.currentTimeMillis();
        m_modified = true;
    }

    private static File getFilePath(Context context) {
        return new File(context.getFilesDir(),Config.getSongDBFileName());
    }

    public static void storeScore(DataOutput dataOut,Score score) throws IOException {
        if (score == null) {
            dataOut.writeBoolean(false);
        } else {
            dataOut.writeBoolean(true);
            dataOut.writeInt(score.score);
            dataOut.writeFloat(score.rating);
        }
    }

    public static Score loadScore(DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        } else {
            return new Score(dataIn.readInt(),dataIn.readFloat());
        }
    }

}
