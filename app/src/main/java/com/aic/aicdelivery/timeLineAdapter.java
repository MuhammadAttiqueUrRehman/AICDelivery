package com.aic.aicdelivery;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import layout.fr_cancel;
import layout.newfiles.DetailFragment;

public class timeLineAdapter extends RecyclerView.Adapter<timeLineAdapter.CustomViewHolder> implements AsyncResponse {
    private Context context;
    private ArrayList<timeLineVO> items;
    private int cuslayout;
    private IntroManager intromanager;
    private HMCoreData myDB;
    public AppCompatActivity activity;

    final public static int NEW_ORDER = 0, COMPLETE_ORDER = 1, ONLOAD_ORDER = 2, TRANSIT_ORDER = 3, BALANCE_ORDER = 4;
    private int fragment_No = -1;
    boolean show_save;
    Dialog dialog = null;

    Fragment frag_ref;

    public void setFrag_ref(Fragment frag_ref) {
        this.frag_ref = frag_ref;
    }

    public timeLineAdapter(FragmentActivity activity, ArrayList<timeLineVO> items, int cuslayout, boolean show_save, int from) throws HMOwnException {
        this.context = activity;
        this.items = items;
        this.cuslayout = cuslayout;
        this.intromanager = new IntroManager(context);
        this.myDB = new HMCoreData(context);
        this.show_save = show_save;
        this.fragment_No = from;
    }

    @Override
    public timeLineAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new timeLineAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(final timeLineAdapter.CustomViewHolder holder, final int position) {

        final timeLineVO timeline = items.get(position);
        holder.ordernumber.setText("" + timeline.getOrderNumber());
        holder.orderdate.setText("" + timeline.getOrderDate());
        /*deliverydate, payment, number_of_items, supplier_code, delivery_veh_code,
                delivery_address, customer_contact-*/
        holder.deliverydate.setText("" + timeline.getDeliverydate());
        holder.payment.setText("" + timeline.getPaymenttype());
        holder.number_of_items.setText("" + timeline.getNumberofitems());
        holder.supplier_code.setText("" + timeline.getVendorcode());
        holder.delivery_veh_code.setText("" + timeline.getVehiclename());
        holder.delivery_address.setText("" + timeline.getAddress());
        holder.customer_contact.setText("" + timeline.getMobilenumber());
        holder.delivery_time.setText("" + timeline.getDeliveryleadtime());
        holder.status.setText("" + timeline.getOrderstatus());
        holder.remarks.setText(timeline.getRemarks());
        holder.giftmessage.setText(timeline.getGiftmessage());
        holder.customername.setText(timeline.getCustomerName());
        Log.i("Debugging", "Current Status :" + timeline.getStatus());
        Log.i("Debugging", "Max Status :" + timeline.getMaxStatus());
      /*  if (timeline.getStatus().equals(timeline.getMaxStatus())) {
            if (timeline.getStatus().equals("8")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
                holder.btninactive.setVisibility(View.VISIBLE);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("7")) {
//                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
//                holder.btninactive.setVisibility(View.VISIBLE);
//                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("6")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);

                holder.btninactive.setVisibility(View.VISIBLE);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("5")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);

                holder.btninactive.setVisibility(View.VISIBLE);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("4")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
                holder.btnreject.setVisibility(View.VISIBLE);
            } else if (timeline.getStatus().equals("3")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
                holder.btninactive.setVisibility(View.VISIBLE);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("2")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
//                holder.btninactive.setVisibility(View.VISIBLE);
//                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("1")) {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
                holder.btndetail.setVisibility(View.VISIBLE);
                holder.btncancel.setVisibility(View.VISIBLE);
                holder.btnreject.setVisibility(View.VISIBLE);
                holder.btnchangedelivery.setVisibility(View.VISIBLE);
            } else {
                resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);
                holder.btninactive.setVisibility(View.VISIBLE);
                holder.btninactive.setText(timeline.getStatusdes());
            }
        } else {
//            resetButton(holder.btninactive, holder.btndetail, holder.btnaccept, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btnchangedelivery);

//            holder.btninactive.setVisibility(View.VISIBLE);
//            holder.btninactive.setText(timeline.getStatusdes());
        }*/

        holder.btndetail.setVisibility(View.VISIBLE);
        if (fragment_No == timeLineAdapter.NEW_ORDER) {
            holder.btnreject.setVisibility(View.VISIBLE);
            holder.btnchangedelivery.setVisibility(View.VISIBLE);
            holder.btncancel.setVisibility(View.VISIBLE);
        }
        if (fragment_No == timeLineAdapter.ONLOAD_ORDER) {
            holder.checkin.setVisibility(View.VISIBLE);
            holder.showaddress.setVisibility(View.VISIBLE);
        }

        holder.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", timeline.getOrderNumber());
                Fragment myFragment = new fr_book_checkout();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = DetailFragment.newInstance(timeline, show_save, fragment_No);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }

        });

        holder.btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", timeline.getOrderNumber());
                Fragment myFragment = new fr_quotation_detail();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.checkin_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("Are you sure to reject this order?");

                Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRejectRequest(dialog, timeline, position);
                    }
                });

                dialog.show();
            }

        });

        holder.btnchangedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", timeline.getOrderNumber());
                Fragment myFragment = new fr_quotation_detail();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

                /*final Dialog dialog = new Dialog(context);*/
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.changedelivery_dialog);

                final EditText et_delivery_date = dialog.findViewById(R.id.et_delivery_date);
                final EditText et_address = dialog.findViewById(R.id.et_address);
                final EditText et_current_loc = dialog.findViewById(R.id.et_current_loc);
