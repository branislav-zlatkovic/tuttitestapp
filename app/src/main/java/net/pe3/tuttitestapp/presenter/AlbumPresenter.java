package net.pe3.tuttitestapp.presenter;

import net.pe3.tuttitestapp.api.TypicodeApi;
import net.pe3.tuttitestapp.manager.UserManager;
import net.pe3.tuttitestapp.model.FullAlbum;
import net.pe3.tuttitestapp.ui.fragment.AlbumFragment;

import java.lang.ref.WeakReference;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AlbumPresenter {

    private TypicodeApi mApi;
    private UserManager mUserManager;
    private Subscription mSubscription;

    private FullAlbum mFullAlbum;

    private WeakReference<AlbumFragment> weakReferenceView;
    private boolean isViewExisting;

    public AlbumPresenter(TypicodeApi api, UserManager userManager) {
        mApi = api;
        mUserManager = userManager;
    }

    public void bind(AlbumFragment albumFragment) {
        this.weakReferenceView = new WeakReference<>(albumFragment);
        isViewExisting = true;
        loadFirstAlbumData();
    }

    public void unBind() {
        isViewExisting = false;
        weakReferenceView.get().showLoading(false);
    }

    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private void loadFirstAlbumData() {
        // Display cached object, if it exists
        if (mFullAlbum != null) {
            weakReferenceView.get().showAlbumInfo(mFullAlbum);
        } else if (mSubscription == null || mSubscription.isUnsubscribed()) { //Subscribe to observable if no subscription already in place
            weakReferenceView.get().showLoading(true);
            mSubscription = mApi.getFirstAlbumObservable(mUserManager.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FullAlbum>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d(e, "Error fetchng album data");
                            AlbumFragment fragment = weakReferenceView.get();
                            if (fragment != null && isViewExisting) {
                                fragment.showLoading(false);
                                fragment.showError("Error displaying album info");
                            }
                        }

                        @Override
                        public void onNext(FullAlbum fullAlbum) {
                            Timber.d("Fetched album info, title: %s, cover url: %s",
                                    fullAlbum.album.title, fullAlbum.photo.url);
                            mFullAlbum = fullAlbum;
                            AlbumFragment fragment = weakReferenceView.get();
                            if (fragment != null && isViewExisting) {
                                fragment.showLoading(false);
                                fragment.showAlbumInfo(fullAlbum);
                            }
                        }
                    });
        } else {
            weakReferenceView.get().showLoading(true);
        }
    }
}
