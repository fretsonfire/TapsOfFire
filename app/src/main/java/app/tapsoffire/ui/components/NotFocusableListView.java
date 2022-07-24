package app.tapsoffire.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NotFocusableListView extends ListView {

    public NotFocusableListView(Context context) {
        super(context);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    public NotFocusableListView(Context context,AttributeSet attributes) {
        super(context,attributes);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    public NotFocusableListView(Context context,AttributeSet attributes,int style) {
        super(context,attributes,style);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

}