//                final Spinner sp_employees = dialog.findViewById(R.id.sp_employees);

                et_delivery_date.setText(timeline.getDeliverydate());
                et_address.setText(timeline.getAddress());
                et_current_loc.setText(timeline.getGeolocation());

                et_current_loc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (frag_ref != null) {
                            Intent intent = new Intent(context, placesActivity.class);
                            frag_ref.startActivityForResult(intent, 2);
                        } else {
                            myDB.showToast(context, "set fragment reference");
                        }
                    }
                });

//                String[] employeeNames = {"Khan"};
                //Creating the ArrayAdapter instance having the employeeNames list
                /*ArrayAdapter<detailitemVO> aa = new ArrayAdapter<detailitemVO>(context,
                        android.R.layout.simple_spinner_item, fr_homepage.emplist);
                aa.setDropDownViewResource(android.R.layout.simple_list_item_1);
                //Setting the ArrayAdapter data on the Spinner
                sp_employees.setAdapter(aa);*/

                et_delivery_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar newCalendar = Calendar.getInstance();
                        DatePickerDialog StartTime = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                Calendar newDate = Calendar.getInstance();
//                                newDate.set(year, monthOfYear, dayOfMonth);
//                                activitydate.setText(dateFormatter.format(newDate.getTime()));
                                monthOfYear += 1;
                                et_delivery_date.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)
                                        + "-" + (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear)
                                        + "-" + year + " " + timeline.getDeliverydate().split(" ")[1]);

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        StartTime.getDatePicker().setSpinnersShown(true);
                        StartTime.getDatePicker().setCalendarViewShown(false);
                        StartTime.show();


                    }
                });

                dialog.findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnupdate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String address = et_address.getText().toString();
                        String geolocation = et_current_loc.getText().toString();

                        timeline.updateData(et_delivery_date.getText().toString().trim()
                                , address, geolocation);

                        String delivery_date = et_delivery_date.getText().toString().trim().replace("-", ":");
                        delivery_date = delivery_date.replace(" ", ":");
                        sendChangeDeliveryRequest(dialog, timeline,
                                "Aic",/*((detailitemVO) sp_employees.getSelectedItem()).getDetailr(),*/
                                address, geolocation, delivery_date);


                    }
                });
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }

        });

        holder.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.checkin_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("Are you sure to cancel this order?");

                Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCancelRequest(dialog, timeline, position);
                    }
                });

                dialog.show();
            }

        });

        //open info window
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (timeline.getStatus().equals("1") || timeline.getStatus().equals("2") || timeline.getStatus().equals("4") || timeline.getStatus().equals("7")) {
                    FragmentActivity activity = (FragmentActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderid", timeline.getOrderNumber());
                    bundle.putString("orderstatus", timeline.getStatus());
                    Fragment myFragment = new fr_orderaddress();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }*/
                FragmentActivity activity = (FragmentActivity) v.getContext();
                Bundle bundle = new Bundle();
//                bundle.putString("orderid", timeline.getOrderNumber());
//                bundle.putString("orderstatus", timeline.getStatus());
                Fragment myFragment = DetailFragment.newInstance(timeline, show_save, fragment_No);
//                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

      /*  holder.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", timeline.getOrderNumber());
                Fragment myFragment = new fr_quotation_detail();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }

        });*/

        holder.checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.checkin_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("Are you sure to Checkin this order?");

                Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCheckinRequest(dialog, timeline, position);
                    }
                });

                dialog.show();
            }
        });

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

    private void sendChangeDeliveryRequest(final Dialog dialog, final timeLineVO timeline, String employeeaccount, String address,
                                           String geoLocation, String delivery_date) {
        IntroManager intromanager = new IntroManager(context);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("address", address);
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("deliverydate", delivery_date);
            jsonMain.put("employeeaccount", employeeaccount);
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("fbtoken", "0000");
            jsonMain.put("geolocation", geoLocation);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("orderid", timeline.getOrderNumber());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMChangeDeliveryDetails", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(context, new ProgressBar(context), "SSMChangeDeliveryDetails");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);

                    JSONObject jObj = new JSONObject(output);
                    if (jObj.getString("statuscode").equals("000")) {
                        Toast.makeText(context, "Delivery Changed!", Toast.LENGTH_SHORT).show();
                        showResponseDialog("Success!", "Order #" + timeline.getOrderNumber() + " successfully Changed");
                        notifyDataSetChanged();
                    } else
                        Toast.makeText(context, jObj.getString("statusdesc"), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void sendCancelRequest(final Dialog dialog, final timeLineVO timeline, final int position) {
        IntroManager intromanager = new IntroManager(context);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("customer", timeline.getCustomercode());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("fbtoken", "0000");
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("orderid", timeline.getOrderNumber());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMCancelServiceOrderPartner", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(context, new ProgressBar(context), "SSMCancelServiceOrderPartner");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);

                    JSONObject jObj = new JSONObject(output);
                    if (jObj.getString("statuscode").equals("000")) {
                        items.remove(timeline);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "order cancelled!", Toast.LENGTH_SHORT).show();
                        showResponseDialog("Success!", "Order #" + timeline.getOrderNumber() + " successfully Cancelled");
                    } else
                        Toast.makeText(context, "Error occoured!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void showResponseDialog(String title, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.checkin_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(title);
        text = (TextView) dialog.findViewById(R.id.text_dialog_message);
        text.setText(message);
        text.setVisibility(View.VISIBLE);

        Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
        dialogButton.setVisibility(View.GONE);

        dialog.findViewById(R.id.btnok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                sendRejectRequest(dialog, timeline, position);
            }
        });

        dialog.show();
    }


    private void sendRejectRequest(final Dialog dialog, final timeLineVO timeline, final int position) {
        IntroManager intromanager = new IntroManager(context);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("fbtoken", "0000");
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("orderid", timeline.getOrderNumber());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMDeclineServiceOrder", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(context, new ProgressBar(context), "SSMDeclineServiceOrder");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);


                    JSONObject jObj = new JSONObject(output);
                    if (jObj.getString("statuscode").equals("000")) {
                        items.remove(timeline);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "rejected!", Toast.LENGTH_SHORT).show();
                        showResponseDialog("Success!", "Order #" + timeline.getOrderNumber() + " successfully Rejected");
                    } else
                        Toast.makeText(context, "Error occoured!", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void sendCheckinRequest(final Dialog dialog, final timeLineVO timeline, final int position) {
        IntroManager intromanager = new IntroManager(context);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("employeeaccount", intromanager.getEmployeeID());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("orderid", timeline.getOrderNumber());
            jsonMain.put("outlet", intromanager.getOutletID());
//            jsonMain.put("FCMkey", intromanager.getFCMKey());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMLoadOrder", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(context, new ProgressBar(context), "SSMLoadOrder");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);

                    JSONObject jObj = new JSONObject(output);
                    if (jObj.getString("statuscode").equals("000")) {

                        items.remove(timeline);
                        notifyItemRemoved(position);

                        Toast.makeText(context, "Checked-in!", Toast.LENGTH_SHORT).show();
                        showResponseDialog("Success!", "Order #" + timeline.getOrderNumber() + " successfully Checked-in");
                    } else
                        Toast.makeText(context, "Error occoured!", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {

        Log.i("Debugging", output);
        Log.i("Debugging", handle);
        //Write local SQL
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.equals("000")) {
            myDB.alertBox(context, statusdesckey);
            return;
        } else if (handle == "SSMAcceptServiceOrder") {
            Bundle bundle = new Bundle();
            bundle.putString("orderid", myjson.getString("transactionref"));
            Fragment myFragment = new fr_cancel();
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

        }

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView ordernumber, orderdate, deliverydate, payment, number_of_items, supplier_code, delivery_veh_code,
                delivery_address, customer_contact, delivery_time, status, remarks, giftmessage,customername;
                /*itemname, priceamount, vendorname, orderstatus, remarks, materialcharges, materialgst*/;
        private Button btncancel, btndetail, btnaccept, btnclose, btncomplain, btnreject, btninactive, btnchangedelivery,
                checkin, showaddress;
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
            remarks = (TextView) v.findViewById(R.id.remarks);
            giftmessage = (TextView) v.findViewById(R.id.giftmessage);
            customername=v.findViewById(R.id.customername);
            /*itemname = (TextView) v.findViewById(R.id.itemname);
            priceamount = (TextView) v.findViewById(R.id.priceamount);
            vendorname = (TextView) v.findViewById(R.id.vendorname);
            orderstatus = (TextView) v.findViewById(R.id.orderstatus);
            materialcharges = (TextView) v.findViewById(R.id.materialcharges);
            materialgst = (TextView) v.findViewById(R.id.materialgst);
            remarks = (TextView) v.findViewById(R.id.remarks);*/
            btncancel = (Button) v.findViewById(R.id.btncancel);
            btndetail = (Button) v.findViewById(R.id.btnprofile);
            btnaccept = (Button) v.findViewById(R.id.btnotp);
            btnclose = (Button) v.findViewById(R.id.btnclose);
            btncomplain = (Button) v.findViewById(R.id.btncomplain);
            btnreject = (Button) v.findViewById(R.id.btnreject);
            btninactive = (Button) v.findViewById(R.id.btninactive);
            btnchangedelivery = (Button) v.findViewById(R.id.btnchangedelivery);
            checkin = (Button) v.findViewById(R.id.checkin);
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
    }

    public void locationSelected(String latlng) {
        Log.d("lovdebugging", "here " + latlng);
        if (dialog != null) {
            EditText et_current_loc = dialog.findViewById(R.id.et_current_loc);
            if (et_current_loc != null) {
                et_current_loc.setText(latlng);
            }
        }
    }
}
