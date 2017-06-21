package com.hellsingorg.topmodelphoto.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hellsingorg.topmodelphoto.Adapter.RecycleViewCoustryList;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;

import com.hellsingorg.topmodelphoto.MainActivity;
import com.hellsingorg.topmodelphoto.Model.CountryListModelClass;
import com.hellsingorg.topmodelphoto.CustomClass.RecyclerItemClickListener;
import com.hellsingorg.topmodelphoto.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryListActivity extends AppCompatActivity {

    KProgressHUD hud;

    JSONObject jsonObject_countryList = null;
    JSONArray jcountryList_JsonArray = null;
    private List<CountryListModelClass> countryListModelClass = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_country_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new GetCountryList().execute();

        if(hud == null) {
            hud = KProgressHUD.create(CountryListActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Downloading data")
                    .setWindowColor(ContextCompat.getColor(CountryListActivity.this,R.color.pink3))
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f).show();
        }


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(countryListModelClass != null){
                            Intent i = new Intent(CountryListActivity.this, CountryWiseModelActivity.class);
                            i.putExtra("messageCountry_id",countryListModelClass.get(position).getId());
                            startActivity(i);

                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private class GetCountryList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            jsonObject_countryList = userFunction.ReadCountryList();

            countryListModelClass = new ArrayList<>();

            if (jsonObject_countryList != null) {
                try {
                    // Getting JSON Array node
                    jcountryList_JsonArray = jsonObject_countryList.getJSONArray("GetCountryList");

                    for (int i = 0; i < jcountryList_JsonArray .length(); i++) {
                        JSONObject c = jcountryList_JsonArray.getJSONObject(i);

                        CountryListModelClass item = new CountryListModelClass();

                        item.setId(c.getString("country_id"));
                        item.setCountryImage(c.getString("country_image"));
                        item.setCountryName(c.getString("country_name"));

                        countryListModelClass.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            setupAdapter();
        }

    }


    void setupAdapter(){

        mAdapter = new RecycleViewCoustryList(this, countryListModelClass);

        mRecyclerView.setAdapter(mAdapter);
        if(hud.isShowing()){
            hud.dismiss();
        }
    }
}
