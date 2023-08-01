package layout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_checkout_confirm_payment extends Fragment {
    private TextView lblorder,lblmessage,mTitle;
    private Button btnhome;
    private Toolbar mtoolbar;

    public fr_checkout_confirm_payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_checkout_confirm_payment, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment Complete");
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(79,7,0), PorterDuff.Mode.SRC_IN);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        lblmessage = (TextView) v.findViewById(R.id.lblmessage);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        lblorder.setText(getArguments().getString("orderref"));
        lblmessage.setText(getArguments().getString("message"));

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        return v;
    }

}

