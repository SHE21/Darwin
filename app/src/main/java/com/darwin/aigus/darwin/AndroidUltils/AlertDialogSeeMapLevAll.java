package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Async.AsyncGetPointAmostra;
import com.darwin.aigus.darwin.Async.AsyncGetAreaLevant;
import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.PolygonArea;
import net.sf.geographiclib.PolygonResult;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogSeeMapLevAll extends DialogFragment {
    private Levantamento levantamento;
    private TextView infoAmostraOnMap, resultAreaOfCircle, resultAreaLevant;
    private GeoPoint geoPointToAnalyse;
    private EditText meterCircle, fild1Polygon, fild2Polygon;
    private LinearLayout panelTools, panelInfo, contAnaliseFeicao;
    private ProgressBar progressBar;
    private Polygon polyAreaLevant, circle;
    private ImageButton saveMarker, btnMoreMarker, btnMorePanelFeicao, saveCircle, removeCircle, addCircle, addPolygon;
    private OverlayManager overlayManager;
    private MapController mapController;
    private MapView mapView;
    private Drawable markerIconDefault, amostra_green, save_green, amostra_gray, save_gray, drawAddCircleGreen, drawAddCircleGray;
    private Marker newMarker, pointAmostra;
    private List<Amostra> lista;
    private ReloadAmostras reloadAmostras;
    private MapOverLay mapOverLay;
    private DataBaseDarwin dataBaseDarwin;
    private String infOfFeicao = null;

    private static final int SAVE_CIRCLE = 0;
    private static final int DELETE_CIRCLE = 1;

    private List<GeoPoint> listOfPointsRectTemp;
    private List<GeoPoint> pointsOfAreaLevant = new ArrayList<>();
    private Polygon rectTemp;
    private Polyline polyline;

    private int colorBlackStroke;
    private int colorRedStroke;
    private int colorBlueFill;
    private int colorRedFill;

    public List<Amostra> getLista() {
        return lista;
    }

    public void setLista(List<Amostra> lista) {
        this.lista = lista;
    }

    public void setReloadAmostras(ReloadAmostras reloadAmostras) {
        this.reloadAmostras = reloadAmostras;
    }

    public interface ReloadAmostras {
        void reloadAmostras();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dataBaseDarwin = new DataBaseDarwin(getContext());

        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.fragment_see_map, null);

        mapView = view.findViewById(R.id.map_one);
        overlayManager = mapView.getOverlayManager();
        mapController = (MapController) mapView.getController();
        TextView panelNameAmostra = view.findViewById(R.id.panelNameAmostra);
        infoAmostraOnMap = view.findViewById(R.id.infoAmostraOnMap);
        progressBar = view.findViewById(R.id.progressBar);
        btnMoreMarker = view.findViewById(R.id.moreMarker);
        btnMorePanelFeicao = view.findViewById(R.id.btnMorePanelFeicao);
        saveMarker =  view.findViewById(R.id.saveMarker);
        panelTools = view.findViewById(R.id.panelTools);
        panelInfo = view.findViewById(R.id.panelInfo);
        saveCircle = view.findViewById(R.id.saveCircle);
        removeCircle = view.findViewById(R.id.removeCircle);
        addPolygon = view.findViewById(R.id.addPolygon);
        fild1Polygon = view.findViewById(R.id.fild1Polygon);
        fild2Polygon = view.findViewById(R.id.fild2Polygon);
        meterCircle = view.findViewById(R.id.meterCircle);
        addCircle = view.findViewById(R.id.addCircle);
        contAnaliseFeicao = view.findViewById(R.id.contAnaliseFeicao);
        resultAreaOfCircle = view.findViewById(R.id.resultAreaOfCircle);
        resultAreaLevant = view.findViewById(R.id.resultAreaLevant);

        mapOverLay = new MapOverLay();//MUITO IMPORTANTE

        saveCircle.setEnabled(false);
        removeCircle.setEnabled(false);
        addPolygon.setEnabled(false);
        addCircle.setEnabled(false);

        colorBlackStroke = ResourcesCompat.getColor(getResources(), R.color.strokeBlack, null);
        colorRedStroke = ResourcesCompat.getColor(getResources(), R.color.fillRect, null);
        colorBlueFill = ResourcesCompat.getColor(getResources(), R.color.fillCircle, null);
        colorRedFill = ResourcesCompat.getColor(getResources(), R.color.fillArea, null);

        Drawable drawAddPolyGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_poly, null);
        Drawable drawAddPolyGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_gray_poly, null);
        Drawable btnMorePanel = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null);
        Drawable btnLessPanel = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null);
        drawAddCircleGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_circle, null);
        drawAddCircleGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_gray_circle, null);
        amostra_green = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_amostra_green, null);
        amostra_gray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_amostra, null);
        save_green = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save_black_green_24dp, null);
        save_gray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save_black_24dp, null);
        markerIconDefault = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker_darwin, null);

        addPolygon.setOnClickListener(onClickListenerAddRectTemp());
        OnEditTextChange onEditTextChange = new OnEditTextChange(addPolygon, drawAddPolyGreen, drawAddPolyGray);
        fild1Polygon.addTextChangedListener(onEditTextChange);
        fild2Polygon.addTextChangedListener(onEditTextChange);
        meterCircle.addTextChangedListener(new OnEditTextChange(addCircle, drawAddCircleGreen, drawAddCircleGray));

        panelNameAmostra.setText(getLevantamento().getLev_name());
        panelInfo.setOnClickListener(v -> panelInfo.setVisibility(View.GONE));

        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setAlignBottom(true);
        scaleBarOverlay.enableScaleBar();

        btnMoreMarker.setImageDrawable(amostra_gray);

        //CAMADAS DE FEIÇÕES
        showArea();
        showAreaLevant();
        showAmostras();
        addCircle.setOnClickListener(onClickListenerAddCircle());
        showRaioFromCircle();
        showCircleFromAmostra();
        showRectTemp();

        btnMoreMarker.setOnClickListener(v -> {

            if (btnMoreMarker.getDrawable() == amostra_gray){
                btnMoreMarker.setImageDrawable(amostra_green);

                Drawable drawable = ResourcesCompat.getDrawable (getResources(), R.drawable.ic_new_marker_darwin, null);
                newMarker = new Marker(mapView);
                newMarker.setIcon(drawable);
                newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                newMarker.setDraggable(true);
                btnMoreMarker.setImageDrawable(amostra_green);
                overlayManager.add(mapOverLay);
                Toast.makeText(getContext(), R.string.modeAddAmostraAtive, Toast.LENGTH_SHORT).show();

            }else{
                btnMoreMarker.setImageDrawable(amostra_gray);
                saveMarker.setImageDrawable(save_gray);
                if(newMarker != null){
                    newMarker.remove(mapView);
                    newMarker = null;
                }
                overlayManager.remove(mapOverLay);
                mapView.invalidate();
                Toast.makeText(getContext(), R.string.modeAddAmostraDesative, Toast.LENGTH_SHORT).show();
            }

        });

        saveMarker.setOnClickListener(v -> {
            if (newMarker != null) {
                GeoPoint coords = newMarker.getPosition();
                String nameAmostra = AlertDialogCreateAmostra.suggestNameAmostra(getLista());
                Amostra amostra = new Amostra();
                amostra.setId_mask(GenerateId.generateId());
                amostra.setId_mask_lev(getLevantamento().getLev_id_mask());
                amostra.setAm_date(DateDarwin.getDate());
                amostra.setAm_name(nameAmostra);
                amostra.setAm_geodata_long(coords.getLongitude());
                amostra.setAm_geodata_lat(coords.getLatitude());

                long t = dataBaseDarwin.insertAmostra(amostra);
                if (t != 0){
                    Toast.makeText(getContext(), amostra.getAm_name() + " criada!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Erro ao criar a Amostra!", Toast.LENGTH_SHORT).show();
                }

                newMarker.remove(mapView);
                showAmostras();
                mapView.invalidate();
                reloadAmostras.reloadAmostras();
                saveMarker.setImageDrawable(save_gray);
                btnMoreMarker.setImageDrawable(amostra_gray);
                newMarker = null;
            }
        });

        btnMorePanelFeicao.setImageDrawable(btnMorePanel);
        btnMorePanelFeicao.setOnClickListener(v -> {
            if (infOfFeicao != null) {

                if (btnMorePanelFeicao.getDrawable() == btnMorePanel) {
                    btnMorePanelFeicao.setImageDrawable(btnLessPanel);
                    contAnaliseFeicao.setVisibility(View.VISIBLE);

                } else {
                    btnMorePanelFeicao.setImageDrawable(btnMorePanel);
                    contAnaliseFeicao.setVisibility(View.GONE);
                }
            }
        });

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController.setZoom(17);
        overlayManager.add(scaleBarOverlay);

        builder.setPositiveButton(R.string.fechar, (dialog, which) -> {

        });

        builder.setView(view);
        return builder.create();
    }

    public void showArea(){
        AsyncGetAreaLevant getAreaLevant = new AsyncGetAreaLevant(dataBaseDarwin, helperAsyncArea());
        getAreaLevant.execute(getLevantamento().getLev_id_mask());
    }

    private AsyncGetAreaLevant.HelperAsync helperAsyncArea(){
        return new AsyncGetAreaLevant.HelperAsync() {
            @Override
            public void onHelperPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHelperPosExecute(List<KmlPolygon> KmlPolygonList) {
                progressBar.setVisibility(View.GONE);
                if (KmlPolygonList.size() != 0) {
                    GeoPoint goTo = new GeoPoint(KmlPolygonList.get(0).getLon(), KmlPolygonList.get(0).getLat());

                    for (int i = 0; i < KmlPolygonList.size(); i++) {
                        pointsOfAreaLevant.add(new GeoPoint(KmlPolygonList.get(i).getLon(), KmlPolygonList.get(i).getLat()));
                    }

                    PolygonResult area = DarwinGeometryAnalyst.getAreaPerimeterOfPolygon(pointsOfAreaLevant);
                    String a = "m²->"+ area.area + " ha->" +area.area/10000;
                    String perimeter = "p->"+ area.perimeter;
                    String medidas = a +" \n"+ perimeter;
                    resultAreaLevant.setText(medidas);

                    mapView.setVisibility(View.VISIBLE);
                    polyAreaLevant.setPoints(pointsOfAreaLevant);
                    polyAreaLevant.setTitle(getResources().getString(R.string.levantamento));
                    mapController.animateTo(goTo);
                    panelTools.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public List<Marker> getListMarkerIcon(MapView mapView, List<Amostra> amostraList){
        List<Marker> markerList = new ArrayList<>();
        for (int i = 0; i < amostraList.size(); i++){
            final Amostra amostra = amostraList.get(i);
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            pointAmostra = new Marker(mapView);
            pointAmostra.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            pointAmostra.setTitle(amostraList.get(i).getAm_name());
            pointAmostra.setIcon(markerIconDefault);
            pointAmostra.setPosition(new GeoPoint(amostra.getAm_geodata_lat(), amostra.getAm_geodata_long()));
            pointAmostra.setOnMarkerClickListener(onCLikMarkerAmostra(amostra));
            markerList.add(pointAmostra);
        }
        return markerList;
    }

    private Marker.OnMarkerClickListener onCLikMarkerAmostra(Amostra amostra){
        return (marker, mapView) -> {
            DataBaseDarwin db = new DataBaseDarwin(getContext());
            int totalAmostras = db.countEspecie(amostra.getId_mask());
            infOfFeicao = marker.getTitle() + "\n" + getContext().getString(R.string.coords) +": "+ marker.getPosition() + "\n" + getContext().getString(R.string.totalEspecie) +": "+ totalAmostras;
            geoPointToAnalyse = new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            panelInfo.setVisibility(View.VISIBLE);
            infoAmostraOnMap.setText(infOfFeicao);
            btnMorePanelFeicao.setVisibility(View.VISIBLE);
            return true;
        };
    }

    public List<Marker> getListMarkerLabel(MapView mapView, List<Amostra> amostraList){
        List<Marker> markerList = new ArrayList<>();
        for (int i = 0; i < amostraList.size(); i++){
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            Marker point = new Marker(mapView);
            point.setTextLabelBackgroundColor(Color.BLACK);
            point.setTextLabelFontSize(15);
            point.setAnchor(0.5f, 2.3f);
            point.setTextLabelForegroundColor(Color.WHITE);
            point.setTitle(amostraList.get(i).getAm_name());
            point.setIcon(null);
            point.setPosition(new GeoPoint(amostraList.get(i).getAm_geodata_lat(), amostraList.get(i).getAm_geodata_long()));
            markerList.add(point);
        }
        return markerList;
    }

    public void addCircle(double meters){
        ArrayList<GeoPoint> circlePoints = Polygon.pointsAsCircle(geoPointToAnalyse, meters);
        List<GeoPoint> p = new ArrayList<>();
        p.add(geoPointToAnalyse);
        p.add(new GeoPoint(circlePoints.get(30).getLatitude(), circlePoints.get(30).getLongitude()));

        polyline.setPoints(p);
        circle.setPoints(circlePoints);
        mapView.invalidate();

        enableBtnOfAnlyse(SAVE_CIRCLE);
    }

    public void addMarker(GeoPoint center){
        mapView.invalidate();
        newMarker.remove(mapView);
        newMarker.setPosition(center);
        overlayManager.add(newMarker);
    }

    public void showRectTemp(){
        rectTemp = new Polygon();
        rectTemp.setFillColor(colorRedFill);
        rectTemp.setStrokeWidth(2);
        rectTemp.setStrokeColor(colorBlackStroke);
        overlayManager.add(rectTemp);
    }

    class MapOverLay extends Overlay {
        @Override
        public void draw(Canvas c, MapView osmv, boolean shadow) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView){
            Projection projection = mapView.getProjection();
            GeoPoint geoPoint = (GeoPoint) projection.fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
            if (newMarker != null){
                addMarker(geoPoint);
                saveMarker.setImageDrawable(save_green);
                saveMarker.setEnabled(true);
            }
            return true;
        }
    }

    public void showAmostras(){
        AsyncGetPointAmostra generateAmostra = new AsyncGetPointAmostra(dataBaseDarwin, helperAsyncTaskAmostras());
        generateAmostra.execute(getLevantamento().getLev_id_mask());
    }

    public void showRaioFromCircle(){
        circle = new Polygon();
        circle.setStrokeWidth(2);
        circle.setFillColor(colorBlueFill);
        circle.setStrokeColor(colorBlackStroke);
        mapView.getOverlays().add(circle);
    }

    public void showCircleFromAmostra(){
        polyline = new Polyline();
        polyline.setWidth(2);
        polyline.setColor(colorBlackStroke);
        overlayManager.add(polyline);
    }

    public void showAreaLevant(){
        polyAreaLevant = new Polygon();
        polyAreaLevant.setFillColor(colorRedFill);
        polyAreaLevant.setStrokeWidth(2);
        polyAreaLevant.setStrokeColor(colorRedStroke);
        overlayManager.add(polyAreaLevant);
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
                    panelTools.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                    overlayManager.addAll(getListMarkerIcon(mapView, getLista()));
                    overlayManager.addAll(getListMarkerLabel(mapView, getLista()));
                    mapView.invalidate();
                }

            }
        };
    }

    private View.OnClickListener onClickListenerAddCircle(){
        return v -> {
            String m = meterCircle.getText().toString();
            if (!m.isEmpty()){
                if(geoPointToAnalyse != null){
                    addCircle(Double.parseDouble(m));
                    Double areaOfCircle = DarwinGeometryAnalyst.getAreaOfCircle(Double.parseDouble(m));
                    String resultArea = "Área\n" + areaOfCircle + " m²\n" + areaOfCircle/10000 + " ha";
                    resultAreaOfCircle.setText(resultArea);
                }else{
                    Toast.makeText(getContext(), "Sem coordenadas da Amostra", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Adicione um Raio em metros!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onClickListenerAddRectTemp(){
        return v -> {
            String widthInMeters = fild1Polygon.getText().toString();
            String lengthInMeters = fild2Polygon.getText().toString();

            if (!widthInMeters.isEmpty() || !lengthInMeters.isEmpty()){
                List<GeoPoint> geoPointList = DarwinGeometryAnalyst.getRectOfLeftBottomCorner(geoPointToAnalyse, Double.parseDouble(widthInMeters), Double.parseDouble(lengthInMeters));
                rectTemp.setPoints(geoPointList);
                mapView.invalidate();

            }else{
                Toast.makeText(getContext(), "Adicione medidas em todos os campos!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void enableBtnOfAnlyse(int btn){
        switch (btn){
            case 0:
                saveCircle.setImageDrawable(save_green);
                saveCircle.setEnabled(true);
                break;
        }
    }

    public Levantamento getLevantamento(){
        return this.levantamento;
    }
    public void setLevantamento(Levantamento levantamento){
        this.levantamento = levantamento;
    }
}
