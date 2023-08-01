package layout.newfiles;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aic.aicdelivery.R;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.CustomViewHolder> {
    private Context context;
    String orderId;
    public ArrayList<Item> items;
    private OnQuantityListener mListener;
    boolean show_plus_minus;

    public ItemListAdapter(Context activity, String orderId, ArrayList<Item> items, boolean show_plus_minus, OnQuantityListener mListener) {
        this.context = activity;
        this.orderId = orderId;
        this.items = items;
        this.show_plus_minus = show_plus_minus;
        this.mListener = mListener;
    }

    @Override
    public ItemListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemListAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemListAdapter.CustomViewHolder holder, int position) {

        final Item item = items.get(position);
        holder.item_code.setText(item.getItemcode());
        holder.description.setText(item.getItemname());
        holder.unit.setText(item.getItemunit());
        holder.actual_qty.setText(String.format("%d", item.getQuantity()));
        holder.delivered_qty.setText(String.format("%d", item.getDeliverqty()));
        holder.giftmessage.setText(item.getGiftMessageDetail());
        if (show_plus_minus) {
            holder.iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.delivered_qty.getText().toString());
                    if (count != item.getQuantity()) {
                        count++;
                        item.setDeliverqty(count);
                        mListener.onItemQuantityChanged(items);
                    }
                    holder.delivered_qty.setText("" + count);

                }
            });

            holder.iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.delivered_qty.getText().toString());
                    if (count != 0) {
                        count--;
                        item.setDeliverqty(count);
                        mListener.onItemQuantityChanged(items);
                    }
                    holder.delivered_qty.setText("" + count);
                }
            });
        } else {
            holder.iv_plus.setVisibility(View.INVISIBLE);
            holder.iv_minus.setVisibility(View.INVISIBLE);
        }

        /*holder.btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroManager intromanager = new IntroManager(context);

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                JSONArray orderItems = new JSONArray();

                for (Item mItem : items) {
                    JSONObject itm = new JSONObject();
                    try {
                        itm.put("itmactqty", mItem.getQuantity());
                        itm.put("itmmltamt", (mItem.getDeliverqty() * Float.parseFloat(mItem.getStandardprice())));
                        itm.put("itmmltcod", mItem.getItemcode());
                        itm.put("itmmltnam", mItem.getItemname());
                        itm.put("itmmltnet", 0.00);
                        itm.put("itmmltprc", mItem.getStandardprice());
                        itm.put("itmmltqty", mItem.getDeliverqty());
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
//                    jsonMain.put("employeeaccount", intromanager.getAdmin().equals("Y") ? "" : intromanager.getEmployeeID());
                    jsonMain.put("employeeaccount", intromanager.getEmployeeID());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("fbtoken", "0000");
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("multiorderdata", orderItems);
                    jsonMain.put("orderid", orderId);
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("reference", "F");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("Debugging", "New Orders Input :" + jsonParam.toString());

                String[] myTaskParams = {"/SSMPrepareServiceOrder", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(context, new ProgressBar(context), "SSMPrepareServiceOrder");
                    aasyncTask.delegate = new AsyncResponse() {
                        @Override
                        public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                            output = output.substring(1, output.length() - 1);
                            output = output.replace("\\", "");

                            Log.i("Debugging", "Booking Timeline Output :" + output);
                            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });*/


//        Log.i("Debugging", "Current Status :" + timeline.getStatus());
//        Log.i("Debugging", "Max Status :" + timeline.getMaxStatus());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView item_code, description, unit, actual_qty, delivered_qty,giftmessage;
        View iv_plus, iv_minus;
//        private Button btnsave, btnclose;

        public CustomViewHolder(View v) {
            super(v);

            item_code = (TextView) v.findViewById(R.id.item_code);
            description = (TextView) v.findViewById(R.id.description);
            unit = (TextView) v.findViewById(R.id.unit);
            actual_qty = (TextView) v.findViewById(R.id.actual_qty);
            delivered_qty = (TextView) v.findViewById(R.id.delivered_qty);
            iv_plus = v.findViewById(R.id.iv_plus);
            iv_minus = v.findViewById(R.id.iv_minus);
            giftmessage = v.findViewById(R.id.giftmessage);
//            btnsave = v.findViewById(R.id.btnsave);
//            btnclose = v.findViewById(R.id.btnclose);
        }
    }

    public interface OnQuantityListener {
        // TODO: Update argument type and name
        void onItemQuantityChanged(ArrayList<Item> items);
    }
}
