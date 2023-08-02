package layout.newfiles;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.detailitemVO;
import com.aic.aicdelivery.timeLineAdapter;
import com.aic.aicdelivery.timeLineVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.fr_homepage;

public class DetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    String orderId, orderDate, deliverDate, payment;
    public boolean show_save, show_delivery_button = false;
    int from_frag = -1;
    String vehiclename;

    private OnFragmentInteractionListener mListener;

    ArrayList<Item> orderList = new ArrayList<>();

    RadioButton rb_full, rb_partial;
    int checked_radio_id = -1;
    Spinner sp_employees;
    TextView tv_employee;

    boolean spinner_init = false;

    IntroManager intromanager;

    CheckBox cb_package_details;
    View rl_package_details;
//    Button checkin;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(timeLineVO param1, boolean show_save, int from_frag) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        Log.d("debugging", "json detail value = " + param1);
        args.putString("orderId", param1.getOrderNumber());
        args.putString("orderDate", param1.getOrderDate());
        args.putString("deliverDate", param1.getDeliverydate());
        args.putString("payment", param1.getPaymenttype());
        args.putString(ARG_PARAM1, param1.getItemdetail());
        args.putBoolean("show_save", show_save);
        args.putInt("from_frag", from_frag);
        args.putString("vehiclename", param1.getVehiclename());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager = new IntroManager(requireContext());
        if (getArguments() != null) {
            orderId = getArguments().getString("orderId");
            orderDate = getArguments().getString("orderDate");
            deliverDate = getArguments().getString("deliverDate");
            payment = getArguments().getString("payment");

            mParam1 = getArguments().getString(ARG_PARAM1);
            show_save = getArguments().getBoolean("show_save", true);
            from_frag = getArguments().getInt("from_frag");
            vehiclename = getArguments().getString("vehiclename");
            try {
                JSONArray jarr = new JSONArray(mParam1);
                JSONObject jobj;
                for (int i = 0; i < jarr.length(); i++) {
                    jobj = jarr.getJSONObject(i);
                    orderList.add(new Item(jobj.getString("itemcode"), jobj.getString("itemname"), jobj.getString("itemunit"),
                            jobj.getInt("quantity"), jobj.getString("standardprice"), jobj.getString("totalamount"), jobj.getString("taxpercent")
                            , jobj.getString("offerprice"), jobj.getInt("deliverqty"), jobj.getString("itemname_a"),jobj.getString("giftmessagedetail")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Item details");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
        });

        ((TextView) v.findViewById(R.id.ordernumber)).setText("" + orderId);
        ((TextView) v.findViewById(R.id.orderdate)).setText("" + orderDate);
        ((TextView) v.findViewById(R.id.deliverydate)).setText("" + deliverDate);
        ((TextView) v.findViewById(R.id.payment)).setText("" + payment);

        cb_package_details = v.findViewById(R.id.cb_package_details);
        rl_package_details = v.findViewById(R.id.rl_package_details);
//        checkin = v.findViewById(R.id.checkin);

        cb_package_details.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    rl_package_details.setVisibility(View.VISIBLE);
                else
                    rl_package_details.setVisibility(View.GONE);

            }
        });

        rb_full = v.findViewById(R.id.rb_full);
        rb_partial = v.findViewById(R.id.rb_partial);

        if (from_frag == timeLineAdapter.NEW_ORDER || from_frag == timeLineAdapter.ONLOAD_ORDER) {
            sp_employees = v.findViewById(R.id.sp_employees);
            //Creating the ArrayAdapter instance having the employeeNames list
            ArrayAdapter<detailitemVO> aa = new ArrayAdapter<detailitemVO>(requireContext(),
                    android.R.layout.simple_spinner_item, fr_homepage.emplist);
            aa.setDropDownViewResource(android.R.layout.simple_list_item_1);
            //Setting the ArrayAdapter data on the Spinner
            sp_employees.setAdapter(aa);
            sp_employees.setVisibility(View.VISIBLE);

            int index = -1;
            for (int i = 0; i < fr_homepage.emplist.size(); i++) {
                if (fr_homepage.emplist.get(i).toString().equals(vehiclename)) {
                    index = i;
                    break;
                }
            }
            if (index != -1)
                sp_employees.setSelection(index);

            sp_employees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(requireContext(), "selected item", Toast.LENGTH_SHORT).show();
                    if (spinner_init)
                        updateVehicle(fr_homepage.emplist.get(position));
                    else spinner_init = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (from_frag == timeLineAdapter.NEW_ORDER) {
                /*final AppCompatSpinner sp_size = v.findViewById(R.id.sp_size);
                final AppCompatSpinner sp_weight = v.findViewById(R.id.sp_weight);
                final EditText et_pieces = v.findViewById(R.id.et_pieces);*/

                cb_package_details.setVisibility(View.VISIBLE);
                /*checkin.setVisibility(View.VISIBLE);
                checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String boxSize = "", boxWeight = "", pieces = "";

                        int selected = sp_size.getSelectedItemPosition();
                        switch (selected) {
                            case 0:
                                boxSize = "23:23:10";
                                break;
                            case 1:
                                boxSize = "28:28:10";
                                break;
                            case 2:
                                boxSize = "47:28:10";
                                break;
                            case 3:
                                boxSize = "30:35:10";
                                break;
                            case 4:
                                boxSize = "40:35:10";
                                break;
                        }
                        boxWeight = "" + (sp_weight.getSelectedItemPosition() + 1);
                        pieces = et_pieces.getText().toString();
                        if (pieces.isEmpty()) {
                            Toast.makeText(requireContext(), "Enter number of pieces!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (cb_package_details.isChecked())
                            sendCheckinRequest(boxSize, boxWeight, pieces);
                        else
                            sendCheckinRequest("", "", "0");
                    }
                });*/
            }

        } else {
            tv_employee = v.findViewById(R.id.tv_employee);
            tv_employee.setText(vehiclename);
            tv_employee.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = v.findViewById(R.id.rv_items);
        ItemListAdapter adapter = new ItemListAdapter(requireContext(), orderId, orderList, show_save, items -> {
            for (Item mItem : orderList) {
                if (mItem.getQuantity() != mItem.getDeliverqty()) {
                    rb_partial.setChecked(true);
                    break;
                } else {
                    rb_full.setChecked(true);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        if (show_save) {
            final AppCompatSpinner sp_size = v.findViewById(R.id.sp_size);
            final AppCompatSpinner sp_weight = v.findViewById(R.id.sp_weight);
            final EditText et_pieces = v.findViewById(R.id.et_pieces);

            v.findViewById(R.id.btnsave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String boxSize = "", boxWeight = "", pieces = "";
                    if (cb_package_details.isChecked()) {
                        int selected = sp_size.getSelectedItemPosition();
                        switch (selected) {
                            case 0:
                                boxSize = "23:23:10";
                                break;
                            case 1:
                                boxSize = "28:28:10";
                                break;
                            case 2:
                                boxSize = "47:28:10";
                                break;
                            case 3:
                                boxSize = "30:35:10";
                                break;
                            case 4:
                                boxSize = "40:35:10";
                                break;
                        }
                        boxWeight = "" + (sp_weight.getSelectedItemPosition() + 1);
                        pieces = et_pieces.getText().toString();

                        if (pieces.isEmpty()) {
                            Toast.makeText(requireContext(), "Enter number of pieces!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    JSONArray orderItems = new JSONArray();

                    for (Item mItem : orderList) {
                        JSONObject itm = new JSONObject();
                        try {
                            itm.put("itmactqty", mItem.getDeliverqty());
                            itm.put("itmmltamt", (mItem.getDeliverqty() * Float.parseFloat(mItem.getStandardprice())));
                            itm.put("itmmltcod", mItem.getItemcode());
                            itm.put("itmmltnam", mItem.getItemname());
                            itm.put("itmmltnet", 0.00);
                            itm.put("itmmltprc", mItem.getStandardprice());
                            itm.put("itmmltqty", mItem.getQuantity());
                            itm.put("itmmltunt", mItem.getItemunit());
                            itm.put("itmordcod", orderId);
                            itm.put("itmtaxpct", mItem.getTaxpercent());
                            orderItems.put(itm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        jsonMain.put("certificate", intromanager.getCertificate());
//                        jsonMain.put("employeeaccount", intromanager.getAdmin().equals("Y") ? "" : intromanager.getEmployeeID());
                        jsonMain.put("employeeaccount", intromanager.getEmployeeID());
                        /*jsonMain.put("employeeaccount", (sp_employees != null) ?
                                ((detailitemVO) sp_employees.getSelectedItem()).getDetailr() : "");*/
                        jsonMain.put("employeeid", intromanager.getEmployeeID());
                        jsonMain.put("fbtoken", intromanager.getFCMKey());
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("merchantcode", intromanager.getMerchantCode());
                        jsonMain.put("multiorderdata", orderItems);
                        jsonMain.put("orderid", orderId);
                        jsonMain.put("outlet", intromanager.getOutletID());
                        jsonMain.put("reference", (rb_full.isChecked()) ? "F" : "P");
                        jsonMain.put("boxsize", boxSize);
                        jsonMain.put("boxweight", boxWeight);
                        jsonMain.put("numberofpieces", pieces);
//                        jsonMain.put("FCMkey", intromanager.getFCMKey());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

                    String[] myTaskParams = {"/SSMPrepareServiceOrder", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        HMDataAccess aasyncTask = new HMDataAccess(requireContext(), new ProgressBar(requireContext()), "SSMPrepareServiceOrder");
                        aasyncTask.delegate = new AsyncResponse() {
                            @Override
                            public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                                output = output.substring(1, output.length() - 1);
                                output = output.replace("\\", "");

                                Log.i("Debugging", "Prepare Service Order :" + output);
                                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show();

                                requireActivity().onBackPressed();
                            }
                        };
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        } else {
            v.findViewById(R.id.btnsave).setVisibility(View.GONE);
//            rb_full.setEnabled(false);
//            rb_partial.setEnabled(false);
        }

        Button btndelivery = v.findViewById(R.id.btndelivery);
        if (show_delivery_button) {
            btndelivery.setVisibility(View.VISIBLE);
            btndelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliver();
                }
            });
        } else
            btndelivery.setVisibility(View.GONE);

        for (Item mItem : orderList) {
            if (mItem.getQuantity() != mItem.getDeliverqty()) {
                rb_partial.setChecked(true);
                if (show_delivery_button)
                    btndelivery.setText("Partial Delivered");
                checked_radio_id = 0;
                break;
            } else {
                rb_full.setChecked(true);
                if (show_delivery_button)
                    btndelivery.setText("Full Delivered");
                checked_radio_id = 1;
            }
        }

        rb_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_radio_id == 0)
                    rb_partial.setChecked(true);
                else if (checked_radio_id == 1)
                    rb_full.setChecked(true);
            }
        });
        rb_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_radio_id == 0)
                    rb_partial.setChecked(true);
                else if (checked_radio_id == 1)
                    rb_full.setChecked(true);
            }
        });

        return v;
    }

    private void updateVehicle(detailitemVO detailitemVO) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("employeeaccount", detailitemVO.getDetailr());
            jsonMain.put("orderid", orderId);
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMUpdateDeliveryEmployee", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), new ProgressBar(requireContext()), "SSMUpdateDeliveryEmployee");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);
                    //Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show();

                   // requireActivity().onBackPressed();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void deliver() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        IntroManager intromanager;
        intromanager = new IntroManager(getActivity());

        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("orderid", orderId);
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("employeeaccount", intromanager.getEmployeeID());
//            jsonMain.put("employeeaccount", intromanager.getAdmin().equals("Y") ? "" : intromanager.getEmployeeID());
            /*jsonMain.put("employeeaccount", (sp_employees != null) ?
                    ((detailitemVO) sp_employees.getSelectedItem()).getDetailr() : "");*/
            jsonMain.put("fbtoken", "0000");
            jsonMain.put("reference", (rb_full.isChecked()) ? "F" : "P");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
