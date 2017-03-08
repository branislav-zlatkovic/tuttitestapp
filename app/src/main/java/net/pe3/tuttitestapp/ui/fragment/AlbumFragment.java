package net.pe3.tuttitestapp.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.pe3.tuttitestapp.R;
import net.pe3.tuttitestapp.TuttiTestApplication;
import net.pe3.tuttitestapp.model.FullAlbum;
import net.pe3.tuttitestapp.presenter.AlbumPresenter;
import net.pe3.tuttitestapp.ui.UiUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumFragment extends Fragment {

    public static final String TAG = AlbumFragment.class.getSimpleName();

    @BindView(R.id.album_title)
    TextView mTitle;

    @BindView(R.id.album_cover)
    ImageView mCoverImage;

    @Inject
    AlbumPresenter mPresenter;

    private ProgressDialog mProgressDialog;

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((TuttiTestApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);
        ButterKnife.bind(this, v);
        mPresenter.bind(this);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void showError(String message) {
        Snackbar.make(mTitle, message, Snackbar.LENGTH_LONG).show();
    }

    public void showLoading(boolean show) {
        if (show) {
            mProgressDialog = UiUtils.createProgressDialog(getContext(), getString(R.string.loading));
        } else if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showAlbumInfo(FullAlbum fullAlbum) {
        mTitle.setText(fullAlbum.album.title);
        Glide.with(getContext())
                .load(fullAlbum.photo.url)
                .fitCenter()
                .into(mCoverImage);
    }
}
