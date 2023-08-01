package com.aic.aicdelivery;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 19-06-2017.
 */
// its the fragment from where we create fragments
public class myImageSlider extends Fragment {
    String imageid;
    // static method to create the MyImageSlider Fragment containing image
    public  static myImageSlider newInstance(JSONObject jsonobject) throws JSONException {
        myImageSlider slider=new myImageSlider();
        Bundle b=new Bundle();
        b.putString("imageurl", jsonobject.getString("imageurll"));
        slider.setArguments(b);
        return slider;
    }
    // get the image id from fragment in this method although we can also get in onCreateView.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageid=getArguments().getString("imageurl");
    }
    // this method returns the view containing the required which is set while creating instance of fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_swipe, container, false);
        ImageView iv=(ImageView)view.findViewById(R.id.hmImage);

        Picasso.with(getActivity())
                .load(imageid)
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .into(iv);
        Log.i("PersonsActivity", String.valueOf(imageid));
        return view;
    }
}
