package layout;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.aic.aicdelivery.R;


public class fr_checkout_confirm extends Fragment {
    private Button btnhome;
    private Toolbar mtoolbar;
    private TextView lblcoupon,lblorder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fr_checkout_confirm, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("title"));
        btnhome = (Button) v.findViewById(R.id.btnhome);
        lblcoupon=(TextView) v.findViewById(R.id.lblcoupon);
        lblorder=(TextView) v.findViewById(R.id.lblorder);
        lblcoupon.setText(getArguments().getString("orderref"));
        lblorder.setText(getArguments().getString("message"));

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_neworders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        return v;
    }


}
