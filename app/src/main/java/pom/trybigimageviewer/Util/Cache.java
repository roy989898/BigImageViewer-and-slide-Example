package pom.trybigimageviewer.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 4/2/15.
 */
public class Cache {


    private final static String PREFIX_CHACHE = "cache_";

    public static boolean isImageCached(Context context, String imageName) {
        // Check is the image was cached
        boolean isImageCached = false;

        FileInputStream fis = null;
        try {

            fis = context.openFileInput(getCachedImageName(imageName));

            if (fis != null) {
                isImageCached = true;
            }

        } catch (FileNotFoundException e) {
            //			Utility.Error("Image '" + imageName + "' is not cached.");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isImageCached;
    }

    public static boolean saveImage(Context context, String imageName, Bitmap bitmap) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            if (bitmap != null && context != null) {
                fos = context.getApplicationContext().openFileOutput(getCachedImageName(imageName),
                        Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();

                fos = null;
                success = true;
                //				Utility.Log("Image '" + imageName + "' is saved.");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static Bitmap getImage(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fis = null;

        try {
            fis = context.getApplicationContext().openFileInput(getCachedImageName(imageName));
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //			Utility.Error("Image '" + imageName + "' is not cached.");
            bitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public static void clearImage(Context context, String imageName) {
        try {

            if (context.getApplicationContext().deleteFile(getCachedImageName(imageName))) {
                //				Utility.Log("Image '" + imageName + "' is deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearImages(Context context) {
        String[] fileList = null;
        try {
            fileList = context.getApplicationContext().fileList();
            int len = fileList.length;

            for (int i = 0; i < len; i++) {
                if (fileList[i].startsWith(PREFIX_CHACHE)) {
                    clearImage(context, fileList[i]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear(Context context) {
        clearImages(context);
    }

    private static String getCachedImageName(String imageName) {
        if (!imageName.startsWith(PREFIX_CHACHE)) {
            imageName = PREFIX_CHACHE + imageName;
        }
        return imageName;
    }

}
