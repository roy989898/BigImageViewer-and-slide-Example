package pom.trybigimageviewer.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pom.trybigimageviewer.R;
import pom.trybigimageviewer.Util.StaticValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {


    @BindView(R.id.mBigImage)
    BigImageView mBigImage;
    private Unbinder unBinder;

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
        mBigImage.setProgressIndicator(new ProgressPieIndicator());

        Bundle args = getArguments();
        String link = args.getString(StaticValue.BUNDLE_KEY_IMAGE_LINK);
        if (link != null)
            mBigImage.showImage(Uri.parse(link));
        return view;
    }

    @Override
    public void onDestroyView() {
//        unBinder.unbind();
        super.onDestroyView();
    }
}
