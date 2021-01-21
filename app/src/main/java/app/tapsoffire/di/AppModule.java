package app.tapsoffire.di;

import android.app.Application;
import android.content.Context;

import app.tapsoffire.configuration.Config;
import app.tapsoffire.device.BuildInfo;
import app.tapsoffire.log.AndroidLogger;
import app.tapsoffire.log.Logger;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    @Provides
    public Application provideApplication(Application application) {
        this.application = application;
        return application;
    }

    @Provides
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    public Logger provideLogger() {
        return new AndroidLogger();
    }

}
