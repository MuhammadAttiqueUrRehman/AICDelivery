package com.aic.aicdelivery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import layout.newfiles.DetailFragment;

public class timeLineAdapterMissed extends RecyclerView.Adapter<timeLineAdapterMissed.CustomViewHolder> {
    private Context context;
    private ArrayList<timeLineVO> items;
    private int cuslayout;
    private IntroManager intromanager;
    private HMCoreData myDB;
    public AppCompatActivity activity;

    public timeLineAdapterMissed(FragmentActivity activity, ArrayList<timeLineVO> items, int cuslayout) throws HMOwnException {
        this.context = activity;
        this.items = items;
        this.cuslayout = cuslayout;
        this.intromanager = new IntroManager(context);
        this.myDB = new HMCoreData(context);

    }

    @Override
    public timeLineAdapterMissed.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new timeLineAdapterMissed.CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }


    @Override
    public void onBindViewHolder(final timeLineAdapterMissed.CustomViewHolder holder, int position) {
        LinearLayout.LayoutParams params1;
        final timeLineVO timeline = items.get(position);
        holder.ordernumber.setText("" + timeline.getOrderNumber());
        holder.orderdate.setText("" + timeline.getOrderDate());

        holder.deliverydate.setText("" + timeline.getDeliverydate());
        holder.payment.setText("" + timeline.getPaymenttype());
        holder.number_of_items.setText("" + timeline.getNumberofitems());
        holder.supplier_code.setText("" + timeline.getVendorcode());
        holder.delivery_veh_code.setText("" + timeline.getVehiclename());
        holder.delivery_address.setText("" + timeline.getAddress());
        holder.customer_contact.setText("" + timeline.getMobilenumber());
        holder.status.setText("" + timeline.getOrderstatus());
        holder.giftmessage.setText( timeline.getGiftmessage());

        /*reset all buttons*/
        resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);

        holder.showaddress.setVisibility(View.VISIBLE);
        holder.btndetail.setVisibility(View.VISIBLE);
        holder.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) v.getContext();
                Bundle bundle = new Bundle();
//                bundle.putString("orderid", timeline.getOrderNumber());
//                bundle.putString("orderstatus", timeline.getStatus());
                Fragment myFragment = DetailFragment.newInstance(timeline, false, timeLineAdapter.TRANSIT_ORDER);
                ((DetailFragment) myFragment).show_delivery_button = true;
//                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        Log.i("Debugging", "Current Status :" + timeline.getStatus());
        Log.i("Debugging", "Max Status :" + timeline.getMaxStatus());

        holder.showaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Show Address", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + timeline.getGeolocation()));
//                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView ordernumber, orderdate, deliverydate, payment, number_of_items, supplier_code, delivery_veh_code,
                delivery_address, customer_contact, delivery_time, status,giftmessage;
        private Button btncancel, btndetail, btnaccept, btnclose, btncomplain, btnreject, btninactive, btnchangedelivery, showaddress;
        private ProgressBar progressBar4;
        private View card_view;

        public CustomViewHolder(View v) {
            super(v);

            ordernumber = (TextView) v.findViewById(R.id.ordernumber);
            orderdate = (TextView) v.findViewById(R.id.orderdate);
            deliverydate = (TextView) v.findViewById(R.id.deliverydate);
            payment = (TextView) v.findViewById(R.id.payment);
            number_of_items = (TextView) v.findViewById(R.id.number_of_items);
            supplier_code = (TextView) v.findViewById(R.id.supplier_code);
            delivery_veh_code = (TextView) v.findViewById(R.id.delivery_veh_code);
            delivery_address = (TextView) v.findViewById(R.id.delivery_address);
            customer_contact = (TextView) v.findViewById(R.id.customer_contact);
            delivery_time = (TextView) v.findViewById(R.id.delivery_time);
            status = (TextView) v.findViewById(R.id.status);
            giftmessage = (TextView) v.findViewById(R.id.giftmessage);

            btncancel = (Button) v.findViewById(R.id.btncancel);
            btndetail = (Button) v.findViewById(R.id.btnprofile);
            btnaccept = (Button) v.findViewById(R.id.btnotp);
            btnclose = (Button) v.findViewById(R.id.btnclose);
            btncomplain = (Button) v.findViewById(R.id.btncomplain);
            btnreject = (Button) v.findViewById(R.id.btnreject);
            btninactive = (Button) v.findViewById(R.id.btninactive);
            btnchangedelivery = (Button) v.findViewById(R.id.btnchangedelivery);
            showaddress = (Button) v.findViewById(R.id.showaddress);
            progressBar4 = (ProgressBar) itemView.findViewById(R.id.progressBar4);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    private void resetButton(Button btninactive, Button btnprofile, Button btnotp, Button btncancel, Button btnreject, Button btncomplain, Button btnclose, Button btnchangedelivery) {

        btninactive.setVisibility(View.GONE);
        btnprofile.setVisibility(View.GONE);
        btnotp.setVisibility(View.GONE);
        btncancel.setVisibility(View.GONE);
        btnreject.setVisibility(View.GONE);
        btncomplain.setVisibility(View.GONE);
        btnclose.setVisibility(View.GONE);
        btnchangedelivery.setVisibility(View.GONE);

       /* LinearLayout.LayoutParams params1;
        params1 = (LinearLayout.LayoutParams) btninactive.getLayoutParams();
        params1.weight = 0f;
        btninactive.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnprofile.getLayoutParams();
        params1.weight = 0f;
        btnprofile.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnotp.getLayoutParams();
        params1.weight = 0f;
        btnotp.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btncancel.getLayoutParams();
        params1.weight = 0f;
        btncancel.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnreject.getLayoutParams();
        params1.weight = 0f;
        btnreject.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btncomplain.getLayoutParams();
        params1.weight = 0f;
        btncomplain.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnclose.getLayoutParams();
        params1.weight = 0f;
        btnclose.setLayoutParams(params1);*/

    }
}
