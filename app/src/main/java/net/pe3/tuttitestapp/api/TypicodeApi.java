package net.pe3.tuttitestapp.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import net.pe3.tuttitestapp.model.Album;
import net.pe3.tuttitestapp.model.FullAlbum;
import net.pe3.tuttitestapp.model.Photo;
import net.pe3.tuttitestapp.model.User;
import net.pe3.tuttitestapp.volley.FirstArrayObjectRequest;

import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import timber.log.Timber;

public class TypicodeApi {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String USERS_ENDPOINT = BASE_URL + "users";
    private static final String ALBUMS_ENDPOINT = BASE_URL + "albums";
    private static final String PHOTOS_ENDPOINT = BASE_URL + "photos";

    private static final String PARAM_EMAIL = "?email=";
    private static final String PARAM_USER_ID = "?userId=";
    private static final String PARAM_ALBUM_ID = "?albumId=";

    private RequestQueue mRequestQueue;

    public TypicodeApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public Observable<User> getEmailVerificationObservable(final String email) {
        return Observable.defer(new Func0<Observable<User>>() {
            @Override
            public Observable<User> call() {
                try {
                    return Observable.just(getUser(email));
                } catch (InterruptedException | ExecutionException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    private User getUser(String email) throws InterruptedException, ExecutionException {
        String url = USERS_ENDPOINT + PARAM_EMAIL + email;
        Timber.i("Fetching user: %s", url);

        RequestFuture<User> future = RequestFuture.newFuture();
        FirstArrayObjectRequest<User> request = new FirstArrayObjectRequest<>(
                url, User.class, null, future, future);
        mRequestQueue.add(request);
        return future.get();
    }

    public Observable<FullAlbum> getFirstAlbumObservable(final int userId) {
        return Observable.defer(new Func0<Observable<FullAlbum>>() {
            @Override
            public Observable<FullAlbum> call() {
                try {
                    final FullAlbum fullAlbum = new FullAlbum();
                    return Observable.just(getFirstALbum(userId))
                            .flatMap(new Func1<Album, Observable<Photo>>() {
                                @Override
                                public Observable<Photo> call(Album album) {
                                    fullAlbum.album = album;
                                    return getFirstPhotoObservable(album.id);
                                }
                            })
                            .map(new Func1<Photo, FullAlbum>() {
                                @Override
                                public FullAlbum call(Photo photo) {
                                    fullAlbum.photo = photo;
                                    return fullAlbum;
                                }
                            });

                } catch (InterruptedException | ExecutionException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    private Album getFirstALbum(int userId) throws InterruptedException, ExecutionException {
        String url = ALBUMS_ENDPOINT + PARAM_USER_ID + userId;
        Timber.i("Fetching first album for userId %s", userId);

        RequestFuture<Album> future = RequestFuture.newFuture();
        FirstArrayObjectRequest<Album> request = new FirstArrayObjectRequest<>(
                url, Album.class, null, future, future);
        mRequestQueue.add(request);
        return future.get();
    }

    private Observable<Photo> getFirstPhotoObservable(int albumId) {
        try {
            return Observable.just(getFirstPhoto(albumId));
        } catch (InterruptedException | ExecutionException e) {
            return Observable.error(e);
        }
    }

    private Photo getFirstPhoto(int albumId) throws InterruptedException, ExecutionException {
        String url = PHOTOS_ENDPOINT + PARAM_ALBUM_ID + albumId;
        Timber.i("Fetching first photo for albumId %s", albumId);

        RequestFuture<Photo> future = RequestFuture.newFuture();
        FirstArrayObjectRequest<Photo> request = new FirstArrayObjectRequest<>(
                url, Photo.class, null, future, future);
        mRequestQueue.add(request);
        return future.get();
    }
}
