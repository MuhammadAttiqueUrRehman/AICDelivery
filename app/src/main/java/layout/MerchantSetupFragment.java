package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MerchantSetupFragment extends Fragment implements AsyncResponse {

    private OnFragmentInteractionListener mListener;

    private Fragment fragment;
    IntroManager intromanager;
    ProgressBar progressBar8;

    public void showFragment(Fragment fr) {
        Fragment myFragment = fr;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
    }


    public MerchantSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager = new IntroManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_marchant_setup, container, false);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()){
                    Toast.makeText(requireContext(), "BackPressed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressBar8 = (ProgressBar) v.findViewById(R.id.progressBar8);
        final EditText et_marchant = v.findViewById(R.id.et_marchant);
        final EditText et_token = v.findViewById(R.id.et_token);
//        v.findViewById(R.id.card_view);
        v.findViewById(R.id.btnverify).setOnClickListener(v1 -> {
            String merchant_code = et_marchant.getText().toString(),
                    token = et_token.getText().toString();
            if (merchant_code.length() < 1) {
                Toast.makeText(requireContext(), "Enter merchat code!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (token.length() < 1) {
                Toast.makeText(requireContext(), "Enter token code!", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", merchant_code);
                jsonMain.put("reference", token);
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] myTaskParams = {"/SSMValidateNewMerchant", jsonParam.toString()};
            Log.i("Debugging", jsonParam.toString());
            try {
                HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "verifyMerchantCode");
                aasyncTask.delegate = (AsyncResponse) MerchantSetupFragment.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.d("Debugging", "response " + output);
        JSONObject jObj = new JSONObject(output);
        if (!jObj.getString("statuscode").equals("000")) {
            Toast.makeText(getActivity(), jObj.getString("statusdesc"), Toast.LENGTH_SHORT).show();
            return;
        } else if (handle.equals("verifyMerchantCode")) {
            intromanager.setMerchantName(jObj.getString("merchantname"));
            intromanager.setMerchantCode(jObj.getString("merchantcode"));
            Log.i("Debugging", "Came to Nexttime");
            intromanager.setIsInit(true);
            if (getActivity() instanceof MainActivity) {
                MainActivity.firsttime = "";
                ((MainActivity) getActivity()).initVendor();
            }

//            fragment = new fr_login();
//            showFragment(fragment);
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }




}
