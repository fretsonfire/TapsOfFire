package app.tapsoffire.di;

import android.app.Application;

import app.tapsoffire.TapsOfFire;
import app.tapsoffire.activity.MainMenuActivity;
import app.tapsoffire.device.BuildInfo;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder buildInfo(BuildInfo buildInfo);

        AppComponent build();
    }

    void inject(MainMenuActivity activity);

    void inject(TapsOfFire app);
}
