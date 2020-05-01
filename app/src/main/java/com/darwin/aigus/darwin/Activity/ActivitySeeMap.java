package com.darwin.aigus.darwin.Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateAmostra;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateRect;
import com.darwin.aigus.darwin.AndroidUltils.DateDarwin;
import com.darwin.aigus.darwin.AndroidUltils.GenerateId;
import com.darwin.aigus.darwin.AndroidUltils.KmlPolygon;
import com.darwin.aigus.darwin.AndroidUltils.OnEditTextChange;
import com.darwin.aigus.darwin.Async.AsyncGetAreaLevant;
import com.darwin.aigus.darwin.Async.AsyncGetPointAmostra;
import com.darwin.aigus.darwin.Geometry.DarwinGeometry;
import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import net.sf.geographiclib.PolygonResult;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
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

public class ActivitySeeMap extends AppCompatActivity {
    private Levantamento levantamento;
    private TextView infoAmostraOnMap, resultAreaOfCircle, resultAreaLevant, titleContLayers;
    private CardView cardProperties;
    private GeoPoint geoPointToAnalyse;
    private DarwinGeometry darwinGeometry;
    private EditText meterCircle;
    private LinearLayout panelInfo, contAnaliseFeicao, contLayers;
    private ProgressBar progressBar;
    private ImageButton btnMorePanelFeicao, saveCircle, removeCircle, addCircle, btnLayers;
    private OverlayManager overlayManager;
    private MapController mapController;
    private MapView mapView;
    private Drawable markerSelected, markerIconDefault, amostra_green, save_green, amostra_gray, save_gray, drawAddCircleGreen, drawAddCircleGray;
    private Marker newMarker, pointAmostra;
    private List<Amostra> lista;
    private MapOverLay mapOverLay;
    private DataBaseDarwin dataBaseDarwin;
    private Menu menus;
    private CheckBox layerArea, layerAmostra, layerAreaRect, layerAreaRadius;
    private String infOfFeicao = null;
    private int idMarkerSelected;
    private int control, controlEdition = 0, selectedEdition = 0;
    private int zoomOfMap;

    private static final int SAVE_CIRCLE = 0;
    private static final int DELETE_CIRCLE = 1;

    private List<GeoPoint> listOfPointsRectTemp;
    private List<GeoPoint> pointsOfAreaLevant = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private List<Marker> setListMarkerIcon = new ArrayList<>();
    private List<Marker> setListMarkerLabel = new ArrayList<>();
    private List<Polygon> setListPolygonRect = new ArrayList<>();
    private Polygon polyAreaLevant = new Polygon();
    private Polygon rectTemp = new Polygon();
    private Polygon circle = new Polygon();
    private Polyline polyline =  new Polyline();

    private AlertDialogCreateRect dialoCreateRect = new AlertDialogCreateRect();

    public Levantamento getLevantamento(){
        return this.levantamento;
    }
    public void setLevantamento(Levantamento levantamento){
        this.levantamento = levantamento;
    }

    public List<Amostra> getLista() {
        return lista;
    }

    public void setLista(List<Amostra> lista) {
        this.lista = lista;
    }

    private Context getContext(){ return this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Levantamento l = (Levantamento)getIntent().getSerializableExtra("lev");
        setLevantamento(l);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getLevantamento().getLev_name());
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dataBaseDarwin = new DataBaseDarwin(getContext());

        mapView = findViewById(R.id.map_one);
        overlayManager = mapView.getOverlayManager();
        mapController = (MapController) mapView.getController();
        infoAmostraOnMap = findViewById(R.id.infoAmostraOnMap);
        progressBar = findViewById(R.id.progressBar);
        panelInfo = findViewById(R.id.panelInfo);
        saveCircle = findViewById(R.id.saveCircle);
        removeCircle = findViewById(R.id.removeCircle);
        meterCircle = findViewById(R.id.meterCircle);
        addCircle = findViewById(R.id.addCircle);
        contAnaliseFeicao = findViewById(R.id.contAnaliseFeicao);
        resultAreaOfCircle = findViewById(R.id.resultAreaOfCircle);
        resultAreaLevant = findViewById(R.id.resultAreaLevant);
        cardProperties = findViewById(R.id.cardProperties);
        layerArea = findViewById(R.id.layerArea);
        layerAmostra = findViewById(R.id.layerAmostra);
        layerAreaRect = findViewById(R.id.layerAreaRect);
        layerAreaRadius = findViewById(R.id.layerAreaRadius);
        btnLayers = findViewById(R.id.btnLayers);
        titleContLayers = findViewById(R.id.titleContLayers);
        contLayers = findViewById(R.id.contLayers);

