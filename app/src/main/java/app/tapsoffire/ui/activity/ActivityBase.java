package app.tapsoffire.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import javax.inject.Inject;

import app.tapsoffire.TapsOfFire;
import app.tapsoffire.configuration.Config;
import app.tapsoffire.R;
import app.tapsoffire.ui.helpers.UISoundEffects;
import app.tapsoffire.utils.UIHelpers;

public abstract class ActivityBase extends Activity {

    protected static final String KEY_ACTIVITY = "app.tapsoffire.Activity:";
    protected static final String KEY_ACTIVITY_PAGE = KEY_ACTIVITY + "page";
    protected static final String KEY_ACTIVITY_STATE = KEY_ACTIVITY  + "State";

    protected static final int PAGE_MAIN = 0;

    protected static final int PAGEACTION_INITIALIZE = 0;
    protected static final int PAGEACTION_START = 1;
    protected static final int PAGEACTION_STOP = 2;
    protected static final int PAGEACTION_PAUSE = 3;
    protected static final int PAGEACTION_RESUME = 4;

    private ViewFlipper m_pageFlipper;

    @Inject protected Config config;
    @Inject protected UISoundEffects uiSoundEffects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TapsOfFire.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        uiSoundEffects.load(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        savePageFlipper(state);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isUsingPageFlipper()) {
            doPageAction(getCurrentPage(), PAGEACTION_PAUSE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUsingPageFlipper()) {
            doPageAction(getCurrentPage(), PAGEACTION_RESUME);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (isUsingPageFlipper()) {
            doPageAction(getCurrentPage(), PAGEACTION_STOP);
        }
    }

    protected boolean onBackToMainPage() {
        if (getCurrentPage() == PAGE_MAIN) {
            return true;
        }
        flipToPage(PAGE_MAIN, true);
        return false;
    }

    protected void doPageAction(int page, int action) {

    }

    protected View onCreateMenuView() {
        return null;
    }

    protected void onMenuItemClick(int id) {

    }

    protected void usePageFlipper(Bundle savedState) {
        m_pageFlipper = (ViewFlipper)findViewById(R.id.page_flipper);
        if (savedState != null) {
            int page = savedState.getInt(KEY_ACTIVITY_PAGE, PAGE_MAIN);
            flipToPage(page, false);
        }
    }

    protected boolean isUsingPageFlipper() {
        return m_pageFlipper != null;
    }

    protected int getCurrentPage() {
        return m_pageFlipper.getDisplayedChild();
    }

    protected void flipToPage(int page, boolean animate) {
        if (page == getCurrentPage()) {
            return;
        }

        m_pageFlipper.getChildAt(page).setVisibility(View.VISIBLE);
        doPageAction(page, PAGEACTION_INITIALIZE);
        doPageAction(getCurrentPage(), PAGEACTION_STOP);
        UIHelpers.flipToChild(m_pageFlipper, page, animate);
        doPageAction(page, PAGEACTION_START);
    }

    private void savePageFlipper(Bundle state) {
        if (isUsingPageFlipper()) {
            state.putInt(KEY_ACTIVITY_PAGE, getCurrentPage());
        }
    }

}
