package app.tapsoffire.di;

import android.app.Application;
import android.content.Context;

import app.tapsoffire.TapsOfFire;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final TapsOfFire app;

    public AppModule(TapsOfFire app) {
        this.app = app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }

    @Provides
    public Context provideContext(Application application) {
        return application.getApplicationContext();
    }

}