        mapOverLay = new ActivitySeeMap.MapOverLay();//MUITO IMPORTANTE

        saveCircle.setEnabled(false);
        removeCircle.setEnabled(false);
        addCircle.setEnabled(false);

        Drawable drawAddPolyGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_poly, null);
        Drawable drawAddPolyGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_gray_poly, null);
        Drawable btnMorePanel = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null);
        Drawable btnLessPanel = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null);
        Drawable drawBtnLayersGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_layer_gray, null);
        Drawable drawBtnLayersGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_layer_green, null);
        drawAddCircleGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_circle, null);
        drawAddCircleGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_gray_circle, null);
        amostra_green = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_amostra_green, null);
        amostra_gray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_amostra, null);
        save_green = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save_black_green_24dp, null);
        save_gray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save_black_24dp, null);
        markerIconDefault = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker_darwin, null);
        markerSelected = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker_selected, null);

        //addPolygon.setOnClickListener(onClickListenerAddRectTemp());
        //OnEditTextChange onEditTextChange = new OnEditTextChange(addPolygon, drawAddPolyGreen, drawAddPolyGray);
        //fild1Polygon.addTextChangedListener(onEditTextChange);
        //fild2Polygon.addTextChangedListener(onEditTextChange);
        meterCircle.addTextChangedListener(new OnEditTextChange(addCircle, drawAddCircleGreen, drawAddCircleGray));

        panelInfo.setOnClickListener(v -> panelInfo.setVisibility(View.GONE));

        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setAlignBottom(true);
        scaleBarOverlay.enableScaleBar();

        btnMorePanelFeicao = findViewById(R.id.btnMorePanelFeicao);
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
        mapView.setMapListener(mapMovinListener());

        mapController.setZoom(17);
        overlayManager.add(scaleBarOverlay);
        darwinGeometry = new DarwinGeometry(mapView, overlayManager);

        //CAMADAS DE FEIÇÕES
        showArea();
        darwinGeometry.showAreaLevant(polyAreaLevant);
        showAmostras();
        addCircle.setOnClickListener(onClickListenerAddCircle());
        darwinGeometry.showRaioFromCircle(circle);
        darwinGeometry.showRadiusFromAmostra(polyline);
        darwinGeometry.showRectTemp(rectTemp);

        btnLayers.setImageDrawable(drawBtnLayersGray);
        btnLayers.setOnClickListener(v -> {
            if (btnLayers.getDrawable() == drawBtnLayersGray){
                btnLayers.setImageDrawable(drawBtnLayersGreen);
                contLayers.setVisibility(View.VISIBLE);
                titleContLayers.setVisibility(View.VISIBLE);

            }else{
                btnLayers.setImageDrawable(drawBtnLayersGray);
                contLayers.setVisibility(View.GONE);
                titleContLayers.setVisibility(View.GONE);
            }
        });

        layerArea.setOnClickListener(v -> {
            if(layerArea.isChecked()) {
                polyAreaLevant.setVisible(true);

            }else{
                polyAreaLevant.setVisible(false);
            }
            mapView.invalidate();
        });

        layerAmostra.setOnClickListener(v -> {
            if(layerAmostra.isChecked()) {
                overlayManager.addAll(setListMarkerLabel);
                overlayManager.addAll(setListMarkerIcon);

            }else{
                overlayManager.removeAll(setListMarkerLabel);
                overlayManager.removeAll(setListMarkerIcon);
            }
            mapView.invalidate();
        });

        layerAreaRect.setOnClickListener(v -> {
            if(layerAreaRect.isChecked()) {
                overlayManager.addAll(setListPolygonRect);

            }else{
                overlayManager.removeAll(setListPolygonRect );

            }
            mapView.invalidate();
        });
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
                }
            }
        };
    }

    public List<Marker> getListMarkerIcon(MapView mapView, List<Amostra> amostraList){
        for (int i = 0; i < amostraList.size(); i++){
            Amostra amostra = amostraList.get(i);
            Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
            pointAmostra = new Marker(mapView);
            pointAmostra.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            pointAmostra.setTitle(amostraList.get(i).getAm_name());
            pointAmostra.setIcon(markerIconDefault);
            pointAmostra.setPanToView(true);
            pointAmostra.setPosition(new GeoPoint(amostra.getAm_geodata_lat(), amostra.getAm_geodata_long()));
            pointAmostra.setOnMarkerClickListener(onCLikMarkerAmostra(amostra, i));
            markerList.add(pointAmostra);
        }
        return markerList;
    }

    private Marker.OnMarkerClickListener onCLikMarkerAmostra(Amostra amostra, int id){
        return (marker, mapView) -> {
            marker.setIcon(markerSelected);
            GeoPoint geoPoint = new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            DataBaseDarwin db = new DataBaseDarwin(getContext());
            int totalAmostras = db.countEspecie(amostra.getId_mask());
            infOfFeicao = marker.getTitle() + "\n" + getContext().getString(R.string.coords) +": "+ marker.getPosition() + "\n" + getContext().getString(R.string.totalEspecie) +": "+ totalAmostras;
            geoPointToAnalyse = geoPoint;
            panelInfo.setVisibility(View.VISIBLE);
            infoAmostraOnMap.setText(infOfFeicao);
            btnMorePanelFeicao.setVisibility(View.VISIBLE);
            centerOfMap(geoPoint);
            markerSelected(id);
            return true;
        };
    }

    private void markerSelected(int idMarker){
        markerList.get(idMarkerSelected).setIcon(markerIconDefault);
        markerList.get(idMarker).setIcon(markerSelected);
        idMarkerSelected = idMarker;
        mapView.invalidate();
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

    public void showAmostras(){
        AsyncGetPointAmostra generateAmostra = new AsyncGetPointAmostra(dataBaseDarwin, helperAsyncTaskAmostras());
        generateAmostra.execute(getLevantamento().getLev_id_mask());
    }
    /*
    / ASYBNC DE AMOSTRAS ESTA AQUI NÃO HA ERROS
     */
    private AsyncGetPointAmostra.HelperAsyncTask helperAsyncTaskAmostras(){
        return new AsyncGetPointAmostra.HelperAsyncTask() {
            @Override
            public void onPreExeculte() {

            }

            @Override
            public void onPostExecute(List<Amostra> amostrasList) {
                setLista(amostrasList);

                setListMarkerIcon = getListMarkerIcon(mapView, getLista());
                setListMarkerLabel = getListMarkerLabel(mapView, getLista());

                if (getLista().size() > 0) {
                    control = 1;
                    overlayManager.addAll(setListMarkerLabel);
                    overlayManager.addAll(setListMarkerIcon);
                }

                mapView.invalidate();
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

    private void enableBtnOfAnlyse(int btn){
        switch (btn){
            case 0:
                saveCircle.setImageDrawable(save_green);
                saveCircle.setEnabled(true);
                break;
        }
    }

    class MapOverLay extends Overlay {
        @Override
        public void draw(Canvas c, MapView osmv, boolean shadow) {

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView){
            Projection projection = mapView.getProjection();
            GeoPoint geoPoint = (GeoPoint) projection.fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
                if (newMarker != null) {

                    if (zoomOfMap > 17) {
                        darwinGeometry.addMarker(geoPoint, newMarker);
                        MenuItem saveAmostraItem = menus.findItem(R.id.action_save_ediction);
                        saveAmostraItem.setIcon(save_green);
                        saveAmostraItem.setEnabled(true);

                    }else{
                        Toast.makeText(getContext(), "Use um Zoom maior para dar precisão às coordenadas da Amostra. Zoom atual: " +zoomOfMap , Toast.LENGTH_LONG).show();
                    }
                }
            return true;
        }
    }

    public void onClickAddAmostra(MenuItem menuItem){
        if (menuItem.getIcon() == amostra_gray){
            menuItem.setIcon(amostra_green);

            Drawable drawable = ResourcesCompat.getDrawable (getResources(), R.drawable.ic_add_marker, null);
            newMarker = new Marker(mapView);
            newMarker.setIcon(drawable);
            newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            newMarker.setDraggable(true);
            overlayManager.add(mapOverLay);
            Toast.makeText(getContext(), R.string.modeAddAmostraAtive, Toast.LENGTH_SHORT).show();
            selectedEdition = menuItem.getItemId();

        }else{
            menuItem.setIcon(amostra_gray);
            MenuItem saveAmostraItem = menus.findItem(R.id.action_save_ediction);
            saveAmostraItem.setIcon(save_gray);
            saveAmostraItem.setEnabled(false);

            if(newMarker != null){
                newMarker.remove(mapView);
                newMarker = null;
            }
            overlayManager.remove(mapOverLay);
            mapView.invalidate();
            Toast.makeText(getContext(), R.string.modeAddAmostraDesative, Toast.LENGTH_SHORT).show();
        }
    }

    public void centerOfMap(GeoPoint center){
        mapController.animateTo(center);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_map, menu);
        menus = menu;
        MenuItem itemAddAmostra = menu.findItem(R.id.action_add_amostra);
        itemAddAmostra.setIcon(amostra_gray);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_amostra:
                if (selectedEdition == 0){
                    onClickAddAmostra(item);

                }else{
                    Toast.makeText(this, "Existe edições em andamento. Salve-as ou desfaça!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_add_rect:
                if (selectedEdition == 0){
                    dialoCreateRect.show(getSupportFragmentManager(), null);
                    dialoCreateRect.setHelpeAlertDialogRect(helpeAlertDialogRect());
                    selectedEdition = R.id.action_add_rect;

                }else{
                    Toast.makeText(this, "Existe edições em andamento. Salve-as ou desfaça!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.action_add_circle:
                if (selectedEdition == 0){
                    Toast.makeText(this, "Ativado", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this, "Existe edições em andamento. Salve-as ou desfaça!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_undo:
                if (selectedEdition != 0){
                    undoEdition();
                }
                break;

            case R.id.action_save_ediction:
                saveEdition();
                break;

            case android.R.id.home:
                finish();
                return true;
        }

        return true;
    }

    private void saveEdition(){
        switch (selectedEdition){
            case R.id.action_add_amostra:
                onClickSaveAmostra();
                break;

            case R.id.action_add_rect:
                Toast.makeText(this, "SALVAS: RECT", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_add_circle:
                Toast.makeText(this, "SALVAS: CIRCLE", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void undoEdition(){
        switch (selectedEdition){
            case R.id.action_add_amostra:
                Toast.makeText(this, "DESFEITO: AMOSTRAS", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_add_rect:
                overlayManager.removeAll(setListPolygonRect);
                break;

            case R.id.action_add_circle:
                Toast.makeText(this, "DESFEITO: CIRCLE", Toast.LENGTH_SHORT).show();
                break;
        }

        selectedEdition = 0;
        mapView.invalidate();
    }

    public void onClickSaveAmostra(){
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

            overlayManager.removeAll(setListMarkerIcon);
            overlayManager.removeAll(setListMarkerLabel);

            newMarker.remove(mapView);
            markerList.clear();
            idMarkerSelected = 0;
            selectedEdition = 0;
            showAmostras();
            mapView.invalidate();

            menus.findItem(R.id.action_add_amostra).setIcon(amostra_gray);
            menus.findItem(R.id.action_save_ediction).setIcon(save_gray).setEnabled(false);
            newMarker = null;
        }
    }

    private AlertDialogCreateRect.HelpeAlertDialogRect helpeAlertDialogRect(){
        return (height, width, general, corner) -> {
            menus.findItem(R.id.action_save_ediction).setIcon(save_green);

            setListPolygonRect = DarwinGeometryAnalyst.getListPolygonRect(corner, getLista(), width, height);

            overlayManager.addAll(setListPolygonRect);
            mapView.invalidate();

            dialoCreateRect.dismiss();

        };
    }

    public MapListener mapMovinListener(){
        return new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                zoomOfMap = event.getZoomLevel();
                return true;
            }
        };
    }
}
