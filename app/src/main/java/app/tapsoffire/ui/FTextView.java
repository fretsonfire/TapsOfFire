package app.tapsoffire.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;


import javax.inject.Inject;

import app.tapsoffire.R;
import app.tapsoffire.configuration.Config;

public class FTextView extends androidx.appcompat.widget.AppCompatTextView {

    @Inject protected Config config;

    public FTextView(Context context) {
        super(context);
        setupTypeface(null, 0);
    }

    public FTextView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setupTypeface(attributes, 0);
    }

    public FTextView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setupTypeface(attributes, style);
    }

    private void setupTypeface(AttributeSet attributes, int style) {
        boolean fireTypeface = false;
        if (attributes != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attributes,
                    R.styleable.FTextView,
                    style, 0);
            fireTypeface = a.getBoolean(R.styleable.FTextView_fireTypeface, false);
            a.recycle();
        }

        if (fireTypeface) {
            setTypeface(config.getFireTypeface());
        } else {
            setTypeface(config.getDefaultTypeface());
        }
    }

}
