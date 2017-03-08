package net.pe3.tuttitestapp;

import android.app.Application;

import net.pe3.tuttitestapp.di.components.AppComponent;
import net.pe3.tuttitestapp.di.components.DaggerAppComponent;
import net.pe3.tuttitestapp.di.modules.AppModule;

import timber.log.Timber;

public class TuttiTestApplication extends Application {

    private AppComponent mAppComponent;

    public void onCreate() {
        super.onCreate();
        initDependencyInjection();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initDependencyInjection() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
