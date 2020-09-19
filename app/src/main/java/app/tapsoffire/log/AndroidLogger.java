package app.tapsoffire.log;

import android.util.Log;

public class AndroidLogger implements Logger{

    @Override
    public void log(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }
}
