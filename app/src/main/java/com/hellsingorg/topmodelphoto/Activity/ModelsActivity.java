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
import android.widget.Toast;

import com.hellsingorg.topmodelphoto.Adapter.RecycleViewModelImage;

import com.hellsingorg.topmodelphoto.CustomClass.RecyclerItemClickListener;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;
import com.hellsingorg.topmodelphoto.MainActivity;
import com.hellsingorg.topmodelphoto.Model.ModelImageList;

import com.hellsingorg.topmodelphoto.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ModelsActivity extends AppCompatActivity {

    JSONObject jsonObject_ModelList = null;
    JSONArray jmodelList_JsonArray = null;
    private List<ModelImageList> modelsList = new ArrayList<>();

    private Boolean modelStoryExitsBoolean;

    private String model_id;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager lLayout;

    private TextView textviewStoryA;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);

        final Intent intent = getIntent();
        model_id = intent.getStringExtra("messageModel_id");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_ModelImage_list);
        mRecyclerView.setHasFixedSize(true);
        lLayout = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(lLayout);

        if(model_id != null){

            new GetModelListImage().execute();
            if(hud == null) {
                hud = KProgressHUD.create(ModelsActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Downloading data")
                        .setWindowColor(ContextCompat.getColor(ModelsActivity.this,R.color.pink3))
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f).show();
            }
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(modelsList != null){
                            Intent i = new Intent(ModelsActivity.this, ModelsAllListActivity.class);
                            i.putExtra("messageModel_id_All", model_id);
                            i.putExtra("messageModel_position", position);
                            startActivity(i);

                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        textviewStoryA = (TextView)findViewById(R.id.textviewStoryA);
        textviewStoryA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modelStoryExitsBoolean == true){
                    Intent intent1 = new Intent(ModelsActivity.this, ModelStoryActivity.class);
                    intent1.putExtra("messageModel_id", model_id);
                    startActivity(intent1);
                }else {
                    Toasty.error(ModelsActivity.this, "No Story in Database", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private class GetModelListImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            jsonObject_ModelList = userFunction.ReadModelbyId(model_id);

            modelsList = new ArrayList<>();

            if (jsonObject_ModelList != null) {
                try {
                    // Getting JSON Array node
                    jmodelList_JsonArray = jsonObject_ModelList.getJSONArray("GetModelImage");

                    for (int i = 0; i < jmodelList_JsonArray .length(); i++) {
                        JSONObject c = jmodelList_JsonArray.getJSONObject(i);

                        ModelImageList item = new ModelImageList();

                        item.setModel_id(c.getString("model_id"));
                        item.setImage_id(c.getString("image_id"));
                        item.setImage(c.getString("image"));
                        modelStoryExitsBoolean = c.getBoolean("storyExiBoolen");

                        modelsList.add(item);
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

        mAdapter = new RecycleViewModelImage(this, modelsList);

        mRecyclerView.setAdapter(mAdapter);

        if(hud.isShowing()){
            hud.dismiss();
        }
    }
}
