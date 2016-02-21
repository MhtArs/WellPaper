package tr.mht.wallpaper;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 8.2.2016.
 */
public class WellPaperApp extends Application {
    public static final String TAG = "WellPaper App";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
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
