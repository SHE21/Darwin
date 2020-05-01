package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darwin.aigus.darwin.Async.AsyncGetAreaLevant;
import com.darwin.aigus.darwin.Async.AsyncGetPointAmostra;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogSeeMapAmostra extends DialogFragment {
    private DataBaseDarwin dataBaseDarwin;
    private Levantamento levantamento;
    private TextView panelNameAmostra, infoAmostraOnMap;
    private LinearLayout panelTools, panelInfo;
    private ProgressBar progressBar;
    private Polygon polygon;
    private OverlayManager overlayManager;
    private MapController mapController;
    private MapView mapView;
    private Drawable drawable, markerIconSelected;
    private List<Amostra> lista;
    private int selectedAmostra;

    public int getSelectedAmostra() {
        return selectedAmostra;
    }

    public void setSelectedAmostra(int selectedAmostra) {
        this.selectedAmostra = selectedAmostra;
    }

    public List<Amostra> getLista() {
        return lista;
    }

    public void setLista(List<Amostra> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dataBaseDarwin = new DataBaseDarwin(getContext());

        @SuppressLint("InflateParams")View view = inflater.inflate(R.layout.fragment_see_map_amostra, null);
        mapView = view.findViewById(R.id.map_one);
        overlayManager = mapView.getOverlayManager();
        mapController = (MapController) mapView.getController();
        panelNameAmostra = view.findViewById(R.id.panelNameAmostra);
        infoAmostraOnMap = view.findViewById(R.id.infoAmostraOnMap);
        progressBar = view.findViewById(R.id.progressBar);
        panelTools = view.findViewById(R.id.panelTools);
        panelInfo = view.findViewById(R.id.panelInfo);

        panelInfo.setOnClickListener(v -> panelInfo.setVisibility(View.GONE));

        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setAlignBottom(true);
        scaleBarOverlay.enableScaleBar();

        //ShowAreaLevantamento showAreaLevantamento = new ShowAreaLevantamento();
        //showAreaLevantamento.execute();

        showAreaLevant();
        showAmostras();

        drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker_darwin, null);
        markerIconSelected = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_selected_marker_darwin, null);

        polygon = new Polygon();
        polygon.setFillColor(Color.argb(75, 255,0,0));
        polygon.setStrokeWidth(4);
        polygon.setStrokeColor(Color.rgb(255, 79, 79));

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController.setZoom(15);
        overlayManager.add(scaleBarOverlay);

        builder.setPositiveButton(R.string.fechar, (dialog, which) -> {

        });

        builder.setView(view);
        return builder.create();
    }

    public void showAreaLevant(){
        AsyncGetAreaLevant getAreaLevant = new AsyncGetAreaLevant(dataBaseDarwin, helperAsyncArea());
        getAreaLevant.execute(getLevantamento().getLev_id_mask());
    }

    private AsyncGetAreaLevant.HelperAsync helperAsyncArea(){
        return new AsyncGetAreaLevant.HelperAsync() {
            @Override
            public void onHelperPreExecute() {

            }

            @Override
            public void onHelperPosExecute(List<KmlPolygon> KmlPolygonList) {
                progressBar.setVisibility(View.GONE);
                if (KmlPolygonList.size() != 0) {
                    GeoPoint goTo = new GeoPoint(KmlPolygonList.get(0).getLon(), KmlPolygonList.get(0).getLat());
                    List<GeoPoint> geoPointPoly = new ArrayList<>();

                    for (int i = 0; i < KmlPolygonList.size(); i++) {
                        geoPointPoly.add(new GeoPoint(KmlPolygonList.get(i).getLon(), KmlPolygonList.get(i).getLat()));
                    }

                    mapView.setVisibility(View.VISIBLE);
                    polygon.setPoints(geoPointPoly);
                    polygon.setTitle(getResources().getString(R.string.levantamento));
                    mapController.animateTo(goTo);
                    overlayManager.add(polygon);
                    panelTools.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void showAmostras(){
        AsyncGetPointAmostra generateAmostra = new AsyncGetPointAmostra(dataBaseDarwin, helperAsyncTaskAmostras());
        generateAmostra.execute(getLevantamento().getLev_id_mask());
    }

    private AsyncGetPointAmostra.HelperAsyncTask helperAsyncTaskAmostras(){
        return new AsyncGetPointAmostra.HelperAsyncTask() {
            @Override
            public void onPreExeculte() {

            }

            @Override
            public void onPostExecute(List<Amostra> amostrasList) {
                setLista(amostrasList);
                if (getLista().size() != 0){
                    List<Marker> markerIconList = getListMarkerIcon(mapView, getLista());
                    List<Marker> markerLabelList = getListMarkerLabel(mapView, getLista());

                    panelTools.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                    overlayManager.addAll(markerIconList);
                    overlayManager.addAll(markerLabelList);

                    Marker markerSelected = markerIconList.get(getSelectedAmostra());
                    markerSelected.setIcon(markerIconSelected);

                    panelNameAmostra.setText(getLista().get(getSelectedAmostra()).getAm_name());

                    zoomPointTo();
                }

            }
        };
    }

    public List<Marker> getListMarkerIcon(MapView mapView, List<Amostra> amostraList){
        List<Marker> markerList = new ArrayList<>();
        for (int i = 0; i < amostraList.size(); i++){
            final Amostra amostra = amostraList.get(i);
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            Marker point = new Marker(mapView);
            point.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            point.setTitle(amostraList.get(i).getAm_name());
            point.setIcon(drawable);
            point.setPosition(new GeoPoint(amostra.getAm_geodata_lat(), amostra.getAm_geodata_long()));
            point.setOnMarkerClickListener((marker, mapView1) -> {
                DataBaseDarwin db = new DataBaseDarwin(getContext());
                int totalAmostras = db.countEspecie(amostra.getId_mask());
                String info = marker.getTitle() + "\n" + getContext().getString(R.string.coords) +": "+ marker.getPosition() + "\n" + getContext().getString(R.string.totalEspecie) +": "+ totalAmostras;
                panelInfo.setVisibility(View.VISIBLE);
                infoAmostraOnMap.setText(info);
                return false;
            });
            markerList.add(point);
        }
        return markerList;
    }

    public List<Marker> getListMarkerLabel(MapView mapView, List<Amostra> amostraList){
        List<Marker> markerList = new ArrayList<>();
        for (int i = 0; i < amostraList.size(); i++){
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            Marker point = new Marker(mapView);
            point.setTextLabelBackgroundColor(Color.BLACK);
            point.setTextLabelFontSize(15);
            //Marker.ANCHOR_LEFT, Marker.ANCHOR_CENTER
            point.setAnchor(0.5f, 4.5f);
            point.setTextLabelForegroundColor(Color.WHITE);
            point.setTitle(amostraList.get(i).getAm_name());
            point.setIcon(null);
            point.setPosition(new GeoPoint(amostraList.get(i).getAm_geodata_lat(), amostraList.get(i).getAm_geodata_long()));
            markerList.add(point);
        }
        return markerList;
    }

    public Levantamento getLevantamento(){
        return this.levantamento;
    }
    public void setLevantamento(Levantamento levantamento){
        this.levantamento = levantamento;
    }

    public void zoomPointTo(){
        if (getLista().size() != 0){
            Amostra amostra = getLista().get(getSelectedAmostra());
            GeoPoint geoPoint = new GeoPoint(amostra.getAm_geodata_lat(), amostra.getAm_geodata_long());
            mapController.animateTo(geoPoint);
        }
    }
}
