package com.hellsingorg.topmodelphoto.Activity;

import android.animation.ArgbEvaluator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;

import android.view.View;

import android.widget.Toast;


import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import com.hellsingorg.topmodelphoto.Adapter.RecycleViewModelImageAll;
import com.hellsingorg.topmodelphoto.CustomClass.RecyclerItemClickListener;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;
import com.hellsingorg.topmodelphoto.MainActivity;
import com.hellsingorg.topmodelphoto.Model.ModelImageList;
import com.hellsingorg.topmodelphoto.R;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelsAllListActivity extends AppCompatActivity {

    JSONObject jsonObject_ModelList = null;
    JSONArray jmodelList_JsonArray = null;
    private List<ModelImageList> modelsList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String models_id_all;
    private int model_list_position;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models_all_list);

        final Intent intent = getIntent();
        models_id_all = intent.getStringExtra("messageModel_id_All");
        model_list_position = intent.getIntExtra("messageModel_position",1);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_model_all_list);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);



        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager.setSmoothScrollbarEnabled(true);

        if(models_id_all != null){
            new GetModelListImage().execute();
        }





        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, final int position) {

                        if(modelsList != null){

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModelsAllListActivity.this, R.style.MyDialogTheme);


                            // Setting Dialog Title
                            alertDialog.setTitle("Download or Share");

                            // Setting Dialog Message
                            alertDialog.setMessage("Do you want to save this file? \nDo you want to share it? ");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.balloons);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed YES button. Write Logic Here
                                    Toast.makeText(getApplicationContext(), "You clicked on YES",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("SHARE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed No button. Write Logic Here
                                    shareItem(modelsList.get(position).getImage());
                                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Setting Netural "Cancel" Button
                            alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed Cancel button. Write Logic Here
                                    Toast.makeText(getApplicationContext(), "You clicked on Cancel",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                        if(modelsList != null){




                        }
                    }
                })
        );


    }
    private class GetModelListImage extends AsyncTask<Void, Void, Void> {

        KProgressHUD hud;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(hud == null) {
                hud = KProgressHUD.create(ModelsAllListActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Downloading data")
                        .setWindowColor(ContextCompat.getColor(ModelsAllListActivity.this, R.color.pink3))
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f).show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            jsonObject_ModelList = userFunction.ReadModelbyId(models_id_all);

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
            if(hud.isShowing()){
                hud.dismiss();
            }
        }

    }

    void setupAdapter(){

        mLayoutManager.scrollToPositionWithOffset(model_list_position,modelsList.size());
        mAdapter = new RecycleViewModelImageAll(this, modelsList);
        mRecyclerView.setAdapter(mAdapter);


    }

    public void shareItem(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
