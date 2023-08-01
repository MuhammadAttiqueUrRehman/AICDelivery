package com.aic.aicdelivery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Administrator on 09-07-2017.
 */

public class historyorderAdapterProd  extends RecyclerView.Adapter<historyorderAdapterProd.MyViewHolder> implements AsyncResponse {
    Context ctx;
    ArrayList<String> messageList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private String ccy;
    private ProgressBar progressBar4;
    private HMAppVariables hmAppVariables=new HMAppVariables();


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {

    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtdate,txtorder,txtpayment,txtamount,txtstatus;


        public MyViewHolder(View v) {
            super(v);
            txtdate = (TextView) v.findViewById(R.id.txtdate);
            txtorder = (TextView) v.findViewById(R.id.txtorder);
            txtpayment = (TextView) v.findViewById(R.id.txtpayment);
            txtamount = (TextView) v.findViewById(R.id.txtamount);
            txtstatus = (TextView) v.findViewById(R.id.txtstatus);
            progressBar4=(ProgressBar) v.findViewById(R.id.progressBar4);
        }
    }

    public historyorderAdapterProd(Context context,ArrayList<String> productList,String ccy) throws HMOwnException {
        this.ctx=context;
        this.messageList=productList;
        this.ccy=ccy;
        myDB = new HMCoreData(context);
        hmAppVariables=myDB.getUserData();
    }

    @Override
    public historyorderAdapterProd.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(R.layout.historyorder_cell,parent,false);
        Log.i("Debugging","Item Created in View Holder");
        return new historyorderAdapterProd.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final historyorderAdapterProd.MyViewHolder holder, final int position) {
        try {
            final JSONObject myjson = new JSONObject(messageList.get(position));
            holder.txtdate.setText(myjson.getString("mday").toString() + " - " + myjson.getString("mmonth").toString());
            holder.txtorder.setText(myjson.getString("orderid").toString());
            holder.txtpayment.setText("Payment Type :" + myjson.getString("paymenttype").toString());
            holder.txtamount.setText("Amount :" + myjson.getString("paymentamount").toString());
            holder.txtstatus.setText("Status :" + myjson.getString("deliverstatus").toString());



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
