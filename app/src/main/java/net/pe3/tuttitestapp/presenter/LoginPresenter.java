package net.pe3.tuttitestapp.presenter;


import com.android.volley.ParseError;

import net.pe3.tuttitestapp.R;
import net.pe3.tuttitestapp.api.TypicodeApi;
import net.pe3.tuttitestapp.manager.UserManager;
import net.pe3.tuttitestapp.model.User;
import net.pe3.tuttitestapp.ui.activity.AlbumActivity;
import net.pe3.tuttitestapp.ui.fragment.LoginFragment;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoginPresenter {

    private TypicodeApi mApi;
    private UserManager mUserManager;
    private Subscription mSubscription;

    private WeakReference<LoginFragment> weakReferenceView;
    private boolean isViewExisting;

    public LoginPresenter(TypicodeApi api, UserManager userManager) {
        mApi = api;
        mUserManager = userManager;
    }

    public void bind(LoginFragment loginFragment) {
        this.weakReferenceView = new WeakReference<>(loginFragment);
        isViewExisting = true;
        loginFragment.setEmail(mUserManager.getUserEmail());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            weakReferenceView.get().showLoading(true);
        }
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

    public void submitEmail(String email) {
        if (mSubscription == null || mSubscription.isUnsubscribed()) { //Subscribe to observable if no subscription already in place
            weakReferenceView.get().showLoading(true);
            mSubscription = mApi.getEmailVerificationObservable(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .delay(5, TimeUnit.SECONDS)
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {
                            //ignore
                        }

                        @Override
                        public void onError(Throwable e) {
                            LoginFragment fragment = weakReferenceView.get();
                            if (fragment != null && isViewExisting) {
                                fragment.showLoading(false);
                                if (e.getCause() instanceof ParseError) {
                                    mUserManager.clearUser();
                                    Timber.d(e, "Returned data cannot be parsed to a user object");
                                    fragment.showError(fragment.getString(R.string.error_no_user));
                                } else {
                                    Timber.d(e, "Error verifying email");
                                    fragment.showError(fragment.getString(R.string.error_generic));
                                }
                            }
                        }

                        @Override
                        public void onNext(User user) {
                            Timber.d("Email verification success: %s", user.email);
                            LoginFragment fragment = weakReferenceView.get();
                            if (fragment != null) {
                                fragment.showLoading(false);
                                mUserManager.setUser(user);
                                fragment.startActivity(AlbumActivity.getIntent(fragment.getContext()));
                                fragment.getActivity().finish();
                            }
                        }
                    });
        }
    }
}