//            myDB.alertBox(getContext(), e.getMessage());
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());
//        String[] myTaskParams = {"/SSMMissedOrderList", jsonParam.toString()};
//        1 2 4
        String[] myTaskParams = {"/SSMCompleteDelivery", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), new ProgressBar(requireContext()), "SSMCompleteDelivery");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);
//                    Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(requireContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.checkin_dialog);

                    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                    text.setText("Order delivered successfully.");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
                    dialogButton.setVisibility(View.GONE);

                    dialog.findViewById(R.id.btnok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    requireActivity().onBackPressed();
                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
//            myDB.alertBox(getContext(), e.getMessage());
            return;
        }
    }

    private void sendCheckinRequest(String boxSize, String boxWeight, String pieces) {
        IntroManager intromanager = new IntroManager(requireContext());

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("fbtoken", "0000");
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("orderid", orderId);
            jsonMain.put("boxsize", boxSize);
            jsonMain.put("boxweight", boxWeight);
            jsonMain.put("numberofpieces", pieces);
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

        String[] myTaskParams = {"/SSMLoadOrder", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), new ProgressBar(requireContext()), "SSMLoadOrder");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    output = output.substring(1, output.length() - 1);
                    output = output.replace("\\", "");

                    Log.i("Debugging", "Booking Timeline Output :" + output);

                    JSONObject jObj = new JSONObject(output);
                    if (jObj.getString("statuscode").equals("000")) {


                        Toast.makeText(requireContext(), "Checked-in!", Toast.LENGTH_SHORT).show();
//                        showResponseDialog("Success!", "Order #" + timeline.getOrderNumber() + " successfully Checked-in");
                    } else
                        Toast.makeText(requireContext(), "Error occoured!", Toast.LENGTH_SHORT).show();

                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
