package net.pe3.tuttitestapp.di.modules;

import android.content.Context;

import net.pe3.tuttitestapp.api.TypicodeApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    @Singleton
    TypicodeApi provideApi(Context context) {
        return new TypicodeApi(context);
    }
}
