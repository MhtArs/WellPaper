package tr.mht.wallpaper;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by Administrator on 8.2.2016.
 */
public class WellPaperApp extends Application {
    public static final String TAG = "WellPaper App";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Stetho.initialize(Stetho.newInitializerBuilder(mContext)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(mContext))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(mContext))
                .build());
    }

    public static Context getContext() {
        return mContext;
    }
}
