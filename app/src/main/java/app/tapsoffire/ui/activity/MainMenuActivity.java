package app.tapsoffire.ui.activity;

import app.tapsoffire.R;
import app.tapsoffire.configuration.Config;
import app.tapsoffire.ui.helpers.UIHelpers;
import app.tapsoffire.ui.helpers.UISoundEffects;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

public class MainMenuActivity extends ActivityBase implements View.OnClickListener {

    @Inject protected UISoundEffects uiSoundEffects;

    private MediaPlayer m_player;
    private static final int PAGE_WELCOME = 1;

    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.main_menu);
        usePageFlipper(savedState);

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);

        m_player = MediaPlayer.create(this, R.raw.menu);
        if (m_player != null) {
            m_player.setLooping(true);
        }
    }

    protected void onPause() {
        super.onPause();
        if (m_player != null) {
            m_player.pause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (m_player != null) {
            float volume = config.getScaledVolume(Config.VOLUME_MENU);
            m_player.setVolume(volume, volume);
            m_player.start();
        }

        if (getCurrentPage() == PAGE_WELCOME && !checkUpdateFirstTime(false)) {
            flipToPage(PAGE_MAIN, false);
        }

        if (getCurrentPage() == PAGE_MAIN) {
            startAnimation();
        }
    }

    protected void onMasterVolumeAdjusted() {
        if (m_player != null) {
            float volume = config.getScaledVolume(Config.VOLUME_MENU);
            m_player.setVolume(volume, volume);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private void startAnimation() {
        UIHelpers.startViewAnimation(this, R.id.logo, R.anim.logo_in);

        int offset = UIHelpers.getInteger(this, R.integer.anim_logo_in_duration);
        int delay = UIHelpers.getInteger(this, R.integer.anim_button_delay);

        UIHelpers.startViewAnimation(this, R.id.play, R.anim.button_in);
        UIHelpers.startViewAnimation(this, R.id.settings, R.anim.button_in);
        UIHelpers.startViewAnimation(this, R.id.help, R.anim.button_in, offset + delay * 2);
    }

    protected void doPageAction(int page, int action) {
        if (page == PAGE_WELCOME && action == PAGEACTION_INITIALIZE) {

        }
    }

    private boolean checkUpdateFirstTime(boolean update) {
        if (update) {
            config.setFirstTime(false);
            return false;
        } else {
            return config.isFirstTime();
        }
    }

    private void resetFirstTime() {
        config.setFirstTime(true);
    }
}
