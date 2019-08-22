package com.alayyubi06.islamiapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.view.View.INVISIBLE;


public class CompassActivity extends AppCompatActivity {
    private static final String TAG = "CompassActivity";
    private Compass compass;
    // private ImageView arrowView;
    private ImageView arrowViewQiblat;
    private ImageView imageDial;
    private TextView text_atas;
    private TextView text_bawah;
    public Menu menu;
    public MenuItem item;
    private float currentAzimuth;
    SharedPreferences prefs;
    public LocationManager locationManager;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        //////////////////////////////////////////

        //////////////////////////////////////////
        prefs = getSharedPreferences("", MODE_PRIVATE);
        gps = new GPSTracker(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //////////////////////////////////////////
        arrowViewQiblat = (ImageView) findViewById(R.id.main_image_qiblat);
        imageDial = (ImageView) findViewById(R.id.main_image_dial);
        text_atas = (TextView) findViewById(R.id.teks_atas);
        text_bawah = (TextView) findViewById(R.id.teks_bawah);

        //////////////////////////////////////////
        arrowViewQiblat .setVisibility(INVISIBLE);
        arrowViewQiblat .setVisibility(View.GONE);

        setupCompass();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        if(compass != null) {
            compass.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(compass != null) {
            compass.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(compass != null) {
            compass.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        if(compass != null) {
            compass.stop();
        }



    }

    private void setupCompass() {
        Boolean permission_granted = GetBoolean("permission_granted");
        if(permission_granted) {
            getBearing();
        }else{
            text_atas.setText(getResources().getString(R.string.msg_permission_not_granted_yet));
            text_bawah.setText(getResources().getString(R.string.msg_permission_not_granted_yet));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }



        compass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                // adjustArrow(azimuth);
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        };
        compass.setListener(cl);
    }


    public void adjustGambarDial(float azimuth) {
        // Log.d(TAG, "will set rotation from " + currentAzimuth + " to "                + azimuth);

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        imageDial.startAnimation(an);
    }
    public void adjustArrowQiblat(float azimuth) {
        //Log.d(TAG, "will set rotation from " + currentAzimuth + " to "                + azimuth);

        float kiblat_derajat = GetFloat("kiblat_derajat");
        Animation an = new RotateAnimation(-(currentAzimuth)+kiblat_derajat, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowViewQiblat.startAnimation(an);
        if(kiblat_derajat > 0){
            arrowViewQiblat .setVisibility(View.VISIBLE);
        }else{
            arrowViewQiblat .setVisibility(INVISIBLE);
            arrowViewQiblat .setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    public void getBearing(){
        // Get the location manager

        float kiblat_derajat = GetFloat("kiblat_derajat");
        if(kiblat_derajat > 0.0001){
            text_bawah.setText(getResources().getString(R.string.your_location) +" "+ getResources().getString(R.string.using_last_location));
            text_atas.setText(getResources().getString(R.string.qibla_direction) +" " + kiblat_derajat + " " + getResources().getString(R.string.degree_from_north));
            // MenuItem item = menu.findItem(R.id.gps);
            if(item != null) {
                item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));
            }
            arrowViewQiblat .setVisibility(View.VISIBLE);
        }else
        {
            fetch_GPS();
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    SaveBoolean("permission_granted", true);
                    text_atas.setText(getResources().getString(R.string.msg_permission_granted));
                    text_bawah.setText(getResources().getString(R.string.msg_permission_granted));
                    arrowViewQiblat .setVisibility(INVISIBLE);
                    arrowViewQiblat .setVisibility(View.GONE);

                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_permission_required), Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public  void SaveString(String Judul, String tex){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(Judul, tex);
        edit.apply();
    }
    public String GetString(String Judul){
        String Stringxxx = prefs.getString(Judul, "");
        return Stringxxx;
    }

    public  void SaveBoolean(String Judul, Boolean bbb){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Judul, bbb);
        edit.apply();
    }
    public Boolean GetBoolean(String Judul){
        Boolean result = prefs.getBoolean(Judul, false);
        return result;
    }
    public  void Savelong(String Judul, Long bbb){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(Judul, bbb);
        edit.apply();
    }
    public Long Getlong(String Judul){
        Long xxxxxx = prefs.getLong(Judul, 0);
        return xxxxxx;
    }

    public void SaveFloat(String Judul, Float bbb){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(Judul, bbb);
        edit.apply();
    }
    public Float GetFloat(String Judul){
        Float xxxxxx = prefs.getFloat(Judul, 0);
        return xxxxxx;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // this.menu = menu;
        // menu.getItem(0). setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));
        // getMenuInflater().inflate(R.menu.gps, menu);
        // MenuItem item = menu.findItem(R.id.gps);
        inflater.inflate(R.menu.gps, menu);
        item = menu.findItem(R.id.gps);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.gps:
                //logout code
                fetch_GPS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetch_GPS(){



        double result = 0;
        gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            // \n is for new line
            text_bawah.setText(getResources().getString(R.string.your_location) + "\nLat: " + latitude + " Long: " + longitude);
            // Toast.makeText(getApplicationContext(), "Lokasi anda: - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Log.e("TAG", "GPS is on");
            double lat_saya = gps.getLatitude ();
            double lon_saya = gps.getLongitude ();
            if(lat_saya < 0.001 && lon_saya < 0.001) {
                // arrowViewQiblat.isShown(false);
                arrowViewQiblat .setVisibility(INVISIBLE);
                arrowViewQiblat .setVisibility(View.GONE);
                text_atas.setText(getResources().getString(R.string.location_not_ready));
                text_bawah.setText(getResources().getString(R.string.location_not_ready));
                if(item != null) {
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));
                }
                // Toast.makeText(getApplicationContext(), "Location not ready, Please Restart Application", Toast.LENGTH_LONG).show();
            }else{
                if(item != null) {
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_on));
                }
                double longitude2 = 39.826206; // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double longitude1 = lon_saya;
                double latitude2 = Math.toRadians(21.422487); // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double latitude1 = Math.toRadians(lat_saya);
                double longDiff= Math.toRadians(longitude2-longitude1);
                double y= Math.sin(longDiff)* Math.cos(latitude2);
                double x= Math.cos(latitude1)* Math.sin(latitude2)- Math.sin(latitude1)* Math.cos(latitude2)* Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x))+360)%360;
                float result2 = (float)result;
                SaveFloat("kiblat_derajat", result2);
                text_atas.setText(getResources().getString(R.string.qibla_direction) +" "+ result2 + " "+ getResources().getString(R.string.degree_from_north));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.qibla_direction) + " " + result2 + " "+ getResources().getString(R.string.degree_from_north), Toast.LENGTH_LONG).show();
                arrowViewQiblat .setVisibility(View.VISIBLE);

            }
            //  Toast.makeText(getApplicationContext(), "lat_saya: "+lat_saya + "\nlon_saya: "+lon_saya, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

            // arrowViewQiblat.isShown(false);
            arrowViewQiblat .setVisibility(INVISIBLE);
            arrowViewQiblat .setVisibility(View.GONE);
            text_atas.setText(getResources().getString(R.string.pls_enable_location));
            text_bawah.setText(getResources().getString(R.string.pls_enable_location));
            if(item != null){
                item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));

            }
            // Toast.makeText(getApplicationContext(), "Please enable Location first and Restart Application", Toast.LENGTH_LONG).show();
        }
    }
    ////////////////////////////////////
}
