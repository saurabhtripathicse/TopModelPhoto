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
import com.hellsingorg.topmodelphoto.Model.CountryListModelClass;
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

public class RecycleViewCoustryList extends RecyclerView.Adapter<RecycleViewCoustryList.ViewHolder> {

    List<CountryListModelClass> data = Collections.emptyList();

    private LayoutInflater inflater;

    private Activity activity;

    AdapterCallback adapterCallback;
    private int lastPosition = -1;

    public RecycleViewCoustryList(Activity activity, List<CountryListModelClass> data){

        this.activity = activity;
        this.data = data;
    }

    public RecycleViewCoustryList(Activity activity, List<CountryListModelClass> data, AdapterCallback adapterCallback){

        this.activity = activity;
        this.adapterCallback = adapterCallback;
        this.data = data;
    }

    @Override
    public RecycleViewCoustryList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.recycleview_country_list, viewGroup, false);

        RecycleViewCoustryList.ViewHolder viewHolder = new RecycleViewCoustryList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewCoustryList.ViewHolder viewHolder, int i) {
        CountryListModelClass current = data.get(i);

        String countryImageLink = current.getCountryImage();

        Picasso.with(activity)
                .load(countryImageLink)
                .placeholder(R.color.pink3)   // optional
                .error(R.color.pink3)
                .centerCrop()
                .fit()
                .into(viewHolder.imageViewCountry);

        String countryName = current.getCountryName();
        viewHolder.textView_countryName.setText(countryName);
        setAnimation(viewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //  public ImageView imgThumbnail;
        public TextView textView_countryName;
        public ImageView imageViewCountry;



        public ViewHolder(View itemView) {
            super(itemView);

            textView_countryName = (TextView)itemView.findViewById(R.id.textView_CountryName);
            imageViewCountry = (ImageView) itemView.findViewById(R.id.imageViewCountryImage);


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
