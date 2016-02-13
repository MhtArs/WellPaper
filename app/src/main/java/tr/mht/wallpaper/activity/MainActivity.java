package tr.mht.wallpaper.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import tr.mht.wallpaper.R;
import tr.mht.wallpaper.fragment.WallpaperListFragment;

public class MainActivity extends AppCompatActivity implements WallpaperListFragment.OnFragmentInteractionListener {
    public static final String TAG = "Main Activity";
    private Drawer drawer;
    private OnCategoryChangedListener onCategoryChangedListener;

    public void setOnCategoryChangedListener(OnCategoryChangedListener onCategoryChangedListener) {
        this.onCategoryChangedListener = onCategoryChangedListener;
    }

    public enum Category {
        TRENDING(1),
        RECENT(2);

        public final int id;

        private Category(int id) {
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Interesting")
                                .withIdentifier(Category.TRENDING.id)
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_trending_up)),
                        new PrimaryDrawerItem()
                                .withName("Recent")
                                .withIdentifier(Category.RECENT.id)
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_system_update)),
                        new PrimaryDrawerItem()
                                .withName("About")
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_info))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem != null) {
                            if(position == 2) {// about
                                new LibsBuilder()
                                        .withActivityStyle(Libs.ActivityStyle.DARK)
                                        .withActivityTitle("About")
                                        .start(view.getContext());
                            } else {
                                if(onCategoryChangedListener != null) {
                                    onCategoryChangedListener.onCategoryChanged(drawerItem.getIdentifier());
                                }
                            }
                        }
                        switch(position) {
                            case 0:
                                // TODO
                                break;
                            case 1:

                                break;
                        }
                        return false;
                    }
                })
                .build();

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, WallpaperListFragment.newInstance()).commit();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnCategoryChangedListener {
        void onCategoryChanged(int category);
    }
}
