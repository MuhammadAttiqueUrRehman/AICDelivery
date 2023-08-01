package com.aic.aicdelivery;

import android.content.Context;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class employeeAdapterSimple extends RecyclerView.Adapter<employeeAdapterSimple.CustomViewHolder> implements AsyncResponse {
    private Context context;
    private ArrayList<detailitemVO> items;
    private int cuslayout;
    private ProgressBar progressBar3;
    private IntroManager intromanager;
    private HMCoreData myDB;

    public employeeAdapterSimple(FragmentActivity activity, ArrayList<detailitemVO> items, int cuslayout) {
        this.context = activity;
        this.items = items;
        this.cuslayout = cuslayout;
        this.intromanager = new IntroManager(context);
        myDB = new HMCoreData(context);

    }

    @Override
    public employeeAdapterSimple.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final detailitemVO album = items.get(position);
        holder.empname.setText(album.getDetailq());
        holder.empstatus.setOnCheckedChangeListener(null);
        if (album.getStatus().equals("0")) {
            holder.card_view.setBackgroundColor(Color.DKGRAY);
            holder.empname.setBackgroundColor(Color.DKGRAY);
            holder.empstatus.setEnabled(false);
        } else {
            holder.empstatus.setChecked(true);
            if (album.getOption().equals("1")) {
                holder.empstatus.setChecked(true);
            } else {
                holder.empstatus.setChecked(false);
            }

            /*holder.empstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String mdata = holder.empstatus.isChecked() ? "1" : "0";
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", intromanager.getMerchantCode());
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("employeeid", intromanager.getEmployeeID());
                        jsonMain.put("certificate", intromanager.getCertificate());
                        jsonMain.put("outlet", intromanager.getOutletID());
                        jsonMain.put("employeeid4status", album.getDetailr());
                        jsonMain.put("status", mdata);
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(context, e.getMessage());
                    }
                    Log.i("Debugging", "Employee Status change :" + jsonParam.toString());
                    String[] myTaskParams = {"/SSMEmployeeCheckInOut", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        HMDataAccess aasyncTask = new HMDataAccess(context, progressBar3, "SSMEmployeeCheckInOut");
                        aasyncTask.delegate = (AsyncResponse) employeeAdapterSimple.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(context, e.getMessage());
                        return;
                    }
                }
            });*/

            Log.i("Debugging", "Employee Status Current :" + album.getDetailq() + album.getDetailr() + album.getOption());
            holder.empstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    String mdata = buttonView.isChecked() ? "1" : "0";
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", intromanager.getMerchantCode());
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("employeeid", intromanager.getEmployeeID());
                        jsonMain.put("certificate", intromanager.getCertificate());
                        jsonMain.put("outlet", intromanager.getOutletID());
                        jsonMain.put("employeeid4status", album.getDetailr());
                        jsonMain.put("status", mdata);
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(context, e.getMessage());
                    }
                    Log.i("Debugging", "Employee Status change :" + jsonParam.toString());
                    String[] myTaskParams = {"/SSMEmployeeCheckInOut", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        HMDataAccess aasyncTask = new HMDataAccess(context, progressBar3, "SSMEmployeeCheckInOut");
                        aasyncTask.delegate = (AsyncResponse) employeeAdapterSimple.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(context, e.getMessage());
                        return;
                    }
                }
            });
        }
    }

    public interface ClickEvent {
        void clickEventItem();
    }

    ClickEvent clickevent;

    public void setClickEvent(ClickEvent event) {
        this.clickevent = event;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.d("debugging", output);
        JSONObject jObj = new JSONObject(output);
        if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(context, jObj.getString("statusdesc"));
            return;
        }
        if (handle.equals("SSMEmployeeCheckInOut")) {
            clickevent.clickEventItem();
            //     myDB.showToast(context, "Employee Status Changed");
            return;
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView empname;
        Switch empstatus;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            empstatus = (Switch) view.findViewById(R.id.empstatus);
            empname = (TextView) view.findViewById(R.id.empname);
            card_view = (CardView) view.findViewById(R.id.card_view);
            progressBar3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        }
    }


}
