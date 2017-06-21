package com.hellsingorg.topmodelphoto.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;
import com.hellsingorg.topmodelphoto.Model.ModelImageList;
import com.hellsingorg.topmodelphoto.R;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ModelStoryActivity extends AppCompatActivity {

    private String model_id;

    private TextView textviewNameModel;
    private TextView textviewHeading;
    private TextView textviewDescription;
    private KenBurnsView KenBurnsViewgradientImage;
    private FloatingActionButton fabshare;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_story);

        textviewNameModel = (TextView) findViewById(R.id.textviewNameModel);
        textviewHeading = (TextView) findViewById(R.id.textviewHeading);
        textviewDescription = (TextView) findViewById(R.id.textviewDescription);
        KenBurnsViewgradientImage = (KenBurnsView) findViewById(R.id.KenBurnsViewgradientImage);
        fabshare = (FloatingActionButton) findViewById(R.id.fabshare);

        final Intent intent = getIntent();
        model_id = intent.getStringExtra("messageModel_id");

        if(model_id !=null){
            new GetModelStory().execute();
            if(hud == null) {
                hud = KProgressHUD.create(ModelStoryActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Downloading data")
                        .setWindowColor(ContextCompat.getColor(ModelStoryActivity.this,R.color.pink3))
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f).show();
            }
        }

        fabshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String modelName = textviewNameModel.getText().toString();
                String textToShare = "Story of "+ modelName.toUpperCase()
                        + "\n\n" + textviewHeading.getText()
                        + "\n" + textviewDescription.getText();
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, textToShare);

                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

    }

    private class GetModelStory extends AsyncTask<Void, Void, Void> {

        private String imageLink;
        private String nameModel;
        private String heading;
        private String description;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject jsonObject_ModelStory = userFunction.ReadModelStorybyId(model_id);



            if (jsonObject_ModelStory != null) {
                try {
                    // Getting JSON Array node
                    JSONArray jmodelStory_JsonArray = jsonObject_ModelStory.getJSONArray("GetModelStory");

                    for (int i = 0; i < jmodelStory_JsonArray .length(); i++) {
                        final JSONObject c = jmodelStory_JsonArray.getJSONObject(i);

                        imageLink = c.getString("model_image");
                        nameModel = c.getString("model_name");
                        heading = c.getString("story_heading");
                        description = c.getString("story_description");



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

            Picasso.with(ModelStoryActivity.this)
                    .load(imageLink)
                    .placeholder(R.color.pink3)   // optional
                    .error(R.color.pink3)
                    .fit()
                    .into(KenBurnsViewgradientImage);

            textviewDescription.setText(description);
            textviewHeading.setText(heading);
            textviewNameModel.setText(nameModel);

            if(hud.isShowing()){
                hud.dismiss();
            }

        }

    }
}
