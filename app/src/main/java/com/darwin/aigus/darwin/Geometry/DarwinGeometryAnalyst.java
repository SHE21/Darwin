package com.darwin.aigus.darwin.Geometry;

import com.darwin.aigus.darwin.Modelos.Amostra;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.PolygonArea;
import net.sf.geographiclib.PolygonResult;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 14/03/2018.
 */

public class DarwinGeometryAnalyst {
    /*
        imageButtonList.add(leftTop);0
        imageButtonList.add(centerTop);1
        imageButtonList.add(rightTop);2
        imageButtonList.add(leftBottom);3
        imageButtonList.add(rightBottom);4
        imageButtonList.add(centerBottom);5
        imageButtonList.add(leftCenter);6
        imageButtonList.add(rightCenter);7
        imageButtonList.add(centerCenter);8
     */

    public static final int LEFT_TOP = 0;
    public static final int CENTER_TOP = 1;
    public static final int RIGHT_TOP = 2; //RightBottomCorner
    public static final int LEFT_BOTTOM = 3; //LeftBottomCorner
    public static final int RIGHT_BOTTOM = 4; //getRectOfTopCenter
    public static final int CENTER_BOTTOM = 5; //getRectOfBttomCenter
    public static final int LEFT_CENTER = 6; //RectOfRightCenter
    public static final int RIGHT_CENTER = 7; //RectOfLeftCenter
    public static final int CENTER_CENTER = 8; //RectOfLeftCenter

    public static List<GeoPoint> getRectOfLeftTopCorner(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p1 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
        GeoPoint p2 = p1.destinationPoint(widthInMeters, 90.0f);
        GeoPoint p4 = p1.destinationPoint(lengthInMeters, 180.0f);
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), p2.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfRightTopCorner(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p1 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
        GeoPoint p2 = p1.destinationPoint(widthInMeters, 270.0f);
        GeoPoint p4 = p1.destinationPoint(lengthInMeters, 180.0f);
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), p2.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfRightBottomCorner(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p1 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
        GeoPoint p2 = p1.destinationPoint(widthInMeters, 270.0f);
        GeoPoint p4 = p1.destinationPoint(lengthInMeters, 0);
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), p2.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfLeftBottomCorner(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p1 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
        GeoPoint p2 = p1.destinationPoint(widthInMeters, 90.0f);
        GeoPoint p4 = p1.destinationPoint(lengthInMeters, 0.f);
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), p2.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfTopCenter(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p0 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());

        GeoPoint p1 = p0.destinationPoint(lengthInMeters/2, 270.0f);
        GeoPoint p4 = p0.destinationPoint(lengthInMeters/2, 90.0f);
        GeoPoint q0 = p0.destinationPoint(widthInMeters, 180.0f);

        GeoPoint p2 = new GeoPoint(q0.getLatitude(), p1.getLongitude());
        GeoPoint p3 = new GeoPoint(q0.getLatitude(), p4.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfBttomCenter(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p0 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());

        GeoPoint p1 = p0.destinationPoint(lengthInMeters/2, 270.0f);
        GeoPoint p4 = p0.destinationPoint(lengthInMeters/2, 90.0f);
        GeoPoint q0 = p0.destinationPoint(widthInMeters, 0);

        GeoPoint p2 = new GeoPoint(q0.getLatitude(), p1.getLongitude());
        GeoPoint p3 = new GeoPoint(q0.getLatitude(), p4.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfRightCenter(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p0 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());

        GeoPoint p1 = p0.destinationPoint(widthInMeters/2, 180.0f);
        GeoPoint p4 = p0.destinationPoint(widthInMeters/2, 0.0f);
        GeoPoint q0 = p0.destinationPoint(lengthInMeters, 270.0f);

        GeoPoint p2 = new GeoPoint(p1.getLatitude(), q0.getLongitude());
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), q0.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<GeoPoint> getRectOfLeftCenter(GeoPoint geoPoint, double widthInMeters, double lengthInMeters){
        List<GeoPoint> geoPointList = new ArrayList<>(4);
        GeoPoint p0 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());

        GeoPoint p1 = p0.destinationPoint(widthInMeters/2, 0.0f);
        GeoPoint p4 = p0.destinationPoint(widthInMeters/2, 180.0f);
        GeoPoint q0 = p0.destinationPoint(lengthInMeters, 90.0f);

        GeoPoint p2 = new GeoPoint(p1.getLatitude(), q0.getLongitude());
        GeoPoint p3 = new GeoPoint(p4.getLatitude(), q0.getLongitude());

        geoPointList.add(p1);
        geoPointList.add(p2);
        geoPointList.add(p3);
        geoPointList.add(p4);

        return geoPointList;
    }

    public static List<Polygon> getListPolygonRect(int corner, List<Amostra> amostraList, double width, double height){
        List<Polygon> polygonList = new ArrayList<>();

        for (int i = 0; i < amostraList.size(); i++){
            GeoPoint geoPoint = new GeoPoint(amostraList.get(i).getAm_geodata_lat(), amostraList.get(i).getAm_geodata_long());
            List<GeoPoint> geoPointList = getListGeopoint(corner,geoPoint, width, height);
            polygonList.add(DarwinGeometry.getPolygonRect(geoPointList));
        }

        return polygonList;
    }

    public static List<GeoPoint> getListGeopoint(int corner, GeoPoint geoPoint, double width, double height){
        List<GeoPoint> geoPointList = new ArrayList<>();

        switch (corner){
            case LEFT_TOP:
                geoPointList = getRectOfLeftTopCorner(geoPoint, width, height);
                break;
            case LEFT_BOTTOM:
                geoPointList = getRectOfLeftBottomCorner(geoPoint, width, height);
                break;
            case LEFT_CENTER:
                geoPointList = getRectOfLeftCenter(geoPoint, width, height);
                break;
            case RIGHT_TOP:
                geoPointList = getRectOfRightTopCorner(geoPoint, width, height);
                break;
            case RIGHT_BOTTOM:
                geoPointList = getRectOfRightBottomCorner(geoPoint, width, height);
                break;
            case RIGHT_CENTER:
                geoPointList = getRectOfRightCenter(geoPoint, width, height);
                break;
            case CENTER_TOP:
                geoPointList = getRectOfTopCenter(geoPoint, width, height);
                break;
            case CENTER_BOTTOM:
                geoPointList = getRectOfBttomCenter(geoPoint, width, height);
        }

        return geoPointList;
    }

    public static double getAreaOfCircle(double radius){
        double r = radius*radius;
        return Math.PI*r;

    }

    public static PolygonResult getAreaPerimeterOfPolygon(List<GeoPoint> geoPointList){
        PolygonArea p =  new PolygonArea(Geodesic.WGS84, false);

        for (int i = 0; i < geoPointList.size(); i++){
            p.AddPoint(geoPointList.get(i).getLatitude(), geoPointList.get(i).getLongitude());
        }

        return p.Compute();
    }

    public static boolean validateCoord(double coord){
        return -180 >= coord && coord <= 180;
    }
}
