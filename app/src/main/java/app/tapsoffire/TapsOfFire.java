package app.tapsoffire;

import app.tapsoffire.device.BuildInfo;
import app.tapsoffire.device.BuildType;
import app.tapsoffire.di.AppComponent;
import app.tapsoffire.di.AppModule;
import app.tapsoffire.di.DaggerAppComponent;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

public class TapsOfFire extends Application {

    private BuildInfo buildInfo;

    @Nullable public static AppComponent appComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // determine build info
        this.buildInfo = createBuildInfo();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .buildInfo(createBuildInfo())
                .build();
        appComponent.inject(this);

        // figure out build config
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
