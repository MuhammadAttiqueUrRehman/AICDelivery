package layout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_addInfoView extends Fragment {
    TextView infdetail;
    ImageView image1,image2,image3,image4;

    public fr_addInfoView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntroManager intromanager = new IntroManager(getActivity());
        View v= inflater.inflate(R.layout.fr_addinfoimageviewer,container,false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Additional Information");
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        infdetail = v.findViewById(R.id.infdetail);
        image1 = v.findViewById(R.id.image1);
        image2 = v.findViewById(R.id.image2);
        image3 = v.findViewById(R.id.image3);
        image4 = v.findViewById(R.id.image4);

        infdetail.setText(intromanager.getAdditionalInfo());

        //  Bitmap bmap = BitmapFactory.decodeResource(getResources(),R.drawable.haal_meer_large);


          if ( intromanager.getServiceImage1().length()>0){
            Picasso.with(getContext())
                    .load(intromanager.getServiceImage1())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image1);
        }

        if ( intromanager.getServiceImage2().length()>0){
            Picasso.with(getContext())
                    .load(intromanager.getServiceImage2())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image2);
        }

        if ( intromanager.getServiceImage3().length()>0){
            Picasso.with(getContext())
                    .load(intromanager.getServiceImage3())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image3);
        }

        if ( intromanager.getServiceImage4().length()>0){
            Picasso.with(getContext())
                    .load(intromanager.getServiceImage4())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image4);
        }

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                    getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
        });
        return v;
    }
}
