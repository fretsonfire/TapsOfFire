package app.tapsoffire;

import app.tapsoffire.device.BuildInfo;
import app.tapsoffire.device.BuildType;
import app.tapsoffire.di.AppComponent;
import app.tapsoffire.di.AppModule;
import app.tapsoffire.di.DaggerAppComponent;
import app.tapsoffire.log.Logger;
import app.tapsoffire.utils.Preconditions;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

public class TapsOfFire extends Application {

    private static final String TAG = "TapsOfFire";

    @Nullable public static AppComponent appComponent;

    @Inject protected BuildInfo buildInfo;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .buildInfo(createBuildInfo())
                .build();

        appComponent.inject(this);
    }

    @NonNull
    public static AppComponent getAppComponent() {
        Preconditions.checkNonNull(appComponent);
        return appComponent;
    }

    public BuildInfo createBuildInfo() {
        if (BuildConfig.DEBUG || BuildConfig.FLAVOR.toLowerCase().equals("beta")) {
            return new BuildInfo(BuildType.BETA);
        }

        return new BuildInfo(BuildType.RELEASE);
    }

    public BuildInfo getBuildInfo() {
        return this.buildInfo;
    }
}
