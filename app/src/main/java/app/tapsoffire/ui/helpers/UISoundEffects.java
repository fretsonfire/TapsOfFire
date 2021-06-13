package app.tapsoffire.ui.helpers;

import app.tapsoffire.R;
import app.tapsoffire.TapsOfFire;
import app.tapsoffire.configuration.Config;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import javax.inject.Inject;

public class UISoundEffects
{

    @Inject protected Config config;

    private SoundPool m_pool;
    private int m_inSound;
    private int m_outSound;

    public UISoundEffects() {
        TapsOfFire.getAppComponent().inject(this);
    }

    public void load(Context context) {
        if (m_pool != null) {
            return;
        }

        m_pool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        m_inSound = m_pool.load(context, R.raw.in, 1);
        m_outSound = m_pool.load(context, R.raw.out, 1);
    }

    public void destroy() {
        if (m_pool != null) {
            m_pool.release();
            m_pool = null;
        }
    }

    public void playInSound() {
        if (m_pool == null) {
            return;
        }

        float volume = config.getScaledVolume(Config.VOLUME_MENU);
        m_pool.play(m_inSound, volume, volume, 1, 0, 1);
    }

    public void playOutSound() {
        if (m_pool == null) {
            return;
        }

        float volume = config.getScaledVolume(Config.VOLUME_MENU);
        if (volume != 0) {
            m_pool.play(m_outSound, volume, volume, 1, 0, 1);
        }
    }

}
