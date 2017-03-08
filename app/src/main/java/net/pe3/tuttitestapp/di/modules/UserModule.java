package net.pe3.tuttitestapp.di.modules;

import android.content.SharedPreferences;

import net.pe3.tuttitestapp.manager.UserManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    @Singleton
    UserManager provideUserManager(SharedPreferences sharedPreferences) {
        return new UserManager(sharedPreferences);
    }
}
