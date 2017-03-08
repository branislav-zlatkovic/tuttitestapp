package net.pe3.tuttitestapp.di.modules;

import android.app.Application;
import android.content.Context;

import net.pe3.tuttitestapp.api.TypicodeApi;
import net.pe3.tuttitestapp.manager.UserManager;
import net.pe3.tuttitestapp.presenter.AlbumPresenter;
import net.pe3.tuttitestapp.presenter.LoginPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application application) {
        mApp = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }

    @Provides
    Context provideAppContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    LoginPresenter provideLoginPresenter(TypicodeApi api, UserManager userManager) {
        return new LoginPresenter(api, userManager);
    }

    @Provides
    AlbumPresenter provideAlbumPresenter(TypicodeApi api, UserManager userManager) {
        return new AlbumPresenter(api, userManager);
    }
}