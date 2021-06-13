package app.tapsoffire.ui.helpers;

import app.tapsoffire.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class UIHelpers
{
    /**
     * Makes the application fullscreen
     * @param activity the activity to make fullscreen
     */
    public static void makeFullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static int getInteger(Context context, int id) {
        return context.getResources().getInteger(id);
    }

    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String getString(Context context, int id, Object...arguments) {
        return context.getResources().getString(id,arguments);
    }

    public static Typeface getTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(),fontPath);
    }

    public static int startViewAnimation(View parentView, int viewID, int animationID) {
        return startViewAnimation(parentView,viewID,animationID,0);
    }

    public static int startViewAnimation(View parentView, int viewID, int animationID, int offset) {
        View view = parentView.findViewById(viewID);
        if (view == null) {
            return 0;
        }

        Animation animation=view.getAnimation();
        if (animation==null) {
            animation = AnimationUtils.loadAnimation(
                    parentView.getContext(),
                    animationID);
        }

        animation.setStartOffset(offset);
        view.startAnimation(animation);

        return offset + (int) animation.getDuration();
    }

    public static int startViewAnimation(Activity activity, int viewID, int animationID) {
        return startViewAnimation(activity,viewID,animationID,0);
    }

    public static int startViewAnimation(Activity activity, int viewID, int animationID, int offset) {
        View view = activity.findViewById(viewID);
        if (view == null) {
            return 0;
        }

        Animation animation = AnimationUtils.loadAnimation(activity,animationID);
        offset += animation.getStartOffset();
        animation.setStartOffset(offset);
        view.startAnimation(animation);

        return offset + (int) animation.getDuration();
    }

}
