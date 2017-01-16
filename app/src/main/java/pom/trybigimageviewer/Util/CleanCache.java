package pom.trybigimageviewer.Util;

import android.content.Context;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class CleanCache {
    Context mContext;


    public CleanCache(Context mContext) {
        this.mContext = mContext;
    }

    public static CleanCache newInstance(Context context) {

        return new CleanCache(context);
    }

    public void cleanCache() {

//        clear Glide disk cache
        deleteDirectoryTree(mContext.getCacheDir());
        //        clear the Glide image cache
        Glide.get(mContext).clearMemory();
        //        clear the Cache made bu us
        Cache.clear(mContext);


    }

    private void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }
}
