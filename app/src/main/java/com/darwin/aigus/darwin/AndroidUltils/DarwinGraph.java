package com.darwin.aigus.darwin.AndroidUltils;

import android.graphics.Paint;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.List;

public class DarwinGraph {
    private GraphView graphView;
    private LegendRenderer legend;
    private String title;
    private DataPoint[] dataPoints;
    private Paint paintLine;
    private Paint p;
    private List<double[]> listData;
    private int color;

    public DarwinGraph() {
    }

    public DarwinGraph(GraphView graphView, int color) {
        this.graphView = graphView;
        this.color = color;
    }

    public DarwinGraph(String title, DataPoint[] dataPoints, Paint paintLine, int color) {
        this.title = title;
        this.dataPoints = dataPoints;
        this.paintLine = paintLine;
        this.color = color;
    }

    public DarwinGraph(DataPoint[] dataPoints, Paint p, int color) {
        this.dataPoints = dataPoints;
        this.p = p;
        this.color = color;
    }

    public DarwinGraph(GraphView graphView, LegendRenderer legend, String title, int color) {
        this.graphView = graphView;
        this.legend = legend;
        this.title = title;
        this.color = color;
    }

    public DarwinGraph(List<double[]> listData) {
        this.listData = listData;
    }

    public DataPoint[] getDataPoints(int indexData){
        int h = listData.size();
        DataPoint[] pointsData = new DataPoint[h];

        for (int i = 0; i < h; i++){
            double[] s = listData.get(i);
            pointsData[i] = new DataPoint(i, s[indexData]);
        }
        return pointsData;
    }

    public LineGraphSeries<DataPoint> getLineGraph(){
        LineGraphSeries<DataPoint> desvioHeight = new LineGraphSeries<>(dataPoints);
        desvioHeight.setDataPointsRadius(8);
        desvioHeight.setDrawDataPoints(true);
        desvioHeight.setTitle(title);
        desvioHeight.setCustomPaint(paintLine);
        desvioHeight.setColor(color);
        desvioHeight.setAnimated(true);
        return  desvioHeight;
    }

    public PointsGraphSeries<DataPoint> getPointsToLineGraph(){
        PointsGraphSeries<DataPoint> pointsToLineGraph = new PointsGraphSeries<>(dataPoints);
        pointsToLineGraph.setSize(8);
        pointsToLineGraph.setCustomShape((canvas, paint, x, y, dataPoint) -> {
            paint.setTextSize(16);
            paint.setColor(color);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawCircle(x, y, 4, p);
            canvas.drawText(String.valueOf(dataPoint.getY()), x, y-15, paint);
        });

        return pointsToLineGraph;
    }

    public LegendRenderer getLegend(){
        LegendRenderer legendRenderer = new LegendRenderer(graphView);
        legendRenderer.setWidth(160);
        legendRenderer.setSpacing(10);
        legendRenderer.setBackgroundColor(color);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);

        return legendRenderer;
    }

    public GridLabelRenderer getGrid(){
        GridLabelRenderer gridLabelRenderer = new GridLabelRenderer(graphView);
        gridLabelRenderer.setVerticalLabelsVisible(false);
        gridLabelRenderer.setGridColor(color);
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setLabelsSpace(10);
        gridLabelRenderer.setLabelHorizontalHeight(65);
        gridLabelRenderer.setHorizontalLabelsAngle(90);
        gridLabelRenderer.setHorizontalAxisTitle(title);
        gridLabelRenderer.setTextSize(12);
        graphView.setLegendRenderer(legend);

        return gridLabelRenderer;
    }
}
