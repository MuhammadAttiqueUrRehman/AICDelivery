package com.aic.aicdelivery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;

public class serviceList extends RecyclerView.Adapter<serviceList.ViewHolder> {
    Context ctx;
    private ArrayList<spinClass> items = new ArrayList<>();
    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    private ArrayList<Integer> intpos = new ArrayList<>();
    public serviceList(Context context,ArrayList<spinClass> items) throws HMOwnException {
        this.ctx=context;
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.service_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);





    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView mCheckedTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.check);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            // use the sparse boolean array to check
            final spinClass spin = items.get(position);
            final providerInfo providerinfo=new providerInfo(ctx);
            if(!intpos.contains(position)) {
                Log.i("Debugging", "Service list Selected " + String.valueOf(spin.getName()) + "    " + spin.getChecked());
                itemStateArray.put(position, spin.getChecked());
               intpos.add(position);
            }
           // mCheckedTextView.setChecked(spin.getChecked());

            if (!itemStateArray.get(position, false)) {
                mCheckedTextView.setChecked(false);}
            else {
                mCheckedTextView.setChecked(true);
            }
            //loading
            mCheckedTextView.setText(String.valueOf(spin.getId()));
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            final spinClass spin = items.get(adapterPosition);
            final providerInfo providerinfo=new providerInfo(ctx);
            if (!itemStateArray.get(adapterPosition, false)) {
                mCheckedTextView.setChecked(true);
                providerinfo.addServiceItem(spin.getName());
                itemStateArray.put(adapterPosition, true);
            }
            else  {
                mCheckedTextView.setChecked(false);
                providerinfo.removeServiceItem(spin.getName());
                itemStateArray.put(adapterPosition, false);
            }
        }

    }
}



