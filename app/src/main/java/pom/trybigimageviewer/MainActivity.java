package pom.trybigimageviewer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pom.trybigimageviewer.Util.CleanCache;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mBigImage)
    BigImageView mBigImage;
    private final String imageLink="http://uat.services.mrm.hk/data/Kerry/images/property_info/7/48/64/HMT_2ndEntrance_r02-2.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mBigImage.setProgressIndicator(new ProgressPieIndicator());
        mBigImage.showImage(Uri.parse(imageLink));
    }

    @Override
    protected void onDestroy() {
        CleanCache.newInstance(this).cleanCache();
        super.onDestroy();
    }
}
