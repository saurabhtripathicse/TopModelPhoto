package com.hellsingorg.topmodelphoto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hellsingorg.topmodelphoto.Activity.AboutUsActivity;
import com.hellsingorg.topmodelphoto.Activity.AllStoryActivity;
import com.hellsingorg.topmodelphoto.Activity.CountryListActivity;
import com.hellsingorg.topmodelphoto.Activity.ModelsActivity;
import com.hellsingorg.topmodelphoto.Adapter.RecycleViewModelList;
import com.hellsingorg.topmodelphoto.CustomClass.RecyclerItemClickListener;
import com.hellsingorg.topmodelphoto.DbHandler.UserFunctions;
import com.hellsingorg.topmodelphoto.Model.ModelsListModelClass;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    JSONObject jsonObject_ModelListDetails = null;
    JSONArray jmodelList_JsonArray = null;
    private List<ModelsListModelClass> modelsListModelClasses = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout ) findViewById(R.id.swipeRefreshLayout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_model_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/bebas-neue-regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );





        new GetModelListDetails().execute();

        if(hud == null) {
            hud = KProgressHUD.create(MainActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Downloading data")
                    .setWindowColor(ContextCompat.getColor(MainActivity.this,R.color.pink3))
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f).show();
        }


        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                new GetModelListDetails().execute();

            }
        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(modelsListModelClasses != null){
                            Intent i = new Intent(MainActivity.this, ModelsActivity.class);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(this, CountryListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, AllStoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    // background Task
    private class GetModelListDetails extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();





        }

        @Override
        protected Void doInBackground(Void... arg0) {

            UserFunctions userFunction = new UserFunctions();
            jsonObject_ModelListDetails = userFunction.ReadModelDetails();

            System.out.println("Return GetVehicleInsuDetails" + jsonObject_ModelListDetails);

            modelsListModelClasses = new ArrayList<>();

            if (jsonObject_ModelListDetails != null) {
                try {
                    // Getting JSON Array node
                    jmodelList_JsonArray = jsonObject_ModelListDetails.getJSONArray("GetModelList");

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
        mAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setRefreshing(false);

        if(hud.isShowing()){
            hud.dismiss();
        }



    }
}
