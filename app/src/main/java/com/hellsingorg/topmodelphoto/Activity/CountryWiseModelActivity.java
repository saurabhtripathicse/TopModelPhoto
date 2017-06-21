package com.hellsingorg.topmodelphoto.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hellsingorg.topmodelphoto.Adapter.RecycleViewModelList;
import com.hellsingorg.topmodelphoto.CustomClass.RecyclerItemClickListener;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;
import com.hellsingorg.topmodelphoto.MainActivity;
import com.hellsingorg.topmodelphoto.Model.ModelsListModelClass;
import com.hellsingorg.topmodelphoto.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryWiseModelActivity extends AppCompatActivity {

    JSONObject jsonObject_ModelListDetails = null;
    JSONArray jmodelList_JsonArray = null;
    private List<ModelsListModelClass> modelsListModelClasses = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager lLayout;


    private String country_id;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_wise_model);

        final Intent intent = getIntent();
        country_id = intent.getStringExtra("messageCountry_id");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_model_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(country_id != null){

            new GetModelListDetails().execute();
            if(hud == null) {
                hud = KProgressHUD.create(CountryWiseModelActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Downloading data")
                        .setWindowColor(ContextCompat.getColor(CountryWiseModelActivity.this,R.color.pink3))
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f).show();
            }

        }



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(modelsListModelClasses != null){
                            Intent i = new Intent(CountryWiseModelActivity.this, ModelsActivity.class);
                            i.putExtra("messageModel_id",modelsListModelClasses.get(position).getId());
                            startActivity(i);

                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }


    private class GetModelListDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            jsonObject_ModelListDetails = userFunction.ReadModelbyCountryId(country_id);

            System.out.println("Return GetVehicleInsuDetails" + jsonObject_ModelListDetails);

            modelsListModelClasses = new ArrayList<>();

            if (jsonObject_ModelListDetails != null) {
                try {
                    // Getting JSON Array node
                    jmodelList_JsonArray = jsonObject_ModelListDetails.getJSONArray("GetModelByCountry");

                    for (int i = 0; i < jmodelList_JsonArray .length(); i++) {
                        JSONObject c = jmodelList_JsonArray.getJSONObject(i);

                        ModelsListModelClass item = new ModelsListModelClass();

                        item.setId(c.getString("id"));
                        item.setDob(c.getString("model_dob"));
                        item.setModelName(c.getString("model_name"));
                        item.setModelCountry(c.getString("country_name"));
                        item.setCountry_image(c.getString("country_image"));
                        item.setModelImageURL(c.getString("model_image"));

                        modelsListModelClasses.add(item);
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

        mAdapter = new RecycleViewModelList(this, modelsListModelClasses);

        mRecyclerView.setAdapter(mAdapter);

        if(hud.isShowing()){
            hud.dismiss();
        }
    }
}
