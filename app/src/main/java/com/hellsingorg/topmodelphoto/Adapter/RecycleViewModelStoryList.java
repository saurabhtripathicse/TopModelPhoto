package com.hellsingorg.topmodelphoto.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import android.widget.TextView;

import com.hellsingorg.topmodelphoto.CustomClass.CircleImageView;

import com.hellsingorg.topmodelphoto.Model.ModelStoryListModelClass;
import com.hellsingorg.topmodelphoto.R;
import com.hellsingorg.topmodelphoto.interfaces.AdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Saurabh on 23-03-2017.
 */

public class RecycleViewModelStoryList extends RecyclerView.Adapter<RecycleViewModelStoryList.ViewHolder> {

    List<ModelStoryListModelClass> data = Collections.emptyList();

    private LayoutInflater inflater;

    private Activity activity;

    AdapterCallback adapterCallback;
    private int lastPosition = -1;

    public RecycleViewModelStoryList(Activity activity, List<ModelStoryListModelClass> data){

        this.activity = activity;
        this.data = data;
    }

    public RecycleViewModelStoryList(Activity activity, List<ModelStoryListModelClass> data, AdapterCallback adapterCallback){

        this.activity = activity;
        this.adapterCallback = adapterCallback;
        this.data = data;
    }

    @Override
    public RecycleViewModelStoryList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.recycleview_model_story_list, viewGroup, false);

        RecycleViewModelStoryList.ViewHolder viewHolder = new RecycleViewModelStoryList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewModelStoryList.ViewHolder viewHolder, int i) {
        ModelStoryListModelClass current = data.get(i);

        String ModelImageLink = current.getModel_image();

        Picasso.with(activity)
                .load(ModelImageLink)
                .placeholder(R.color.social_twitter_color)   // optional
                .error(R.color.social_twitter_color)
                .centerCrop()
                .fit()
                .into(viewHolder.imageViewModelStory);

        String ModelName = current.getModel_name();
        viewHolder.textView_ModelStoryName.setText(ModelName);

        String ModelStoryHeading = current.getStory_heading();
        viewHolder.textView_ModelStoryHeading.setText(ModelStoryHeading);

        setAnimation(viewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //  public ImageView imgThumbnail;
        public TextView textView_ModelStoryName;
        public TextView textView_ModelStoryHeading;
        public CircleImageView imageViewModelStory;



        public ViewHolder(View itemView) {
            super(itemView);

            textView_ModelStoryName = (TextView)itemView.findViewById(R.id.textView_ModelStoryName);
            textView_ModelStoryHeading = (TextView)itemView.findViewById(R.id.textView_ModelStoryHeading);
            imageViewModelStory = (CircleImageView) itemView.findViewById(R.id.imageViewModelStory);


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
