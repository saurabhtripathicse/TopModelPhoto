package com.hellsingorg.topmodelphoto.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.hellsingorg.topmodelphoto.Model.ModelsListModelClass;
import com.hellsingorg.topmodelphoto.R;
import com.hellsingorg.topmodelphoto.interfaces.AdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Saurabh on 20-03-2017.
 */

public class RecycleViewModelList extends RecyclerView.Adapter<RecycleViewModelList.ViewHolder> {

    List<ModelsListModelClass> data = Collections.emptyList();

    private LayoutInflater inflater;

    private Activity activity;

    AdapterCallback adapterCallback;
    private int lastPosition = -1;

    public RecycleViewModelList(Activity activity, List<ModelsListModelClass> data){

        this.activity = activity;
        this.data = data;
    }

    public RecycleViewModelList(Activity activity, List<ModelsListModelClass> data, AdapterCallback adapterCallback){

        this.activity = activity;
        this.adapterCallback = adapterCallback;
        this.data = data;
    }

    @Override
    public RecycleViewModelList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.recycleview_models_photo_list, viewGroup, false);

        RecycleViewModelList.ViewHolder viewHolder = new RecycleViewModelList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewModelList.ViewHolder viewHolder, int i) {
        ModelsListModelClass current = data.get(i);

        String ImageLink = current.getModelImageURL();
        String CountryLink = current.getCountry_image();

        Picasso.with(activity)
                .load(ImageLink)
                .placeholder(R.color.pink2)   // optional
                .error(R.color.pink2)
                .centerCrop()
                .fit()
                .into(viewHolder.kenBurnsView_Model_image);

        Picasso.with(activity)
                .load(CountryLink)
                .placeholder(R.color.pink3)   // optional
                .error(R.color.pink3)
                .centerCrop()
                .fit()
                .into(viewHolder.imageViewCountry);

        String modelName = current.getModelName();
        viewHolder.textView_modelName.setText(modelName);

        String modelDOB = current.getDob();
        viewHolder.textView_model_dob.setText(modelDOB);

        String modelCountry = current.getModelCountry();
        viewHolder.textView_model_country.setText(modelCountry);

        setAnimation(viewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //  public ImageView imgThumbnail;
        public TextView textView_modelName;
        public TextView textView_model_dob;
        public  TextView textView_model_country;
        public KenBurnsView kenBurnsView_Model_image;
        public ImageView imageViewCountry;



        public ViewHolder(View itemView) {
            super(itemView);

            textView_modelName = (TextView)itemView.findViewById(R.id.textView_modelName);
            textView_model_dob = (TextView)itemView.findViewById(R.id.textView_model_dob);
            textView_model_country = (TextView) itemView.findViewById(R.id.textView_model_country);
            kenBurnsView_Model_image = (KenBurnsView) itemView.findViewById(R.id.modelImageView);
            imageViewCountry = (ImageView) itemView.findViewById(R.id.imageViewCountry);


        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
}
