package pom.trybigimageviewer;

import android.app.Application;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
    }
}
