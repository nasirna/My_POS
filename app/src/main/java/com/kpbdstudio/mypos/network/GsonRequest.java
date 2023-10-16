package com.kpbdstudio.mypos.network;


import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.VolleyLog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/*public class GsonRequest<T> extends Request<T> {
    // create variables
    private Gson mGson = new Gson();
    private Class<T> tClass;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Response.Listener<T> listener;


    public GsonRequest(int method, String url, Class<T> tClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.tClass = tClass;
        this.listener = listener;
        mGson = new Gson();
    }

    public GsonRequest(int method, String url, Class<T> tClass, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.tClass = tClass;
        this.params = params;
        this.listener = listener;
        this.headers = null;
        mGson = new Gson();
    }

    //@Override
    //public Map<String, String> getHeaders() throws AuthFailureError {
    //    return headers != null ? headers : super.getHeaders();
    //}

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        return params;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        //params.put("Content-Type", "application/json; charset=utf-8");
        return params;
    }

    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, tClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}*/

/**
 * Custom implementation of Request<T> class which converts the HttpResponse obtained to Java class objects.
 * Uses GSON library, to parse the response obtained.
 * Ref - JsonRequest<T>
 * @author Mani Selvaraj
 */

public class GsonRequest<T> extends Request<T> {

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s",
            PROTOCOL_CHARSET);

    private final Listener<T> mListener;

    private final String mRequestBody;

    private Gson mGson;
    private Class<T> mJavaClass;

    private Map<String, String> headers;
    private Map<String, String> params;

    public GsonRequest(int method, String url, Class<T> cls, String requestBody, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mGson = new Gson();
        this.mJavaClass = cls;
        this.mListener = listener;
        this.mRequestBody = requestBody;
    }

    //nsr
    /*

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        return params;
    }

    */

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        return params;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        //params.put("Content-Type", "application/json; charset=utf-8");
        params.put("Content-Type", "application/json");
        return params;
    }

    //nsr
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    public void setHeaders(Map<String, String> headers) throws AuthFailureError {
        getHeaders().putAll(headers);
    }
    public void setParams(Map<String, String> params) throws AuthFailureError {
        getParams().putAll(params);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            //String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //Type type = TypeToken.get(mJavaClass).getType();
            //NetResponse<T> parsedParentGSON = mGson.fromJson(jsonString,
            //        GsonUtils.getType(NetResponse.class, type));
            //return Response.success(parsedParentGSON.result, HttpHeaderParser.parseCacheHeaders(response));
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mJavaClass), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody,
                    PROTOCOL_CHARSET);
            return null;
        }
    }
}