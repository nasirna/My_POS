package com.kpbdstudio.mypos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kpbdstudio.mypos.adapter.SingleCategoryAdapter;
import com.kpbdstudio.mypos.entities.MenuItemObject;
import com.kpbdstudio.mypos.network.GsonRequest;
import com.kpbdstudio.mypos.network.VolleyRequest;
import com.kpbdstudio.mypos.network.VolleySingleton;
import com.kpbdstudio.mypos.util.CustomApplication;
import com.kpbdstudio.mypos.util.Helper;
import com.kpbdstudio.mypos.util.SimpleDividerItemDecoration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleMenuCategoryActivity extends AppCompatActivity {

    private static final String TAG = SingleMenuCategoryActivity.class.getSimpleName();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_menu_category);

        String menuItem = getIntent().getExtras().getString("CATEGORY_NAME");
        setTitle(menuItem);

        int menuId = getIntent().getExtras().getInt("CATEGORY_ID");

        recyclerView = (RecyclerView)findViewById(R.id.single_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        getMenuItemsInCategoryFromRemoteServer(menuId);
    }

    //nsr

    private void getMenuItemsInCategoryFromRemoteServer(int id){
        //Map<String, String> params = new HashMap<String,String>();
        //params.put(Helper.MENU_ID, String.valueOf(id));

            String json_string = "{\n" +
                    "    \"menu_id\":\"2\"\n" +
                    "}";

            GsonRequest<MenuItemObject[]> serverRequest = new GsonRequest<MenuItemObject[]>(
                Request.Method.POST,
                Helper.PATH_TO_MENU_ITEM_BY_CATEGORY,
                MenuItemObject[].class,
                    json_string,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(SingleMenuCategoryActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<MenuItemObject[]> createRequestSuccessListener() {
        return new Response.Listener<MenuItemObject[]>() {
            @Override
            public void onResponse(MenuItemObject[] response) {
                try {
                    if(response.length > 0){
                        Log.d(TAG, "Json Response " + response.length);
                        //display menu items by category
                        List<MenuItemObject> itemObject = new ArrayList<MenuItemObject>();
                        for (int i = 0; i < response.length; i++){
                            itemObject.add(new MenuItemObject(response[i].getMenu_id(), response[i].getItem_name(),
                                    response[i].getDescription(), response[i].getItem_picture(), response[i].getItem_price(), response[i].getItem_options()));
                        }
                        SingleCategoryAdapter mAdapter = new SingleCategoryAdapter(SingleMenuCategoryActivity.this, itemObject, R.layout.layout);
                        recyclerView.setAdapter(mAdapter);
                    }else{
                        Toast.makeText(SingleMenuCategoryActivity.this, R.string.cannot_edit_user, Toast.LENGTH_LONG).show();
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
}
