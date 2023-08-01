package com.aic.aicdelivery;

import org.json.JSONException;

/**
 * Created by Administrator on 11-06-2017.
 */
public interface AsyncResponse {
    void processFinish(String output,String handle) throws HMOwnException, JSONException;

   // boolean onCreateOptionsMenu(Menu menu);
}
