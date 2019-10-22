package appmoviles.com.preclase13.app;

import android.app.Application;
import android.content.Context;

public class AlbumApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AlbumApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AlbumApp.context;
    }
}
