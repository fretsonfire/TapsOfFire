package app.tapsoffire.library;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import app.tapsoffire.utils.DataStreamHelpers;
import android.content.res.AssetManager;

public class SongInfo implements SongConfig {

    private SongIni m_ini;
    private File m_filesPath;
    private File m_assetPath;

    private File m_guitarFile;
    private File m_songFile;

    private int m_selectedSkill = Song.INVALID_SKILL;

    private static final int DATA_TAG = 0x53494E46;
    public static final String BUNDLE_KEY = "app.tapsoffire.SongInfo";

    public SongInfo(File songPath) throws InvalidSongException {
        m_ini = new SongIni(songPath);
        setFilesPath(songPath);
    }

    public SongInfo(AssetManager assets,File songPath) throws InvalidSongException {
        m_ini = new SongIni(assets,songPath);
        m_assetPath = songPath;
    }

    public SongInfo(SongInfo other) {
        m_ini = new SongIni(other.m_ini);
        m_filesPath = other.m_filesPath;
        m_assetPath = other.m_assetPath;
        m_guitarFile = other.m_guitarFile;
        m_songFile = other.m_songFile;
        m_selectedSkill = other.m_selectedSkill;
    }

    public SongInfo(DataInput dataIn) throws IOException {
        DataStreamHelpers.checkTag(dataIn,DATA_TAG);
        m_ini = new SongIni(dataIn);
        m_filesPath = restoreFile(dataIn);
        m_assetPath = restoreFile(dataIn);
        m_guitarFile = restoreFile(dataIn);
        m_songFile = restoreFile(dataIn);
        m_selectedSkill = dataIn.readInt();
    }

    public void saveState(DataOutput dataOut) throws IOException {
        dataOut.writeInt(DATA_TAG);
        m_ini.saveState(dataOut);
        saveFile(dataOut,m_filesPath);
        saveFile(dataOut,m_assetPath);
        saveFile(dataOut,m_guitarFile);
        saveFile(dataOut,m_songFile);
        dataOut.writeInt(m_selectedSkill);
    }

    public SongIni getIni() {
        return m_ini;
    }

    public final int getID() {
        return getIni().getID();
    }

    public final String getName() {
        return getIni().getName();
    }

    public final String getArtist() {
        return getIni().getArtist();
    }

    public final int getSkills() {
        return getIni().getSkills();
    }

    public boolean isAsset() {
        return m_assetPath != null;
    }

    public File getAssetPath() {
        return m_assetPath;
    }

    public File getFilesPath() {
        return m_filesPath;
    }

    public void setFilesPath(File path) {
        m_filesPath = path;
        m_guitarFile = new File(path,SongIni.GUITAR_FILE);
        m_songFile = new File(path,SongIni.SONG_FILE);
    }

    public File getNotesFile() {
        return new File(m_filesPath,SongIni.NOTES_FILE);
    }

    public File getGuitarFile() {
        return m_guitarFile;
    }

    public void setGuitarFile(File file) {
        m_guitarFile=file;
    }

    public File getSongFile() {
        return m_songFile;
    }

    public void setSongFile(File file) {
        m_songFile=file;
    }

    public int getSelectedSkill() {
        return m_selectedSkill;
    }
    public void setSelectedSkill(int skill) {
        m_selectedSkill=skill;
    }

    public String getErrorDetails() {
        return String.format(
                "Song info:\n"+
                        "  Name: %s\n"+
                        "  Artist: %s\n"+
                        "  Song file: %s\n"+
                        "  Guitar file: %s\n",
                getName(),
                getArtist(),
                m_songFile != null ? m_songFile.getPath() : "null",
                m_guitarFile != null ? m_guitarFile.getPath() : "null"
        );
    }

    private static void saveFile(DataOutput dataOut,File file) throws IOException {
        if (file == null) {
            dataOut.writeBoolean(false);
        } else {
            dataOut.writeBoolean(true);
            dataOut.writeUTF(file.getPath());
        }
    }

    private static File restoreFile(DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        } else {
            return new File(dataIn.readUTF());
        }
    }
}
