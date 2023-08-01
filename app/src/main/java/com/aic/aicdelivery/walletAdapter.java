package com.aic.aicdelivery;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class walletAdapter extends RecyclerView.Adapter<walletAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<walletVO> items;
    private int cuslayout;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private HMAppVariables appVariables;
    public AppCompatActivity activity;

    public walletAdapter(FragmentActivity activity, ArrayList<walletVO> items, int cuslayout) throws HMOwnException {
        this.context = activity;
        this.items = items;
        this.cuslayout=cuslayout;
        this.intromanager = new IntroManager(context);
        this.myDB = new HMCoreData(context);
        this.appVariables=myDB.getUserData();
    }
    @Override
    public walletAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new walletAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(walletAdapter.CustomViewHolder holder, int position) {
        final walletVO wallet = items.get(position);
        holder.date.setText(wallet.getDate());
        holder.narration1.setText(wallet.getNarration1());
        holder.narration2.setText(wallet.getNarration2());
        holder.amount.setText(wallet.getAmount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView date,narration1,narration2,amount;
        public CustomViewHolder(View v) {
            super(v);

            date= (TextView) v.findViewById(R.id.date);
            narration1= (TextView) v.findViewById(R.id.narration1);
            narration2= (TextView) v.findViewById(R.id.narration2);
            amount= (TextView) v.findViewById(R.id.amount);
        }
    }
}
