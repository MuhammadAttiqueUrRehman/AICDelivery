package com.aic.aicdelivery;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class filterDialog extends BottomSheetDialogFragment implements AsyncResponse, View.OnClickListener {
    private BottomSheetListener mListener;
    ImageButton btnclose, btncheck;
    ProgressBar progressBar13;
    IntroManager intromanager;
    HMCoreData myDB;
    providerInfo providerinfo;
    ArrayList<spinClass> servicelist = new ArrayList<spinClass>();
    serviceList adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());
        providerinfo = new providerInfo(getActivity());
        View v = inflater.inflate(R.layout.filter_dialog, container, false);
        btnclose = (ImageButton) v.findViewById(R.id.closedialog);
        btncheck = (ImageButton) v.findViewById(R.id.btncheck);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);
        recyclerView = (RecyclerView) v.findViewById(R.id.servicelist);
        fetchServices();

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Debugging", "Servicelist Count " + providerinfo.getAllServiceItems().size());
                if (providerinfo.getAllServiceItems().size() > 0) {
                    mListener.onButtonClicked("Y");
                } else {
                    mListener.onButtonClicked("");
//                    for (int i=0;i<providerinfo.getAllServiceItems().size();i++){
//                        Log.i("Debugging","Servicelist Count " +  providerinfo.getAllServiceItems().get(i));
//                    }
                }

                dismiss();
            }
        });
        //   mListener = (BottomSheetListener) getParentFragment();
        return v;
    }

    @Override
    public void onClick(View v) {

    }

   /* public void setListener(BottomSheetListener listener) {
        this.mListener = listener;
    }*/

    public interface BottomSheetListener {
        //void onButtonClicked(ArrayList<String> info);
        void onButtonClicked(String info);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Log.i("Debugging", "Came to Attach Fragment function");
            mListener = (BottomSheetListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }


    private void fetchServices() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMServiceList", jsonParam.toString()};
        Log.i("Debugging", "Servicelist " + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar13, "SSMServiceList");
            aasyncTask.delegate = (AsyncResponse) filterDialog.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        adapter = new serviceList(getContext(), servicelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        servicelist.clear();
        Log.i("Debugging", "Servicelist " + output);
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_item = myjson.getJSONArray("servicelist");
        Boolean mc = false;
        for (int i = 0; i < json_array_item.length(); i++) {
            JSONObject objects = json_array_item.getJSONObject(i);
            for (int m = 0; m < providerinfo.getAllServiceItems().size(); m++) {
                if (providerinfo.getAllServiceItems().get(m).equals(objects.getString("servicecode"))) {
                    mc = true;
                    break;
                } else {
                    mc = false;
                }
            }
            spinClass spin = new spinClass(objects.getString("servicename"), objects.getString("servicecode"), i, mc);
            servicelist.add(spin);

        }
        adapter.notifyDataSetChanged();
    }
}
