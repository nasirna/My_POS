package com.kpbdstudio.mypos.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.kpbdstudio.mypos.LoginActivity;
import com.kpbdstudio.mypos.MainActivity;
import com.kpbdstudio.mypos.R;
import com.kpbdstudio.mypos.adapter.CategoryAdapter;
import com.kpbdstudio.mypos.entities.MenuCategoryObject;
import com.kpbdstudio.mypos.network.GsonRequest;
import com.kpbdstudio.mypos.network.VolleySingleton;
import com.kpbdstudio.mypos.util.CustomApplication;
import com.kpbdstudio.mypos.util.Helper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuCategoryFragment extends Fragment {

    private static final String TAG = MenuCategoryFragment.class.getSimpleName();
    RequestQueue requestQueue;
    private RecyclerView recyclerView;

    public MenuCategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.manu_categories));
        View view = inflater.inflate(R.layout.fragment_menu_category, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.food_menu);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);

        //Cache cache = new DiskBasedCache(getActivity().getCacheDir(),1024*1024);
        //Network network = new BasicNetwork(new HurlStack());
        //requestQueue = new RequestQueue(cache,network);
        //requestQueue.start();

        getRestaurantMenuFromRemoteServer();
        //getRestaurantMenu();

        return view;
    }

    private void getRestaurantMenuFromRemoteServer() {
        GsonRequest<MenuCategoryObject[]> serverRequest = new GsonRequest<MenuCategoryObject[]>(
                Request.Method.GET,
                Helper.PATH_TO_MENU,
                MenuCategoryObject[].class,
                null,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<MenuCategoryObject[]> createRequestSuccessListener() {
        return new Response.Listener<MenuCategoryObject[]>() {
            @Override
            public void onResponse(MenuCategoryObject[] response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    if (response.length > 0) {
                        //display restaurant menu information
                        List<MenuCategoryObject> menuObject = new ArrayList<>();
                        for (int i = 0; i < response.length; i++) {
                            Log.d(TAG, "Menu name " + response[i].getMenu_name());
                            menuObject.add(new MenuCategoryObject(response[i].getMenu_id(), response[i].getMenu_name(), response[i].getMenu_image()));
                        }
                        CategoryAdapter mAdapter = new CategoryAdapter(getActivity(), menuObject);
                        recyclerView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(getActivity(), "Restaurant menu is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    /*private void getRestaurantMenu(){

        //get working now
        //let's try post and send some data to server
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        String url=Helper.PATH_TO_MENU;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //get_response_text.setText("Data : "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //get_response_text.setText("Data 1 :"+jsonObject.getString("data_1")+"\n");
                    //get_response_text.append("Data 2 :"+jsonObject.getString("data_2")+"\n");
                    //get_response_text.append("Data 3 :"+jsonObject.getString("data_3")+"\n");
                    Toast.makeText(getActivity(), "Restaurant menu is empty", Toast.LENGTH_LONG).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                    //get_response_text.setText("Failed to Parse Json");
                    Toast.makeText(getActivity(), "Restaurant menu is empty", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //get_response_text.setText("Data : Response Failed");
            }
        }){ //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        queue.add(stringRequest);
    }


     */


}
