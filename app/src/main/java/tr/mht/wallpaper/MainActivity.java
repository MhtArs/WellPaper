package tr.mht.wallpaper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import tr.mht.wallpaper.adapter.WallpaperListAdapter;
import tr.mht.wallpaper.model.Image;
import tr.mht.wallpaper.network.WellPaperApi;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main Activity";

    RecyclerView imageList;

    ArrayList<Image> mWallpapers;
    private WallpaperListAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWallpapers = new ArrayList<>();

        imageList = (RecyclerView) findViewById(R.id.wallpaper_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        imageList.setLayoutManager(mLayoutManager);

        imageAdapter = new WallpaperListAdapter(this, mWallpapers);
        imageList.setAdapter(imageAdapter);

        WellPaperApi.getApi().getImages().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Response<List<Image>> response, Retrofit retrofit) {
                mWallpapers.addAll(response.body());
                Log.d(TAG, response.body().get(0).getId());
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
