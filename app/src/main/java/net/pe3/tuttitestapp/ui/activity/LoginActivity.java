package net.pe3.tuttitestapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.pe3.tuttitestapp.R;
import net.pe3.tuttitestapp.ui.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initMainContent();
    }

    private void initMainContent() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentByTag(LoginFragment.TAG);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    fragment, LoginFragment.TAG).commit();
        }
    }
}
