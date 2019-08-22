package com.alayyubi06.islamiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public  class MainActivity extends AppCompatActivity implements
        View.OnClickListener{
    private Button btnMoveShalat;
    private Button btnMoveHalal;
    private Button btnMoveKiblat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMoveShalat = (Button) findViewById(R.id.btn_move_shalat_app);
        btnMoveShalat.setOnClickListener(this);
        btnMoveHalal=(Button)findViewById(R.id.btn_move_halal_app);
        btnMoveHalal.setOnClickListener(this);
        btnMoveKiblat=(Button)findViewById(R.id.btn_move_kiblat_app);
        btnMoveKiblat.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_move_shalat_app:
                Intent moveShalat = new Intent(MainActivity.this,ShalatApp.class);
                startActivity(moveShalat);
                break;
            case R.id.btn_move_halal_app:
                Intent moveHalal = new Intent(MainActivity.this,HalalApp.class);
                startActivity(moveHalal);
                break;
            case R.id.btn_move_kiblat_app:
                Intent moveKiblat = new Intent(MainActivity.this,CompassActivity.class);
                startActivity(moveKiblat);
                break;
        }
    }
}

