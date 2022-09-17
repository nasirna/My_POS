package com.kpbdstudio.mypos.network;

import android.content.Context;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    public Context mContext;
    public String Response = "";

    private MyServerListener listener;


    public VolleyRequest(Context context) {
        mContext = context;
        this.listener = null;

    }

    public void setListener(MyServerListener listener) {
        this.listener = listener;
    }

    public void VolleyPost(final String requestBody, String url) {


        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("OO9",""+response.toString());
                if (listener != null) {
                    listener.onResponse(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OO9",""+error.toString());
                if (listener != null) {
                    listener.onError(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };

        queue.add(JOPR);

    }


    public interface MyServerListener {
        public void onResponse(JSONObject response);

        public void onError(String error);
    }


}