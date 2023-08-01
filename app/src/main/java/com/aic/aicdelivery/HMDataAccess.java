package com.aic.aicdelivery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 06-07-2017.
 */

public class HMDataAccess extends AsyncTask<String, String, String> {
    public AsyncResponse delegate = null;
    private Context mContext;
    private ProgressBar mpb;
    private String mview;
    private ConnectivityManager cm;
    private FragmentActivity fragmentActivity;
    private HMCoreData myDB;
    private Boolean isConnected = true;
    public HMDataAccess(Context context, ProgressBar pb, String curview)  {
        this.mContext = context;
        this.mpb = pb;
        this.mview = curview;
        fragmentActivity=new FragmentActivity();
        myDB=new HMCoreData(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mpb.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(String... params)  {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if (!isNetworkAvailable()){
           isConnected=false;
            }
            //myDB.showToast(mContext,"Internet Connection not available");
        try {
            URL url = new URL(HMConstants.appurl + params[0]);
            Log.e("Debugging", HMConstants.appurl + params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //           JSONObject jsonParam = new JSONObject();


            wr.writeBytes(params[1]);

            wr.flush();
            wr.close();


            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();

        } catch (MalformedURLException e) {
            isConnected=false;
            e.printStackTrace();

        } catch (IOException e) {
            isConnected=false;
            e.printStackTrace();

        } finally {
            connection.disconnect();
            try {
                reader.close();
            } catch (IOException e) {
                isConnected=false;
                e.printStackTrace();
            } catch (NullPointerException e) {
                isConnected=false;
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mpb.setVisibility(View.GONE);
        if (!isConnected){
           //showErrorPage("Internet Connection is not available");
           myDB.showToast(mContext,"Internet Connection is not available or the platform is not reachable.  Please try after sometime.  Call +97143958183 for assistance");
            return;
        }
        try {
            delegate.processFinish(result, mview);
        } catch (Exception e) {
            //showErrorPage(e.getMessage());
            myDB.showToast(mContext,e.getMessage());
            e.printStackTrace();
        }

    }

    public  boolean isNetworkAvailable() {
        boolean status=false;
        try{
            cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()== NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            myDB.showToast(mContext,e.getMessage());
            e.printStackTrace();
        }
        return status;
    }
}
