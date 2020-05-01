package com.darwin.aigus.darwin.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.AndroidUltils.DarwinGraph;
import com.darwin.aigus.darwin.AndroidUltils.DarwinStatistics;
import com.darwin.aigus.darwin.AndroidUltils.KmlPolygon;
import com.darwin.aigus.darwin.Async.AsyncCountEspecieByAmostra;
import com.darwin.aigus.darwin.Async.AsyncGenerateArea;
import com.darwin.aigus.darwin.Async.AsyncGetAmostras;
import com.darwin.aigus.darwin.Async.AsyncGetAreaLevant;
import com.darwin.aigus.darwin.Async.AsyncGetStatisticEspecie;
import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import net.sf.geographiclib.PolygonResult;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraph extends AppCompatActivity {
    private DataBaseDarwin dataBaseDarwin;
    private String superTitleHeight, superTitleDiameter;
    private TextView seeDataTabular, seeDataTabular1, idareaPerimeter;
    private LinearLayout contDataTabular, contDataTabular1;
    private GraphView graphView, graphBarras, graphDiameter;
    private GridLabelRenderer gridLabelRenderer, gridDiameter;
    private RadioButton layerMediaHeight, layerVarHeight, layerDesvioHeight, layerAllHeight, layerMediaDiameter, layerVarDiameter, layerDesvioDiameter, layerAllDiameter;
    private RadioGroup layersHeight, layersDiameter;
    private List<Amostra> amostras;
    private List<KmlPolygon> kmlPolygonList = null;
    private Levantamento levantamento;
    private Paint pLineMedia, pLineVar, pLineDes, p;

    public List<Amostra> getAmostras() {
        return amostras;
    }

    public void setAmostras(List<Amostra> amostras) {
        this.amostras = amostras;
    }

    private Context getContext(){return  this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        levantamento = (Levantamento)getIntent().getSerializableExtra("lev");
        dataBaseDarwin = new DataBaseDarwin(getContext());

        AsyncGetAmostras getAmostras = new AsyncGetAmostras(dataBaseDarwin, helperAsyncGetAmostras());
        getAmostras.execute(levantamento.getLev_id_mask());

        AsyncGetAreaLevant getAreaLevant = new AsyncGetAreaLevant(dataBaseDarwin, helperAsyncArea());
        getAreaLevant.execute(levantamento.getLev_id_mask());

        superTitleHeight = getContext().getResources().getString(R.string.subTitleGraph2);
        superTitleDiameter = getContext().getResources().getString(R.string.subTitleGraph3);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.action_data);
            getSupportActionBar().setSubtitle(levantamento.getLev_name());
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int colorLegend = getContext().getResources().getColor(R.color.colorGrayOneAlpha);
        String titleAmostras = getContext().getResources().getString(R.string.amostras);

        graphDiameter = findViewById(R.id.graphDiameter);
        LegendRenderer legendGraphDiamter = new DarwinGraph(graphDiameter, colorLegend).getLegend();
        gridDiameter = graphDiameter.getGridLabelRenderer();
        gridDiameter.setVerticalLabelsVisible(false);
        gridDiameter.setGridColor(R.color.gray);
        gridDiameter.setHighlightZeroLines(false);
        gridDiameter.setLabelsSpace(10);
        gridDiameter.setLabelHorizontalHeight(60);
        gridDiameter.setHorizontalLabelsAngle(90);
        gridDiameter.setHorizontalAxisTitle(titleAmostras);
        gridDiameter.setTextSize(12);
        graphDiameter.setLegendRenderer(legendGraphDiamter);

        graphView = findViewById(R.id.graph);
        LegendRenderer legendGraphHeight = new DarwinGraph(graphView, colorLegend).getLegend();
        gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setVerticalLabelsVisible(false);
        gridLabelRenderer.setGridColor(R.color.gray);
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setLabelsSpace(10);
        gridLabelRenderer.setLabelHorizontalHeight(60);
        gridLabelRenderer.setHorizontalLabelsAngle(90);
        gridLabelRenderer.setHorizontalAxisTitle(titleAmostras);
        gridLabelRenderer.setTextSize(12);
        graphView.setLegendRenderer(legendGraphHeight);

        graphBarras = findViewById(R.id.graphBarras);

        idareaPerimeter = findViewById(R.id.areaPerimeter);

        layersHeight = findViewById(R.id.layersHeight);
        layerMediaHeight = findViewById(R.id.layerMediaHeight);
        layerVarHeight = findViewById(R.id.layerVarHeight);
        layerDesvioHeight = findViewById(R.id.layerDesvioHeight);
        layerAllHeight = findViewById(R.id.layerAllHeight);

        layersDiameter = findViewById(R.id.layersDiameter);
        layerMediaDiameter = findViewById(R.id.layerMediaDiameter);
        layerVarDiameter = findViewById(R.id.layerVarDiameter);
        layerDesvioDiameter = findViewById(R.id.layerDesvioDiameter);
        layerAllDiameter = findViewById(R.id.layerAllDiameter);

        contDataTabular = findViewById(R.id.contDataTabular);
        seeDataTabular = findViewById(R.id.seeDataTabular);

        contDataTabular1 = findViewById(R.id.contDataTabular1);
        seeDataTabular1 = findViewById(R.id.seeDataTabular1);

        seeDataTabular.setOnClickListener(v -> {
            if (contDataTabular.getVisibility() == View.GONE){
                seeDataTabular.setText(R.string.fechar);
                contDataTabular.setVisibility(View.VISIBLE);

            }else{
                contDataTabular.setVisibility(View.GONE);
                seeDataTabular.setText(R.string.seeDataTab);
            }

        });

        seeDataTabular1.setOnClickListener(v -> {
            if (contDataTabular1.getVisibility() == View.GONE){
                seeDataTabular1.setText(R.string.fechar);
                contDataTabular1.setVisibility(View.VISIBLE);

            }else{
                contDataTabular1.setVisibility(View.GONE);
                seeDataTabular1.setText(R.string.seeDataTab);
            }

        });

        p = new Paint();
        p.setColor(Color.WHITE);

        pLineMedia = new Paint();
        pLineMedia.setStrokeWidth(3);
        pLineMedia.setColor(Color.BLUE);

        pLineVar = new Paint();
        pLineVar.setStrokeWidth(3);
        pLineVar.setColor(Color.BLACK);

        pLineDes = new Paint();
        pLineDes.setStrokeWidth(3);
        pLineDes.setColor(Color.RED);
    }

    private AsyncGetAmostras.HelperAsyncGetAmostras helperAsyncGetAmostras(){
        return new AsyncGetAmostras.HelperAsyncGetAmostras() {
            @Override
            public void onPreHelperExecute() {

            }

            @Override
            public void onPosHelperExecute(List<Amostra> amostraList) {
                setAmostras(amostraList);
                amostras = amostraList;
                AsyncCountEspecieByAmostra especieByAmostra = new AsyncCountEspecieByAmostra(dataBaseDarwin, helperAsync(), amostraList);
                especieByAmostra.execute();

                generateStatisticHeight(amostraList);
                generateStatisticDiameter(amostraList);
            }
        };
    }

    private AsyncCountEspecieByAmostra.HelperAsync helperAsync(){
        return new AsyncCountEspecieByAmostra.HelperAsync() {
            @Override
            public void onPreHelperExecute() {

            }

            @Override
            public void onPosHelperExecute(DataPoint[] dataPoint) {
                if (dataPoint != null) {

                    BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPoint);
                    graphBarras.addSeries(barGraphSeries);

                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphBarras);
                    staticLabelsFormatter.setHorizontalLabels(namesAmostra(getAmostras()));
                    graphBarras.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graphBarras.getGridLabelRenderer().setTextSize(12);
                    graphBarras.getGridLabelRenderer().setLabelsSpace(10);
                    graphBarras.getGridLabelRenderer().setVerticalLabelsVisible(false);
                    graphBarras.getGridLabelRenderer().setHighlightZeroLines(false);
                    graphBarras.getGridLabelRenderer().setLabelHorizontalHeight(65);
                    graphBarras.getGridLabelRenderer().setHorizontalLabelsAngle(90);
                    graphBarras.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.CENTER);

                    barGraphSeries.setValueDependentColor(data -> Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100));
                    barGraphSeries.setSpacing(10);
                    barGraphSeries.setDrawValuesOnTop(true);
                    barGraphSeries.setValuesOnTopColor(Color.RED);
                }
            }
        };
    }

    public void generateStatisticHeight(List<Amostra> amostraList){
        AsyncGetStatisticEspecie statisticEspecie = new AsyncGetStatisticEspecie(amostraList, dataBaseDarwin, helperAsyncStatisticEspecie());
        statisticEspecie.execute(DarwinStatistics.HEIGHT);
    }

    public void generateStatisticDiameter(List<Amostra> amostraList){
        AsyncGetStatisticEspecie statisticEspecie = new AsyncGetStatisticEspecie(amostraList, dataBaseDarwin, helperAsyncStatisticDiameter());
        statisticEspecie.execute(DarwinStatistics.DIAMETER);
    }

    private AsyncGetStatisticEspecie.HelperAsyncStatisticEspecie helperAsyncStatisticEspecie(){
        return new AsyncGetStatisticEspecie.HelperAsyncStatisticEspecie() {

            private ProgressBar progress1 = findViewById(R.id.progress1);

            @Override
            public void onPreHelperExecute() {
                progress1.setVisibility(View.VISIBLE);
                layerMediaHeight.setEnabled(false);
                layerVarHeight.setEnabled(false);
                layerDesvioHeight.setEnabled(false);

            }

            @Override
            public void onPosHelperExecute(List<double[]> listData) {
                progress1.setVisibility(View.INVISIBLE);
                if(listData != null){
                    layerMediaHeight.setEnabled(true);
                    layerVarHeight.setEnabled(true);
                    layerDesvioHeight.setEnabled(true);

                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
                    staticLabelsFormatter.setHorizontalLabels(namesAmostra(amostras));
                    gridLabelRenderer.setLabelFormatter(staticLabelsFormatter);

                    DarwinGraph dataPoints = new DarwinGraph(listData);

                    DarwinGraph pointsLineMediaT = new DarwinGraph(dataPoints.getDataPoints(0), p, Color.BLUE);
                    DarwinGraph pointsLineVarT = new DarwinGraph(dataPoints.getDataPoints(1), p, Color.BLACK);
                    DarwinGraph pointsLineDesT = new DarwinGraph(dataPoints.getDataPoints(2), p, Color.RED);

                    DarwinGraph graphLineMedia = new DarwinGraph(layerMediaHeight.getText().toString(), dataPoints.getDataPoints(0), pLineMedia, Color.BLUE);
                    DarwinGraph graphLineVar = new DarwinGraph(layerVarHeight.getText().toString(), dataPoints.getDataPoints(1), pLineVar, Color.BLACK);
                    DarwinGraph graphLineDes = new DarwinGraph(layerDesvioHeight.getText().toString(), dataPoints.getDataPoints(2), pLineDes, Color.RED);

                    graphView.setTitle(superTitleHeight + " / " + layerMediaHeight.getText().toString());
                    graphView.addSeries(graphLineMedia.getLineGraph());
                    graphView.addSeries(pointsLineMediaT.getPointsToLineGraph());

                    graphView.getLegendRenderer().setVisible(true);
                    graphView.setOnClickListener(v -> {
                        if(graphView.getLegendRenderer().isVisible()){
                            graphView.getLegendRenderer().setVisible(false);

                        }else{
                            graphView.getLegendRenderer().setVisible(true);
                        }
                    });

                    layersHeight.setOnCheckedChangeListener((group, checkedId) -> {
                        graphView.removeAllSeries();
                        switch (checkedId){

                            case R.id.layerMediaHeight:
                                graphView.setTitle(superTitleHeight + " / " + layerMediaHeight.getText().toString());
                                graphView.addSeries(graphLineMedia.getLineGraph());
                                graphView.addSeries(pointsLineMediaT.getPointsToLineGraph());
                                break;

                            case R.id.layerVarHeight:
                                graphView.setTitle(superTitleHeight + " / " + layerVarHeight.getText().toString());
                                graphView.addSeries(graphLineVar.getLineGraph());
                                graphView.addSeries(pointsLineVarT.getPointsToLineGraph());
                                break;

                            case R.id.layerDesvioHeight:
                                graphView.setTitle(superTitleHeight + " / " + layerDesvioHeight.getText().toString());
                                graphView.addSeries(graphLineDes.getLineGraph());
                                graphView.addSeries(pointsLineDesT.getPointsToLineGraph());
                                break;

                            case R.id.layerAllHeight:
                                graphView.setTitle(superTitleHeight);
                                graphView.addSeries(graphLineMedia.getLineGraph());
                                graphView.addSeries(pointsLineMediaT.getPointsToLineGraph());

                                graphView.addSeries(graphLineVar.getLineGraph());
                                graphView.addSeries(pointsLineVarT.getPointsToLineGraph());

                                graphView.addSeries(graphLineDes.getLineGraph());
                                graphView.addSeries(pointsLineDesT.getPointsToLineGraph());
                                break;
                        }
                    });

                } else {
                    Toast.makeText(ActivityGraph.this, "Error", Toast.LENGTH_SHORT).show();}
            }
        };
    }

    private AsyncGetStatisticEspecie.HelperAsyncStatisticEspecie helperAsyncStatisticDiameter(){
        return new AsyncGetStatisticEspecie.HelperAsyncStatisticEspecie() {
            @Override
            public void onPreHelperExecute() {

            }

            @Override
            public void onPosHelperExecute(List<double[]> listData) {
                if (listData != null){
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphDiameter);
                    staticLabelsFormatter.setHorizontalLabels(namesAmostra(amostras));
                    gridDiameter.setLabelFormatter(staticLabelsFormatter);

                    DarwinGraph dataPoints = new DarwinGraph(listData);

                    DarwinGraph pointsLineMediaT = new DarwinGraph(dataPoints.getDataPoints(0), p, Color.BLUE);
                    DarwinGraph pointsLineVarT = new DarwinGraph(dataPoints.getDataPoints(1), p, Color.BLACK);
                    DarwinGraph pointsLineDesT = new DarwinGraph(dataPoints.getDataPoints(2), p, Color.RED);

                    DarwinGraph graphLineMedia = new DarwinGraph(layerMediaDiameter.getText().toString(), dataPoints.getDataPoints(0), pLineMedia, Color.BLUE);
                    DarwinGraph graphLineVar = new DarwinGraph(layerVarDiameter.getText().toString(), dataPoints.getDataPoints(1), pLineVar, Color.BLACK);
                    DarwinGraph graphLineDes = new DarwinGraph(layerDesvioDiameter.getText().toString(), dataPoints.getDataPoints(2), pLineDes, Color.RED);

                    graphDiameter.setTitle(superTitleDiameter + " / " + layerMediaHeight.getText().toString());
                    graphDiameter.addSeries(graphLineMedia.getLineGraph());
                    graphDiameter.addSeries(pointsLineMediaT.getPointsToLineGraph());
                    graphDiameter.getLegendRenderer().setVisible(true);

                    graphView.setOnClickListener(v -> {
                        if(graphDiameter.getLegendRenderer().isVisible()){
                            graphDiameter.getLegendRenderer().setVisible(false);

                        }else{
                            graphDiameter.getLegendRenderer().setVisible(true);
                        }
                    });

                    layersDiameter.setOnCheckedChangeListener((group, checkedId) -> {
                        graphDiameter.removeAllSeries();
                        switch (checkedId) {

                            case R.id.layerMediaDiameter:
                                graphDiameter.setTitle(superTitleDiameter + " / " + layerMediaDiameter.getText().toString());
                                graphDiameter.addSeries(graphLineMedia.getLineGraph());
                                graphDiameter.addSeries(pointsLineMediaT.getPointsToLineGraph());
                                break;

                            case R.id.layerVarDiameter:
                                graphDiameter.setTitle(superTitleDiameter + " / " + layerVarDiameter.getText().toString());
                                graphDiameter.addSeries(graphLineVar.getLineGraph());
                                graphDiameter.addSeries(pointsLineVarT.getPointsToLineGraph());
                                break;

                            case R.id.layerDesvioDiameter:
                                graphDiameter.setTitle(superTitleDiameter + " / " + layerDesvioDiameter.getText().toString());
                                graphDiameter.addSeries(graphLineDes.getLineGraph());
                                graphDiameter.addSeries(pointsLineDesT.getPointsToLineGraph());
                                break;

                            case R.id.layerAllDiameter:
                                graphDiameter.setTitle(superTitleDiameter);
                                graphDiameter.addSeries(graphLineMedia.getLineGraph());
                                graphDiameter.addSeries(pointsLineMediaT.getPointsToLineGraph());

                                graphDiameter.addSeries(graphLineVar.getLineGraph());
                                graphDiameter.addSeries(pointsLineVarT.getPointsToLineGraph());

                                graphDiameter.addSeries(graphLineDes.getLineGraph());
                                graphDiameter.addSeries(pointsLineDesT.getPointsToLineGraph());
                                break;
                        }
                    });

                }else {
                    Toast.makeText(ActivityGraph.this, "Error", Toast.LENGTH_SHORT).show();

                }

            }
        };
    }

    private AsyncGetAreaLevant.HelperAsync helperAsyncArea(){
        return new AsyncGetAreaLevant.HelperAsync() {
            @Override
            public void onHelperPreExecute() {

            }

            @Override
            public void onHelperPosExecute(List<KmlPolygon> KmlPolygon) {
                if (KmlPolygon != null){
                    List<GeoPoint> geoPointList = new ArrayList<>();

                    for (int i = 0; i < KmlPolygon.size();i++){
                        geoPointList.add(new GeoPoint(KmlPolygon.get(i).getLon(), KmlPolygon.get(i).getLat()));
                    }

                    PolygonResult polygonResult = DarwinGeometryAnalyst.getAreaPerimeterOfPolygon(geoPointList);
                    String resultAream2 = String.valueOf(polygonResult.area);
                    String resultAreaHa = String.valueOf(polygonResult.area/10000);
                    String resultPerimeter = String.valueOf(polygonResult.perimeter);
                    String finalResult = "AREA: " + resultAream2 + "m² " + resultAreaHa + " ha\nPERIMETER: " + resultPerimeter;

                    idareaPerimeter.setText(finalResult);
                }else{
                    idareaPerimeter.setText("SEM DADOS GEOGRAFICO PARA ESTE LEVAMNTAMENTO");

                }

            }
        };
    }

    private String[] namesAmostra(List<Amostra> amostraList){
        int tA = amostraList.size();
        String[] names = new String[tA];
        for (int i = 0; i < tA; i++){
            names[i] = amostraList.get(i).getAm_name();
        }
        return names;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return true;
    }
}
