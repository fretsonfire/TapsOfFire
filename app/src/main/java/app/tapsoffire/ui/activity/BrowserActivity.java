package app.tapsoffire.ui.activity;

import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import app.tapsoffire.R;

public class BrowserActivity extends ActivityBase implements ListAdapter, OnItemClickListener {

    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.browser);
    }

}
