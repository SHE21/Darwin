package com.darwin.aigus.darwin.AndroidUltils;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by SANTIAGO on 29/01/2018.
 */

public class Gps implements LocationListener{
    private GeoPoint geoPoint;
    private MapController mapController;
    private MapView mapView;
    private Marker marker;
    private Drawable icon;

    public Gps(MapController mapController, MapView mapView, Drawable icon) {
        this.mapController = mapController;
        this.mapView = mapView;
        this.icon = icon;
    }

    @Override
    public void onLocationChanged(Location location) {
        geoPoint = new GeoPoint(location.getLongitude(), location.getLatitude());
        mapController.animateTo(geoPoint);
        putMarkerGps(geoPoint);
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

    public void putMarkerGps(GeoPoint geoPoint){
        marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setIcon(icon);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
    }
}
