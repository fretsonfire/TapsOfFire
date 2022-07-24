package app.tapsoffire.library;

import java.io.File;

public interface SongConfig {

    public SongIni getIni();

    public File getNotesFile();
    public File getGuitarFile();
    public File getSongFile();

}
