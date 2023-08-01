package com.aic.aicdelivery;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 18-06-2017.
 */
public class imageSwipe extends PagerAdapter {
    private Context ctx;
    private LayoutInflater layoutInflator;
    private JSONArray json_array;


    public imageSwipe(Context c,String output) throws JSONException {
        JSONObject myjson = new JSONObject(output);
        json_array = myjson.getJSONArray("slidelist");
        ctx=c;
    }

    @Override
    public int getCount() {
        return json_array.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }


    @Override
    public Object instantiateItem(ViewGroup container,int position ){
        HMCoreData myDB=new HMCoreData(ctx);
        Log.i("Debugging","Came here inside swipe");
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(R.layout.image_swipe,container,false);
        ImageView imageView=(ImageView) itemView.findViewById(R.id.hmImage);
        //return json image link and other slide data
        JSONObject jsonobject = null;
        try {
            jsonobject = json_array.getJSONObject(position);
        } catch (JSONException e) {
           myDB.showToast(ctx, e.getMessage());
        }

        try {
            String url = jsonobject.getString("imageurls");
         //  myDB.showToast(ctx, String.valueOf(position));
            Log.i("Debugging",url);
            if (url != null) {
                Picasso.with(ctx)
                        .load(url)
                        .placeholder(R.drawable.haal_meer_large)
                        .fit()
                        .centerCrop()
                        .noFade()
                        .into(imageView);
                Log.i("PersonsActivity", String.valueOf(url));
            }

            // myDB.showToast(ctx,url);
            //  int imageResource = ctx.getResources().getIdentifier(url, null, ctx.getPackageName());
            //    imageView.setImageResource(imageResource);
        } catch (JSONException e) {
           myDB.showToast(ctx, e.getMessage());
        }



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     FragmentActivity activity = (FragmentActivity) v.getContext();
           ///     Fragment myFragment = new fr_offers();
           //     activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
       container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager) container).removeView((View) object);
    }
}
