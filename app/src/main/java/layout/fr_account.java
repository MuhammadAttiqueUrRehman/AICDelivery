package layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;

import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class fr_account extends Fragment implements AsyncResponse {
    private CircleImageView pimage;
    private TextView username;
    private ScrollView scrlview;
    private ImageButton btnprofile, btnemployee, btnfinance, btnmessage, btnsettings, btnreferlist, btnsalesreport,btnorderstatus;
    private ProgressBar progressBar13;
    private HMCoreData myDB;
    byte[] decodedBytes;

    private IntroManager intromanager;

    /*line31a
    line61*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());

        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_fr_account, container, false);
        scrlview = (ScrollView) v.findViewById(R.id.accscroll);
        username = (TextView) v.findViewById(R.id.txtusername);
        pimage = (CircleImageView) v.findViewById(R.id.profileimage);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);
        btnprofile = (ImageButton) v.findViewById(R.id.btnprofile);
        btnemployee = (ImageButton) v.findViewById(R.id.btnemployee);
        btnfinance = (ImageButton) v.findViewById(R.id.btnfinance);
        btnmessage = (ImageButton) v.findViewById(R.id.btnmessage);
        btnsettings = (ImageButton) v.findViewById(R.id.btnsettings);
        btnorderstatus = v.findViewById(R.id.btnorderstatus);
        btnsalesreport = v.findViewById(R.id.btnsalesreport);
        //btnreferlist = (ImageButton) v.findViewById(R.id.btnreferlist);
        Main2Activity.ln.setVisibility(View.VISIBLE);
        username.setText("Welcome " + intromanager.getEmployeeName() + "!");

        decodedBytes = Base64.decode(intromanager.getServiceImage1(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        pimage.setImageBitmap(decodedByte);

        /*    Picasso.with(getContext())
                    .load(intromanager.getServiceImage1())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(pimage);*/


        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (intromanager.getCategory().equals("119")) {*/
                intromanager.setImageCount(0);
                Fragment myFragment = new fr_providerprofile1();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                /*} else {
                    Fragment myFragment = new fr_promoterupdate();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }*/
            }
        });

        btnemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_employeelist();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

//        btnreferlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment myFragment = new fr_refererdashboard();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
//            }
//        });

        btnmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_messages();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnfinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_wallet();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_settings();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnsalesreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_Sales_Report();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnorderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_OrderStatusReport();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        Log.i("Debugging Login Result", output);
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            intromanager.setLoginOP("");
            intromanager.setLogged(false);
            intromanager.setEmployeeID("");
            intromanager.setCertificate("");
            intromanager.setOutletID("");
            intromanager.setCheckinStatus("");
            intromanager.setCompanyType("");
            intromanager.setServiceImage1("");
            intromanager.setAdmin("");
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}


