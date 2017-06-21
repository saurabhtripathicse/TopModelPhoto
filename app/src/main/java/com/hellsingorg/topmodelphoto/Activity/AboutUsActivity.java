package com.hellsingorg.topmodelphoto.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hellsingorg.topmodelphoto.MainActivity;
import com.hellsingorg.topmodelphoto.R;

public class AboutUsActivity extends AppCompatActivity {

    ImageView view_backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        view_backbutton = (ImageView) findViewById(R.id.imageview_backbutton);
        view_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
