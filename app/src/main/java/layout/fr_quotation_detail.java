package layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class fr_quotation_detail extends Fragment implements AsyncResponse {
    private LinearLayout.LayoutParams params,params1;
    private HMCoreData myDB;
    private TextView itemname,itemsubname;
    private ProgressBar progressBar4a;
    private View v;
    private ImageView img;
    private TextView detailQ1,detailQ2,detailQ3,detailQ4,detailQ5,detailR1,detailR2,detailR3,detailR4,detailR5;
    private TextView pricedesc,priceamount,taxdesc,taxamount,totaldesc,totalamount,disclaimer;
    private IntroManager intromanager;
    private Button btnaccept,btndecline;
    private ScrollView accscroll;
    private String estimate,qualcode;
    private Float mincharges;
    private EditText laborcharges,materialcharges,materialstategst,materialcentralgst,txtremarks;

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Fragment myFragment;
        int id = items.getItemId();
         if(id == R.id.action_image) {
            myFragment= new fr_addInfoView();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (id == R.id.action_image) {
             startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+ intromanager.getCustomerCall())));
         }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.addinfo, menu);
    }

    public fr_quotation_detail() {
        // Required empty public constructor
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
        v = inflater.inflate(R.layout.fragment_fr_quotation_detail, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quotation");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);

        progressBar4a = (ProgressBar) v.findViewById(R.id.progressBar4a);
        itemname=(TextView) v.findViewById(R.id.itemname);
        itemsubname=(TextView) v.findViewById(R.id.unitprice);
        img=(ImageView) v.findViewById(R.id.detailimage);
        btnaccept=(Button) v.findViewById(R.id.btnaccept);
        btndecline=(Button) v.findViewById(R.id.btndecline);
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
        laborcharges=(EditText) v.findViewById(R.id.txtlaboramount);
        materialcharges=(EditText) v.findViewById(R.id.txtmaterialamount);
        materialstategst=(EditText) v.findViewById(R.id.txtstategst);
        materialcentralgst=(EditText) v.findViewById(R.id.txtcentralgst);
        txtremarks=(EditText) v.findViewById(R.id.txtremarks);
        intromanager.setAdditionalInfo("");
        if (getArguments() != null) {
            intromanager.setOrderId(getArguments().getString("orderid"));
        }
        intromanager.setServiceImage1("");
        intromanager.setServiceImage2("");
        intromanager.setServiceImage3("");
        intromanager.setServiceImage4("");
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("orderid",intromanager.getOrderId());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }
        Log.i("Debugging", "Fetch Detail Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMFetchOrderVendor", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMFetchOrderVendor");
            aasyncTask.delegate = (AsyncResponse) fr_quotation_detail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment= new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (laborcharges.length()==0){
                    laborcharges.setError("Service Charges should have a value");
                    return;
                }
                if (materialcharges.length()==0){
                    materialcharges.setError("Material Charges should have a value or 0");
                    return;
                }
                if (materialstategst.length()==0){
                    materialstategst.setError("Material VAT should have a value or 0");
                    return;
                }
                if (materialcentralgst.length()==0){
                    materialcentralgst.setError("Material VAT should have a value or 0");
                    return;
                }
                if (mincharges>Float.parseFloat(laborcharges.getText().toString())){
                    materialcentralgst.setError("Service Charges cannot be below the catalog price");
                    return;
                }
                if (txtremarks.getText().toString().trim().equals("")){
                    txtremarks.setError("Please provide quotation details");
                    return;
                }
        /*
                myrow.Add("laboramount", oOrderDetailVO.itmOrdQty * oOrderDetailVO.itmOrdAmt)
                myrow.Add("materialamount", oOrderDetailVO.itmMetAmt)
                myrow.Add("materialstategst", oOrderDetailVO.itmMtStGt)
                myrow.Add("materialcentralgst", oOrderDetailVO.itmMtCtGt)
         */

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("laboramount", laborcharges.getText().toString());
                    jsonMain.put("materialamount", materialcharges.getText().toString());
                    jsonMain.put("materialstategst", materialstategst.getText().toString());
                    jsonMain.put("materialcentralgst", materialcentralgst.getText().toString());
                    jsonMain.put("reference", txtremarks.getText().toString());
                    jsonMain.put("orderid", intromanager.getOrderId());
                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.alertBox(getContext(), e.getMessage());
                }
                Log.i("Debugging"," Order Accept Input :" + jsonParam.toString());
                String[] myTaskParams = {"/SSMSubmitQuotation", jsonParam.toString()};
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4a, "SSMSubmitQuotation");
                    aasyncTask.delegate = (AsyncResponse) fr_quotation_detail.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.alertBox(getContext(), e.getMessage());
                }
                //   FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                //   Fragment myFragment = new fr_qapage1();
                //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        accscroll.setVisibility(GONE);
        return v;
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

            final Float laboramt = Float.parseFloat(myjson.getString("laboramount"));
            final Float materialamt = Float.parseFloat(myjson.getString("materialamount"));
            final Float sgst = Float.parseFloat(myjson.getString("materialstategst"));
            final Float cgst = Float.parseFloat(myjson.getString("materialcentralgst"));
            mincharges=laboramt;
            laborcharges.setText(String.format("%.02f", laboramt));
            materialcharges.setText(String.format("%.02f", materialamt));
            materialstategst.setText(String.format("%.02f", sgst));
            materialcentralgst.setText(String.format("%.02f", cgst));
            txtremarks.setText(myjson.getString("remarks"));

            JSONArray mjarray = object1.getJSONArray("documentlist");
            JSONObject mjson = mjarray.getJSONObject(0);

            intromanager.setAdditionalInfo(mjson.getString("details"));
            intromanager.setServiceImage1(mjson.getString("image1"));
            intromanager.setServiceImage2(mjson.getString("image2"));
            intromanager.setServiceImage3(mjson.getString("image3"));
            intromanager.setServiceImage4(mjson.getString("image4"));


        } else if(handle == "SSMAcceptServiceOrder") {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "");
            bundle.putString("message", "Order Accepted, arrange visit to the customer. You can call the customer from the orders page.");
            bundle.putString("title", "Order Accept Confirmation");
            Fragment myFragment = new fr_checkout_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }   else if(handle == "SSMDeclineServiceOrder") {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "");
            bundle.putString("title", "Order Decline Confirmation");
            bundle.putString("message", "Order Declined, declining orders will impact your ratings and you will be given last priority while assigning new orders.");
            Fragment myFragment = new fr_checkout_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }  else if(handle == "SSMSubmitQuotation") {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "");
            bundle.putString("title", "Quotation Submit Confirmation");
            bundle.putString("message", "Quotation  updated successfully.");
            Fragment myFragment = new fr_checkout_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
        accscroll.setVisibility(View.VISIBLE);
    }

}
