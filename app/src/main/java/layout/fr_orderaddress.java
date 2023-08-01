package layout;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class fr_orderaddress extends Fragment implements AsyncResponse {

    private LinearLayout.LayoutParams params,params1;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle,mSubTitle,itemname,itemsubname,la1,la2,la3,la4;
    private ProgressBar progressBar4a;
    private View v;
    private ImageView img;
    private TextView detailQ1,detailQ2,detailQ3,detailQ4,detailQ5,detailR1,detailR2,detailR3,detailR4,detailR5;
    private TextView pricedesc,priceamount,taxdesc,taxamount,totaldesc,totalamount,disclaimer;
    private IntroManager intromanager;
    private Button btndirection,btnpayment;
    private ScrollView accscroll;
    private String estimate,slat,slon,scoupon,scustomer,scustomermobile;


    public fr_orderaddress() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.addinfo, menu);
        return;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Inflate the layout for this fragment
        Main2Activity.ln.setVisibility(View.GONE);
        intromanager = new IntroManager(getActivity());
        myDB=new HMCoreData(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_orderaddress, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        progressBar4a = (ProgressBar) v.findViewById(R.id.progressBar4a);
        itemname=(TextView) v.findViewById(R.id.itemname);
        itemsubname=(TextView) v.findViewById(R.id.unitprice);
        img=(ImageView) v.findViewById(R.id.detailimage);
        btndirection=(Button) v.findViewById(R.id.btndirection);
        btnpayment=(Button) v.findViewById(R.id.btnpayment);
        accscroll=(ScrollView) v.findViewById(R.id.accscroll);
        detailQ1=(TextView) v.findViewById(R.id.detailQ1);
        detailQ2=(TextView) v.findViewById(R.id.detailQ2);
        detailQ3=(TextView) v.findViewById(R.id.detailQ3);
        detailQ4=(TextView) v.findViewById(R.id.detailQ4);
        detailQ5=(TextView) v.findViewById(R.id.detailQ5);
        detailR1=(TextView) v.findViewById(R.id.detailR1);
        detailR2=(TextView) v.findViewById(R.id.detailR2);
        detailR3=(TextView) v.findViewById(R.id.detailR3);
        detailR4=(TextView) v.findViewById(R.id.detailR4);
        detailR5=(TextView) v.findViewById(R.id.detailR5);
        pricedesc=(TextView) v.findViewById(R.id.pricedesc);
        priceamount=(TextView) v.findViewById(R.id.priceamount);
        taxdesc=(TextView) v.findViewById(R.id.taxdesc);
        taxamount=(TextView) v.findViewById(R.id.taxamount);
        totaldesc=(TextView) v.findViewById(R.id.totaldesc);
        totalamount=(TextView) v.findViewById(R.id.totalamount);
        disclaimer=(TextView) v.findViewById(R.id.disclaimer);
        la1=(TextView) v.findViewById(R.id.la1);
        la2=(TextView) v.findViewById(R.id.la2);
        la3=(TextView) v.findViewById(R.id.la3);
        la4=(TextView) v.findViewById(R.id.la4);

        if (getArguments().getString("orderstatus").equals("1")){
            btnpayment.setEnabled(false);
        }

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("orderid", getArguments().getString("orderid"));
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return null;
        }
        Log.i("Debugging", "Fetch Detail Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMFetchOrderVendor", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMFetchOrderVendor");
            aasyncTask.delegate = (AsyncResponse) fr_orderaddress.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return null;
        }

        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Float serviceamount=(timeline.getPayable()) + (timeline.getPayable()*(timeline.getSGST()/100)) + (timeline.getPayable()*(timeline.getCGST()/100));
                //   final Float total=serviceamount+Float.parseFloat(timeline.getMaterialCharges())+Float.parseFloat(timeline.getMaterialGST());
                //   intromanager.setVisitingCharges(String.valueOf(total));
                //   Log.i("Debugging","Service Amount :" + serviceamount.toString());
                //   Log.i("Debugging","Total Amount :" + total.toString());
                Bundle bundle = new Bundle();
                bundle.putString("vendorid", intromanager.getOutletID());
                bundle.putString("charges", scoupon);
                bundle.putString("customer", scustomer);
                bundle.putString("orderid", getArguments().getString("orderid"));
                bundle.putString("itemname", itemname.getText().toString());
                Fragment myFragment = new fr_vendorratingpay();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btndirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     if (!isLocationEnabled(getActivity())){
           //         statusCheck();
          //      }
                Bundle bundle = new Bundle();
                bundle.putString("lat", slat);
                bundle.putString("lon",slon);
                Fragment myFragment = new fr_direction();
                myFragment.setArguments(bundle);
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        accscroll.setVisibility(GONE);
        return v;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your device location services seems to be disabled, You need to enable location for KihdmaNow to work. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                       // finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        JSONObject object1 = new JSONObject(output);

        if (!object1.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), object1.getString("statusdesc"));
            return;
        } else if(handle.equals("SSMFetchOrderVendor")) {
            JSONArray mjarray = object1.getJSONArray("documentlist");
            JSONObject mjson = mjarray.getJSONObject(0);

            intromanager.setAdditionalInfo(mjson.getString("details"));
            intromanager.setServiceImage1(mjson.getString("image1"));
            intromanager.setServiceImage2(mjson.getString("image2"));
            intromanager.setServiceImage3(mjson.getString("image3"));
            intromanager.setServiceImage4(mjson.getString("image4"));
            JSONArray myarray = object1.getJSONArray("catalog");
            JSONObject myjson = myarray.getJSONObject(0);
            itemname.setText(myjson.getString("itemname"));
            //  itemsubname.setText(myjson.getString("unitprice"));
            Picasso.with(getContext())
                    .load(myjson.getString("itemimage"))
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(img);
            if (myjson.getString("question1").length()==0){
                detailQ1.setVisibility(View.INVISIBLE);
                detailR1.setVisibility(View.INVISIBLE);
            }
            if (myjson.getString("question2").length()==0){
                detailQ2.setVisibility(View.INVISIBLE);
                detailR2.setVisibility(View.INVISIBLE);
            }
            if (myjson.getString("question3").length()==0){
                detailQ3.setVisibility(View.INVISIBLE);
                detailR3.setVisibility(View.INVISIBLE);
            }
            if (myjson.getString("question4").length()==0){
                detailQ4.setVisibility(View.INVISIBLE);
                detailR4.setVisibility(View.INVISIBLE);
            }
            if (myjson.getString("question5").length()==0){
                detailQ5.setVisibility(View.INVISIBLE);
                detailR5.setVisibility(View.INVISIBLE);
            }

            detailQ1.setText(myjson.getString("question1"));
            detailR1.setText(myjson.getString("response1"));
            detailQ2.setText(myjson.getString("question2"));
            detailR2.setText(myjson.getString("response2"));
            detailQ3.setText(myjson.getString("question3"));
            detailR3.setText(myjson.getString("response3"));
            detailQ4.setText(myjson.getString("question4"));
            detailR4.setText(myjson.getString("response4"));
            detailQ5.setText(myjson.getString("question5"));
            detailR5.setText(myjson.getString("response5"));
            estimate=myjson.getString("directnego");
            intromanager.setCustomerCall(myjson.getString("customermobile"));
            if (myjson.getString("directnego").equals("1")){
                params = (LinearLayout.LayoutParams) pricedesc.getLayoutParams();
                params.weight = 1f;
                params1 = (LinearLayout.LayoutParams) priceamount.getLayoutParams();
                params1.weight = 0f;
                pricedesc.setLayoutParams(params);
                priceamount.setLayoutParams(params1);
                //    pricedesc.setTextColor(getResources().R.color.textcolor1);
                pricedesc.setText("Actual Quote will be provided after inspection");
                params = (LinearLayout.LayoutParams) taxdesc.getLayoutParams();
                params.weight = 1f;
                params1 = (LinearLayout.LayoutParams) taxamount.getLayoutParams();
                params1.weight = 0f;
                taxdesc.setLayoutParams(params);
                taxamount.setLayoutParams(params1);
                //   pricedesc.setTextColor(getResources().R.color.textcolor2);
                taxdesc.setText("A VAT of 5% will be applied to estimate amount provided by the vendor");
                totaldesc.setText("");
                totalamount.setText("");
            }else{
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                pricedesc.setText("Fixed Price");
                taxdesc.setText("VAT 5%");
                priceamount.setText(getString(R.string.Rs)  + " " +  formatter.format(Double.parseDouble(myjson.getString("catalogprice"))));
                taxamount.setText(getString(R.string.Rs)  + " " + formatter.format(Double.parseDouble(myjson.getString("sgst"))+Double.parseDouble(myjson.getString("cgst"))));
                totaldesc.setText("Total Amount");
                totalamount.setText(getString(R.string.Rs)  + " " +  formatter.format(Double.parseDouble(myjson.getString("catalogprice"))+Double.parseDouble(myjson.getString("sgst"))+Double.parseDouble(myjson.getString("cgst"))));
            }
            disclaimer.setText("*A visiting charges of " + getString(R.string.Rs)  + " " + intromanager.getVisitingCharges() + " plus VAT should be paid to the vendor incase job is not confirmed" );
            la1.setText("Order ID: " + myjson.getString("orderid"));
            la2.setText("Order Date: " + myjson.getString("orderdate"));
            la3.setText("Customer Name : " + myjson.getString("customername"));
            la4.setText("Address : " + myjson.getString("address"));
            slat = myjson.getString("lat");
            slon = myjson.getString("lon");
            scoupon = myjson.getString("coupon");
            scustomer = myjson.getString("customer");
            scustomermobile = myjson.getString("customermobile");
        }
        accscroll.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }else if (id == R.id.menu_contactcustomer) {
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+ intromanager.getCustomerCall())));
        } else if (id == R.id.action_image) {
                Fragment myFragment= new fr_addInfoView();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
        return super.onOptionsItemSelected(items);
    }

}
