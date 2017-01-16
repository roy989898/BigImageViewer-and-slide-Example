package pom.trybigimageviewer.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.github.piasy.biv.view.BigImageView;

import java.io.File;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class MyBigImageView extends BigImageView {
    public MyBigImageView(Context context) {
        super(context);
    }

    public MyBigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCacheMiss(File image) {
        Log.d("MyBigImageView", "onCacheMiss");
        super.onCacheMiss(image);
    }

    @Override
    public void onCacheHit(File image) {
        Log.d("MyBigImageView", "onCacheHit");
        super.onCacheHit(image);
    }

    @Override
    public void onFinish() {
        Log.d("MyBigImageView", "onFinish");
        super.onFinish();
    }
}
