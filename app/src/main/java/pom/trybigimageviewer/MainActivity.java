package pom.trybigimageviewer;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pom.trybigimageviewer.Adapter.ScreenSlidePagerAdapter;
import pom.trybigimageviewer.Util.CleanCache;

public class MainActivity extends AppCompatActivity {


    private final String imageLink1 = "http://uat.services.mrm.hk/data/Kerry/images/property_info/9/53/62/P2_Layout.jpg";
    private final String imageLink2 = "http://uat.services.mrm.hk/data/Kerry/images/property_info/9/53/62/P2_Carpark.jpg";
    private final String imageLink3 = "http://uat.services.mrm.hk/data/Kerry/images/property_info/9/53/62/P2_Building.jpg";
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<String> linkList = new ArrayList<>();
        initTheArrayList(linkList);
        PagerAdapter pageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), linkList);
        pager.setAdapter(pageAdapter);


    }

    @Override
    protected void onDestroy() {
        CleanCache.newInstance(this).cleanCache();
        super.onDestroy();
    }

    private void initTheArrayList(ArrayList<String> linkList) {
        linkList.add(imageLink1);
        linkList.add(imageLink2);
        linkList.add(imageLink3);

    }

}
