package pom.trybigimageviewer.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import pom.trybigimageviewer.Fragment.SlideFragment;
import pom.trybigimageviewer.Util.StaticValue;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> mLinkList;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<String> linkList) {
        super(fm);
        mLinkList = linkList;
    }

    @Override
    public Fragment getItem(int position) {
        String link=mLinkList.get(position);

        Bundle args=new Bundle();
        args.putString(StaticValue.BUNDLE_KEY_IMAGE_LINK,link);
        return SlideFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return mLinkList.size();
    }
}
