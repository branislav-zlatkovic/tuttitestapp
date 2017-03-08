package net.pe3.tuttitestapp.manager;

import android.content.SharedPreferences;
import android.text.TextUtils;

import net.pe3.tuttitestapp.model.User;

public class UserManager {

    private static final String KEY_EMAIL = "email";

    private User user;

    private SharedPreferences sharedPreferences;

    public UserManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        user = new User();
    }

    public int getUserId() {
        return user.id;
    }

    public String getUserEmail() {
        return TextUtils.isEmpty(user.email) ? getStoredEmail() : user.email;
    }

    public void setUser(User user) {
        this.user = user;
        if (!TextUtils.isEmpty(user.email)) {
            storeEmail();
        }
    }

    public void clearUser() {
        user = new User();
        storeEmail();
    }

    private void storeEmail() {
        sharedPreferences.edit().putString(KEY_EMAIL, user.email).apply();
    }

    private String getStoredEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }
}
