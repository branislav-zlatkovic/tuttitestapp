package net.pe3.tuttitestapp.di.components;

import net.pe3.tuttitestapp.di.modules.ApiModule;
import net.pe3.tuttitestapp.di.modules.AppModule;
import net.pe3.tuttitestapp.di.modules.SharedPrefsModule;
import net.pe3.tuttitestapp.di.modules.UserModule;
import net.pe3.tuttitestapp.ui.fragment.AlbumFragment;
import net.pe3.tuttitestapp.ui.fragment.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, SharedPrefsModule.class, UserModule.class, ApiModule.class})
public interface AppComponent {

    void inject(LoginFragment loginFragment);

    void inject(AlbumFragment albumFragment);
}
