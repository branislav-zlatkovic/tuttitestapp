package net.pe3.tuttitestapp.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import net.pe3.tuttitestapp.R;
import net.pe3.tuttitestapp.TuttiTestApplication;
import net.pe3.tuttitestapp.presenter.LoginPresenter;
import net.pe3.tuttitestapp.ui.UiUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.input_layout_email)
    TextInputLayout mEmailInputLayout;

    @BindView(R.id.email)
    EditText mEmailInput;

    @BindView(R.id.go_button)
    Button mGoButton;

    @Inject
    LoginPresenter mPresenter;

    private ProgressDialog mProgressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        mPresenter.bind(this);

        initTextValidationObservable();
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

    private void initTextValidationObservable() {
        Observable<Boolean> emailValidationObservable = RxTextView.textChanges(mEmailInput)
                .skip(1)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return validateEmail();
                    }
                });

        emailValidationObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mGoButton.setEnabled(aBoolean);
            }
        });
    }

    /**
     * Validate email input. If input is invalid, the input field displays an error and requests focus
     * @return true if email input is valid
     */
    private boolean validateEmail() {
        String email = getEmail();
        if (email.isEmpty()) {
            mEmailInputLayout.setError(getString(R.string.error_field_required));
            mEmailInput.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailInputLayout.setError(getString(R.string.error_invalid_email));
            mEmailInput.requestFocus();
            return false;
        } else {
            mEmailInputLayout.setError(null);
        }
        return true;
    }

    private String getEmail() {
        return mEmailInput.getText().toString().trim();
    }

    public void setEmail(String email) {
        mEmailInput.setText(email);
    }

    @OnClick(R.id.go_button)
    void onActionButtonPressed() {
        mPresenter.submitEmail(getEmail());
    }

    public void showError(String message) {
        Snackbar.make(mEmailInputLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void showLoading(boolean show) {
        if (show) {
            mProgressDialog = UiUtils.createProgressDialog(getContext(), getString(R.string.loading));
        } else if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
