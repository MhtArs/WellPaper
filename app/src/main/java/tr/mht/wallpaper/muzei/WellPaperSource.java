package tr.mht.wallpaper.muzei;

import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.Random;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import tr.mht.wallpaper.model.Photo;
import tr.mht.wallpaper.model.Photos;
import tr.mht.wallpaper.model.PhotosResponse;
import tr.mht.wallpaper.network.WellPaperApi;

/**
 * Created by Administrator on 14.2.2016.
 */
public class WellPaperSource extends RemoteMuzeiArtSource {
    private static final String SOURCE_NAME = "wellpaper";
    private static final int ROTATE_TIME_MILLIS = 4 * 60 * 60 * 1000; // rotate every 4 hours

    private int mWallpaperWidth = -1;

    public WellPaperSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);

        mWallpaperWidth = WallpaperManager.getInstance(this).getDesiredMinimumWidth();
        Log.d(SOURCE_NAME, "desired minimum wallpaper width is " + mWallpaperWidth);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        WellPaperApi.getApi().getInterestingPhotos(1, 100).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Response<PhotosResponse> response, Retrofit retrofit) {
                Photos photos = response.body().getPhotos();
                Photo randomPhoto = photos.getPhoto().get(new Random().nextInt(photos.getPhoto().size()));

                if(randomPhoto == null) {
                    scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                    return;
                }

                publishArtwork(new Artwork.Builder()
                        .imageUri(Uri.parse(randomPhoto.getPhotoUrl(mWallpaperWidth)))
                        .title(randomPhoto.getTitle())
                        .byline(randomPhoto.getOwnername())
                        .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(randomPhoto.getFlickrPhotoUrl())))
                        .build());

                //schedule the next update ;)
                scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
