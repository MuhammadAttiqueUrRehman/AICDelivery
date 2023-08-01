package layout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_noservice extends Fragment {
    Button btnretry;

    public fr_noservice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_noservice, container, false);
        btnretry = (Button) v.findViewById(R.id.btnretry);

//        String number = "+97143958183";
        String number = new IntroManager(requireContext()).getSupportCall();

        TextView labelor = v.findViewById(R.id.labelor);
        labelor.setText("The service or access is currently not available online.  Please call us on " + number + ". Thank You.");


        btnretry.setOnClickListener(new View.OnClickListener() {
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
