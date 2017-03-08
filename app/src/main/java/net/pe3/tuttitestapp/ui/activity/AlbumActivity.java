package net.pe3.tuttitestapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.pe3.tuttitestapp.R;
import net.pe3.tuttitestapp.ui.fragment.AlbumFragment;

public class AlbumActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AlbumActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initMainContent();
    }

    private void initMainContent() {
        AlbumFragment fragment = (AlbumFragment) getSupportFragmentManager()
                .findFragmentByTag(AlbumFragment.TAG);
        if (fragment == null) {
            fragment = AlbumFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    fragment, AlbumFragment.TAG).commit();
        }
    }
}
