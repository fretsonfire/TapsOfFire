package app.tapsoffire.ui.components;

import app.tapsoffire.configuration.Config;
import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

import javax.inject.Inject;

public class TextButton extends AppCompatButton
{
    @Inject
    protected Config config;

    public TextButton(Context context) {
        super(context);
        setup();
    }

    public TextButton(Context context,AttributeSet attrs) {
        super(context,attrs);
        setup();
    }

    public TextButton(Context context,AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        setup();
    }

    private void setup() {
        setTypeface(config.getDefaultTypeface());
    }
}
