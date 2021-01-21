package app.tapsoffire.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import app.tapsoffire.utils.MathHelpers;
import app.tapsoffire.utils.UIHelpers;

@Singleton
public class Config {

    private static class Name {
        static final String MODIFICATION_TIME = "modificationTime";
        static final String VOLUME_ = "volume#";
        static final String NOTES_DELAY = "notesDelay";
        static final String SONG_CACHE_LENGTH = "songCacheLength";
        static final String EARLY_PICK_MARGIN = "earlyPickMargin";
        static final String LATE_PICK_MARGIN = "latePickMargin";
        static final String REPICK_MARGIN = "repickMargin";
        static final String MIN_NOTES_DISTANCE = "minNotesDistance";
        static final String TARGET_FPS = "targetFPS";
        static final String TOUCH_HANDLER_SLEEP = "touchHandlerSleep";
        static final String SHOW_DEBUG_INFO = "showDebugInfo";

        static final String LEAK_CANARY = "leakCanary";
    }

    @NonNull private final SharedPreferences prefs;
    @NonNull private final AudioManager audio;

    private static final String PREFERENCES = "settings";

    private final int VOLUME_MENU = 0;
    private final int VOLUME_SONG = 1;
    private final int VOLUME_GUITAR = 2;
    private final int VOLUME_SCREWUP = 3;
    private final int COUNTOF_VOLUMES = 4;

    private float m_masterVolume;
    private static boolean m_masterVolumeFixed;
    private float[] m_absoluteVolumes = new float[COUNTOF_VOLUMES];
    private final float[] DEFAULT_ABSOLUTE_VOLUMES = {
            0.2f,0.7f,0.7f,0.2f
    };

    private final File rootPath;

    // typeface
    private Typeface m_fireTypeface;
    private Typeface m_defaultTypeface;

    @Inject
    Config(@NonNull final Context context) {
        prefs = context.getSharedPreferences(PREFERENCES, 0);
        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // initialize volume stuff volume
        loadMasterVolume();
        fixMasterVolume(false);
        loadAbsoluteVolumes();

        // setup root path
        rootPath = context.getExternalFilesDir(null);
    }

    private void putBoolean(@NonNull String name, boolean value) {
        prefs.edit().putBoolean(name, value).apply();
    }

    private void putInt(@NonNull String name, int value) {
        prefs.edit().putInt(name, value).apply();
    }

    private void putFloat(@NonNull String name, float value) {
        prefs.edit().putFloat(name, value).apply();
    }

    private void putLong(@NonNull String name, long value) {
        prefs.edit().putLong(name, value).apply();
    }

    private void putString(@NonNull String name, @Nullable String value) {
        prefs.edit().putString(name, value).apply();
    }

    private void putStringSet(@NonNull String name, Set<String> value) {
        prefs.edit().putStringSet(name, value).apply();
    }

    public void fixMasterVolume(boolean force) {
        if ((!m_masterVolumeFixed || force) && getMasterVolume() < 0.3f) {
            setMasterVolume(0.3f);
        }

        m_masterVolumeFixed = true;
    }

    public void loadMasterVolume() {
        int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if (maxVolume == 0) {
            m_masterVolume = 0;
        } else {
            m_masterVolume =  (float) volume/maxVolume;
        }
    }

    public void setMasterVolume(float volume) {
        m_masterVolume = Math.min(Math.max(0, volume), 1);

        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, MathHelpers.round(volume * maxVolume), 0);
    }

    public float getMasterVolume() {
        return m_masterVolume;
    }

    public float getAbsoluteVolume(int volumeIndex) {
        if (volumeIndex < 0 || volumeIndex >= COUNTOF_VOLUMES) {
            return 0;
        }

        return m_absoluteVolumes[volumeIndex];
    }

    public void setAbsoluteVolume(int volumeIndex, float volume) {
        if (volumeIndex < 0 || volumeIndex >= COUNTOF_VOLUMES) {
            return;
        }

        volume = Math.min(Math.max(0, volume), 1);
        m_absoluteVolumes[volumeIndex] = volume;
        setModificationTime();
    }

    public float getScaledVolume(int volumeIndex) {
        if (volumeIndex < 0 || volumeIndex >= COUNTOF_VOLUMES) {
            return 0;
        }

        if (m_masterVolume == 0) {
            return 0;
        }

        return Math.min(m_absoluteVolumes[volumeIndex]/m_masterVolume, 1);
    }

    public File getBuiltInSongsPath() {
        return new File("songs");
    }

    public File getSongsPath() {
        return new File(rootPath, "songs");
    }

    public String getSongDBFileName() {
        return "songs.db";
    }

    public File getCachePath() {
        return new File(rootPath, "cache");
    }

    private void loadResources(Context context) {
        m_fireTypeface = UIHelpers.getTypeface(context, "fonts/title.ttf");
        m_defaultTypeface = UIHelpers.getTypeface(context, "fonts/default.ttf");
    }

    public Typeface getFireTypeface() {
        return m_fireTypeface;
    }

    public Typeface getDefaultTypeface() {
        return m_defaultTypeface;
    }

    public void setModificationTime() {
        putLong(Name.MODIFICATION_TIME, new Date().getTime());
    }

    public long getModificationTime() {
        return prefs.getLong(Name.MODIFICATION_TIME, 0);
    }

    public void loadAbsoluteVolumes() {
        for (int i = 0; i != COUNTOF_VOLUMES; ++i) {
            m_absoluteVolumes[i] = prefs.getFloat(
                    Name.VOLUME_+i,
                    DEFAULT_ABSOLUTE_VOLUMES[i]
            );
        }
    }

    public float[] getAbsoluteVolumes() {
        return m_absoluteVolumes;
    }

    public void setAbsoluteVolumes() {
        for (int i = 0; i != COUNTOF_VOLUMES; ++i) {
            putFloat(Name.VOLUME_+i, m_absoluteVolumes[i]);
        }
    }

    public void setUseLeakyCanary(boolean useLeakyCanary) {
        putBoolean(Name.LEAK_CANARY, useLeakyCanary);
    }

    public boolean getUseLeakyCanary() {
        return prefs.getBoolean(Name.LEAK_CANARY, false);
    }


}
