package pom.trybigimageviewer.ProgressIndicator;

import android.view.LayoutInflater;
import android.view.View;

import com.filippudak.ProgressPieView.ProgressPieView;
import com.github.piasy.biv.indicator.ProgressIndicator;
import com.github.piasy.biv.view.BigImageView;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class MyProgressIndicator implements ProgressIndicator {
    private ProgressPieView mProgressPieView;

    @Override
    public View getView(BigImageView parent) {
        mProgressPieView = (ProgressPieView) LayoutInflater.from(parent.getContext())
                .inflate(com.github.piasy.biv.indicator.progresspie.R.layout.ui_progress_pie_indicator, parent, false);
        return mProgressPieView;
    }

    @Override
    public void onStart() {
        // not interested
    }

    @Override
    public void onProgress(int progress) {
       /* if (progress < 0 || progress > 100) {
            return;
        }
        mProgressPieView.setProgress(progress);
        mProgressPieView.setText(String.format(Locale.getDefault(), "%d%%", progress));*/
    }

    @Override
    public void onFinish() {
        // not interested
    }
}

