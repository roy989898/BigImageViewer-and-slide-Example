package pom.trybigimageviewer.Fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pom.trybigimageviewer.R;
import pom.trybigimageviewer.Util.StaticValue;
import pom.trybigimageviewer.Util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {


    @BindView(R.id.iv_touch)
    SubsamplingScaleImageView ivTouch;
    @BindView(R.id.bt_load)
    Button btLoad;
    private Unbinder unBinder;
    Util.GetCachedImageTask mGetCachedImageTask;

    public static SlideFragment newInstance(Bundle args) {


        SlideFragment fragment = new SlideFragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public SlideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        unBinder = ButterKnife.bind(this, view);
//        mBigImage.setProgressIndicator(new MyProgressIndicator());

        Bundle args = getArguments();
        String link = args.getString(StaticValue.BUNDLE_KEY_IMAGE_LINK);

        downloadImg(link);


        return view;
    }

    @Override
    public void onDestroyView() {
        unBinder.unbind();
        super.onDestroyView();
    }


    private void downloadImg(String imgUrl) {
        if (mGetCachedImageTask != null) {
            mGetCachedImageTask.cancel(true);
        }


        mGetCachedImageTask = new Util.GetCachedImageTask(getContext(), imgUrl) {
            public Bitmap mImage;

            @Override
            protected void onPreExecute() {
                try {
                    btLoad.setVisibility(View.VISIBLE);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }


            }

            protected void onPostExecute(Bitmap result) {



                if (result != null) {
                    mImage = result;
                    // initImageView();

                    try {
                        btLoad.setVisibility(View.GONE);
                        ivTouch.setVisibility(View.VISIBLE);
//                    ivTouch.setImageBitmapReset(mImage, 0, true);
                        ivTouch.setImage(ImageSource.bitmap(result));
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    Log.d("ImageViewerActivity", "Download Image fail");
                }
            }

            ;
        };
        mGetCachedImageTask.execute();
    }
}
