package com.example.cris.globalnews;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class ServicioGPS extends Service implements LocationListener{

    private final Context ctx;
    double latitud;
    double longitud;
    Location location;
    boolean gpsActivo;
    LocationManager locationManager;

    public ServicioGPS(){
        super();
        this.ctx = this.getApplicationContext();

        getLocation();
    }

    public ServicioGPS(Context c){
        super();
        this.ctx = c;

        getLocation();
    }

    public double getLatitud(){
        return this.latitud;
    }

    public void getLocation(){
        try {
            locationManager = (LocationManager)this.ctx.getSystemService(LOCATION_SERVICE);
            gpsActivo = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e){

        }

        if(gpsActivo){
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000*60, 10, this);

            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }
    }

    public double getLongitud(){
        return this.longitud;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
