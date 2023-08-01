package layout;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aic.aicdelivery.R;


public class fr_error extends Fragment {
    private Button btnretry;
    private TextView txterr;
    private Toolbar mtoolbar;
    public TextView mTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fr_error, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.tbActivityMain);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        txterr = (TextView) v.findViewById(R.id.labelor1);
        btnretry=(Button) v.findViewById(R.id.btnretry);
        mTitle.setText("Unrecoverable Error");
        txterr.setText(getArguments().getString("error"));

        //start Shopping
        btnretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        return v;
    }

}
