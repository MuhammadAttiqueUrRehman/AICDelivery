package layout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_cancel extends Fragment {
    private TextView lblorder,lblcoupon;
    private Button btnhome;
    private Toolbar mtoolbar;
    private String message,message1;

    public fr_cancel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fr_cancel, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Booking Cancelled");
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        btnhome = (Button) v.findViewById(R.id.btnhome);

        Log.i("Debugging","Order id :" + getArguments().getString("orderid"));
        Log.i("Debugging","deliverydate :" + getArguments().getString("deliverydate"));

        message="Your booking # " + getArguments().getString("orderid") + " has been cancelled on your request.";


        lblorder.setText(message);

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         Fragment myFragment = new fr_myorders();
     //           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }

}
