package tr.mht.wallpaper.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import tr.mht.wallpaper.R;
import tr.mht.wallpaper.model.Photo;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_PHOTO = "selected_photo";

    private int mWallpaperWidth, mWallpaperHeight;
    private Photo mSelectedPhoto;
    private ImageView imageView;
    private IconicsDrawable mDrawablePhoto;
    private ImageView mFabButton;
    private TextView author;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWallpaperWidth = WallpaperManager.getInstance(DetailActivity.this).getDesiredMinimumWidth();
        mWallpaperHeight = WallpaperManager.getInstance(DetailActivity.this).getDesiredMinimumHeight();

        imageView = (ImageView) findViewById(R.id.activity_detail_image);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
            imageView.setTransitionName("selectedphoto");
        }

        mDrawablePhoto = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_photo).color(Color.WHITE).sizeDp(24);

        mFabButton = (ImageView) findViewById(R.id.activity_detail_fab);
//        mFabButton.setScaleX(0);
//        mFabButton.setScaleY(0);
        mFabButton.setImageDrawable(mDrawablePhoto);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answers.getInstance().logCustom(new CustomEvent("Set Wallpaper")
                    .putCustomAttribute("photo_id", mSelectedPhoto.getId()));
                setWallpaperFromURLString(mSelectedPhoto.getPhotoUrl(mWallpaperWidth));
            }
        });

        author = (TextView) findViewById(R.id.activity_detail_author);
        title = (TextView) findViewById(R.id.activity_detail_title);
        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        mSelectedPhoto = intent.getParcelableExtra(EXTRA_PHOTO);

        title.setText(mSelectedPhoto.getTitle());
        author.setText(mSelectedPhoto.getOwnername());
        Picasso.with(this).load(mSelectedPhoto.getPhotoUrl(mWallpaperWidth)).into(imageView);
    }

    private void setWallpaperFromURLString(String urlString) {
        AsyncTask<String, String, Object> task = new AsyncTask<String, String, Object>() {

            @Override
            protected Object doInBackground(String... params) {
                URL url = null;
                try {
                    url = new URL(params[0]);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    if (url != null) {
                        WallpaperManager.getInstance(DetailActivity.this).setStream(
                                url.openConnection().getInputStream());
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(urlString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
