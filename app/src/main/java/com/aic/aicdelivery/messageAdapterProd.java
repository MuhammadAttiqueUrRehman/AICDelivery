package com.aic.aicdelivery;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

import layout.fr_message_detail;
import layout.fr_messages;


public class messageAdapterProd extends RecyclerView.Adapter<messageAdapterProd.MyViewHolder> implements AsyncResponse{
    Context ctx;
    ArrayList<String> messageList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private String ccy;
    private ProgressBar progressBar4;
    private  IntroManager intromanager;

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.toString().equals("000")) {
           myDB.showToast(ctx, statusdesckey);
            return;
        } else {
            if (handle == "deletemessage") {
                myDB.showToast(ctx, "Notification deleted");
            }
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageButton read,delete;
        private TextView mday,message,message120,sequence;
        private CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardview=(CardView) itemView.findViewById(R.id.card_view);
            read=(ImageButton) itemView.findViewById(R.id.imageread);
            delete=(ImageButton) itemView.findViewById(R.id.imagedelete);
            mday=(TextView) itemView.findViewById(R.id.txtdate);
            message120=(TextView) itemView.findViewById(R.id.message120);
            message=(TextView) itemView.findViewById(R.id.message);
            sequence=(TextView) itemView.findViewById(R.id.sequence);
            progressBar4=(ProgressBar) itemView.findViewById(R.id.progressBar4);
        }
    }

    public messageAdapterProd(Context context,ArrayList<String> productList,String ccy) throws HMOwnException {
        this.ctx=context;
        this.messageList=productList;
        this.ccy=ccy;
         myDB = new HMCoreData(context);
         intromanager = new IntroManager(ctx);
    }

    @Override
    public messageAdapterProd.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(R.layout.message_cell,parent,false);

        Log.i("Debugging","Item Created in View Holder");
        return new messageAdapterProd.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final messageAdapterProd.MyViewHolder holder, final int position) {
        try {
            final JSONObject myjson = new JSONObject(messageList.get(position));

            holder.mday.setText(myjson.getString("mday").toString()+" - " +myjson.getString("mmonth").toString());
            holder.read.setColorFilter(myjson.getString("isread").toString().equals("1")?Color.parseColor("#17be2f"):Color.parseColor("#8e8b92"));
            holder.message120.setText( myjson.getString("narration120").toString());
            holder.message.setText( myjson.getString("narration").toString());
            holder.sequence.setText( myjson.getString("sequence").toString());

            //Add item
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();

                    try {
                        bundle.putString("mday", holder.mday.getText().toString());
                        bundle.putString("mmonth", myjson.getString("mmonth").toString());
                        bundle.putString("message", holder.message.getText().toString());
                        bundle.putString("sequence", holder.sequence.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Fragment myFragment = new fr_message_detail();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }
            });

            //subtract item
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("merchantcode", intromanager.getMerchantCode());
                        jsonMain.put("customer", intromanager.getEmployeeID());
                        jsonMain.put("certificate", intromanager.getCertificate());
                        jsonMain.put("reference", holder.sequence.getText().toString());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                       myDB.showToast(ctx,e.getMessage());
                        return;
                    }

                    messageList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, messageList.size());
                    if (messageList.size()==0){
                        fr_messages.fillcart.setVisibility(View.INVISIBLE);
                        fr_messages.emptycart.setVisibility(View.VISIBLE);
                    }


                    String[] myTaskParams = {"/SSMmessageDeleteCustomer", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        HMDataAccess aasyncTask = new HMDataAccess(ctx, progressBar4, "SSMmessageDeleteCustomer");
                        aasyncTask.delegate = (AsyncResponse) messageAdapterProd.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                       myDB.showToast(ctx,e.getMessage());
                        return;
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
           myDB.showToast(ctx,e.getMessage());
            return;
        }
        Log.i("Debugging","Binding in view holder " + position);
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }



}
