package layout;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class fr_walkthrough_slide extends Fragment {
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotslayout;
    Button next, skip;

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = getActivity().getLayoutInflater().inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        //  return super.instantiateItem(container,position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layouts = new int[]{R.layout.walk_screen1, R.layout.walk_screen2, R.layout.walk_screen3, R.layout.walk_screen4};
        View v = inflater.inflate(R.layout.fragment_fr_walkthrough_slide, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        next = (Button) v.findViewById(R.id.btn_next);
        skip = (Button) v.findViewById(R.id.btn_skip);
        dotslayout = (LinearLayout) v.findViewById(R.id.layoutdots);
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);

                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    getActivity().getFragmentManager().popBackStack();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return v;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + 1;
    }

    private void addBottomDots(int position) {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.active_dot);
        int[] colorInActive = getResources().getIntArray(R.array.inactive_dot);
        dotslayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(colorInActive[position]);
            dotslayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(colorActive[position]);
        }

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                next.setText("PROCEED");
                skip.setVisibility(View.GONE);
            } else {
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
