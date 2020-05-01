package com.darwin.aigus.darwin.Geometry;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 10/04/2018.
 */
public class DarwinGeometry{
    private MapView mapView;
    private OverlayManager overlayManager;
    private OnClickListenerMarker onClickListenerMarker;

    public interface OnClickListenerMarker{
        Marker.OnMarkerClickListener onClickMarker(Amostra amostra, int id);
    }

    private static final int STROKE_BLACK = Color.argb(255, 53, 53, 53);
    private static final int STROK_RED = Color.argb(255, 194, 42, 42);
    private static final int FILL_BLUE = Color.argb(97, 89, 114, 255);
    private static final int FILL_RED = Color.argb(97, 255, 41, 73);

    public DarwinGeometry(MapView mapView, OverlayManager overlayManager) {
        this.mapView = mapView;
        this.overlayManager = overlayManager;
    }

    public void showRadiusFromAmostra(Polyline polyline){
        polyline.setWidth(2);
        polyline.setColor(STROKE_BLACK);
        overlayManager.add(polyline);
    }

    public void addMarker(GeoPoint center, Marker marker){
        mapView.invalidate();
        marker.remove(mapView);
        marker.setPosition(center);
        overlayManager.add(marker);
    }

    public void showAreaLevant(Polygon polygon){
        polygon.setFillColor(FILL_RED);
        polygon.setStrokeWidth(2);
        polygon.setStrokeColor(STROK_RED);
        overlayManager.add(0, polygon);
    }

    public void showRaioFromCircle(Polygon polygon){
        polygon.setStrokeWidth(2);
        polygon.setFillColor(FILL_BLUE);
        polygon.setStrokeColor(STROKE_BLACK);
        overlayManager.add(polygon);
    }

    public void showRectTemp(Polygon polygon){
        polygon.setFillColor(FILL_RED);
        polygon.setStrokeWidth(2);
        polygon.setStrokeColor(STROKE_BLACK);
        overlayManager.add(polygon);
    }

    public static Polygon getPolygonRect(List<GeoPoint> geoPoints){
        Polygon polygon = new Polygon();
        polygon.setFillColor(FILL_RED);
        polygon.setStrokeWidth(2);
        polygon.setPoints(geoPoints);
        polygon.setStrokeColor(STROKE_BLACK);
        return polygon;
    }

    public List<Marker> getListMarkerIcon(List<Amostra> amostraList, List<Marker> markerList, Drawable markerIconDefault){
        for (int i = 0; i < amostraList.size(); i++){
            Amostra amostra = amostraList.get(i);
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            Marker pointAmostra = new Marker(mapView);
            pointAmostra.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            pointAmostra.setTitle(amostraList.get(i).getAm_name());
            pointAmostra.setIcon(markerIconDefault);
            pointAmostra.setPanToView(true);
            pointAmostra.setPosition(new GeoPoint(amostra.getAm_geodata_lat(), amostra.getAm_geodata_long()));
            markerList.add(pointAmostra);
        }
        return markerList;
    }

}
