package tr.mht.wallpaper.activity;

import android.graphics.Color;
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
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

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
        RECENT(2),
        NEARME(3),
        SHUFFLE(256);

        public final int id;

        private Category(int id) {
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Interesting")
                                .withIdentifier(Category.TRENDING.id)
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_trending_up)),
                        new PrimaryDrawerItem()
                                .withName("Recent")
                                .withIdentifier(Category.RECENT.id)
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_system_update)),
                        /*new PrimaryDrawerItem()
                                .withName("Near Me")
                                .withIdentifier(Category.NEARME.id)
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_my_location)),*/
                        new PrimaryDrawerItem()
                                .withName("About")
                                .withSelectable(false)
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
                                if(drawerItem instanceof Nameable) {
                                    toolbar.setTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                }
                                if(onCategoryChangedListener != null) {
                                    onCategoryChangedListener.onCategoryChanged(drawerItem.getIdentifier());
                                }
                            }
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
        menu.findItem(R.id.action_shuffle).setIcon(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_shuffle).color(Color.WHITE).actionBar()
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_shuffle:
                onCategoryChangedListener.onCategoryChanged(Category.SHUFFLE.id);
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
