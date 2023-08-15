package layout;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.spinClass2;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class fr_OrderStatusReport extends Fragment implements DatePickerDialog.OnDateSetListener, AsyncResponse {

    private Toolbar mtoolbar;
    private ProgressBar progressBar11;
    private Button btnnext;
    private String[] status = new String[]{"New Order","Packing and preparation for load","Order Cancelled","Loaded to vehicle","Delivery Complete", "Balance Orders after partial delivery","Completed balance orders "};
    private EditText et_from_date, et_to_date;
    private Spinner spStatus, spRider;
    String tag = "";
    private Calendar myCalendar, tempCal;

    String fromDate, toDate, statusIndex, riderIndex;
    private IntroManager introManager;
    HMCoreData myDB;

    public fr_OrderStatusReport() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_status_report, container, false);

        mtoolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order Status Report");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Main2Activity.ln.setVisibility(View.GONE);
        introManager = new IntroManager(getContext());
        myDB = new HMCoreData(getContext());
        progressBar11 = v.findViewById(R.id.progressBar11);
        btnnext = v.findViewById(R.id.btnnext);
        et_to_date = v.findViewById(R.id.et_to_date);
        et_from_date = v.findViewById(R.id.et_from_date);
        spRider = v.findViewById(R.id.spRider);
        spStatus = v.findViewById(R.id.spStatus);
        myCalendar = Calendar.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, status);
        spStatus.setAdapter(adapter);
        setDefaultDate(et_from_date);
        setDefaultDate(et_to_date);

        ArrayAdapter<spinClass2> riderAdapter = new ArrayAdapter<spinClass2>(getActivity(), R.layout.spinner_layout, introManager.getAllEmployeesActive());
        spRider.setAdapter(riderAdapter);
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusIndex = String.valueOf(position+1);
                if (position == 0 || position == 1){
                    spRider.setEnabled(false);
                    riderIndex = "";
                }
                else {
                    spRider.setEnabled(true);
                }

                Log.d("OrderStatus","statusIndex : "+statusIndex);
                Log.d("OrderStatus","riderIndex : "+riderIndex);
                Log.d("OrderStatus","fromDate : "+fromDate);
                Log.d("OrderStatus","toDate : "+toDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spRider.isEnabled()){
                    riderIndex = introManager.getAllEmployees().get(position+1).toString();
                }

                Log.d("OrderStatus","statusIndex : "+statusIndex);
                Log.d("OrderStatus","riderIndex : "+riderIndex);
                Log.d("OrderStatus","fromDate : "+fromDate);
                Log.d("OrderStatus","toDate : "+toDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_from_date.setOnClickListener(v12 -> {
            tag = "from";
            Date currentTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime.getTime());
            int Day = calendar.get(Calendar.DAY_OF_MONTH);
            int  Month = calendar.get(Calendar.MONTH);
            int Year = calendar.get(Calendar.YEAR);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Year, Month, Day);
            dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            tempCal = Calendar.getInstance();
            tempCal.set(2023,0,1);
            dialog.getDatePicker().setMinDate(tempCal.getTimeInMillis());
            dialog.show();
        });

        et_to_date.setOnClickListener(v13 -> {
            tag = "to";
            Date currentTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime.getTime());
            int Day = calendar.get(Calendar.DAY_OF_MONTH);
            int  Month = calendar.get(Calendar.MONTH);
            int Year = calendar.get(Calendar.YEAR);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Year, Month, Day);
            dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            tempCal = Calendar.getInstance();
            tempCal.set(2023,0,1);
            dialog.getDatePicker().setMinDate(tempCal.getTimeInMillis());
            dialog.show();
        });


        btnnext.setOnClickListener(v1 -> sendEmail());

        return v;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String myFormat = "dd/M/yyyy";
        String remoteFormat = "ddMMyyyy000000";
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdformat1 = new SimpleDateFormat(remoteFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (tag.equals("from")){
            fromDate = sdformat1.format(myCalendar.getTime());
            et_from_date.setText(sdformat.format(myCalendar.getTime()));
        }
        else if (tag.equals("to")){
            toDate = sdformat1.format(myCalendar.getTime());
            et_to_date.setText(sdformat.format(myCalendar.getTime()));
        }
    }

    private void setDefaultDate(EditText editText){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("ddMMyyyy000000", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df1.format(c);
        fromDate = formattedDate1;
        toDate = formattedDate1;
        editText.setText(formattedDate);
    }

    private void sendEmail() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", introManager.getMerchantCode());
            jsonMain.put("mdevice", introManager.getDevice());
            jsonMain.put("employeeid", introManager.getEmployeeID());
            jsonMain.put("certificate", introManager.getCertificate());
            jsonMain.put("outlet", introManager.getOutletID());
            jsonMain.put("employeeaccount",riderIndex);
            jsonMain.put("status",statusIndex);
            jsonMain.put("fromdate",fromDate);
            jsonMain.put("todate",toDate);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(requireContext(), e.getMessage());
        }
        Log.i("OrderStatus", "Send Email :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMNewOrderListReport", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), progressBar11, "SSMNewOrderListReport");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        JSONObject jObj = new JSONObject(output);
        Log.d("debugging", output);
        if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), jObj.getString("response"));
            return;
        }
        else {
            myDB.showToast(getActivity(), "Success");
        }
    }
}