package net.pe3.tuttitestapp.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPrefsModule {

    private static final String PREF_NAME = "default_prefs";

    @Provides
    @Singleton
    SharedPreferences provideUserSharedPreferences(Application application) {
        return application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
