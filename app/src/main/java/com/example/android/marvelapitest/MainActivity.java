package com.example.android.marvelapitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creando eventlistener en las imagenes para que abran otras actividades
        //donde se mostrará los titulos de algunos comics del heroe correspondiente

        //EventListener en imagen de Spider Man
        ImageView spidermanImageView = (ImageView)findViewById(R.id.spiderman);
        if(spidermanImageView != null) {
            spidermanImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent spidermanIntent = new Intent(MainActivity.this, SpiderManActivity.class);
                    startActivity(spidermanIntent);
                }
            });
        }

        //EventListener en imagen de Iron Man
        ImageView thorImageView = (ImageView)findViewById(R.id.thor);
        if(thorImageView != null) {
            thorImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent thorIntent = new Intent(MainActivity.this, ThorActivity.class);
                    startActivity(thorIntent);
                }
            });
        }

    }


}
