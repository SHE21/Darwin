package com.darwin.aigus.darwin.Geometry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReaderKml {
    private File fileKml;

    public ReaderKml(File fileKml) {
        this.fileKml = fileKml;
    }

    public String getPolygonString(){
        String kml = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileKml);

            NodeList nodelist = doc.getElementsByTagName("outerBoundaryIs");
            int tamanhoList = nodelist.getLength();
            if(tamanhoList == 1){

                for(int i = 0; i < tamanhoList; i++){
                    kml = nodeList(nodelist, i);
                }

            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            kml = null;

        }
        return kml;
    }

    private String nodeList(NodeList nodelist, int item){
        String kml = null;
        Node folder = nodelist.item(item);
        if(folder.getNodeType() == Node.ELEMENT_NODE){
            Element elementFolder = (Element) folder;
            NodeList placemark = elementFolder.getChildNodes();

            int totalplacemark = placemark.getLength();
            for(int j = 0; j < totalplacemark; j++){
                Node nodePlacemark = placemark.item(j);
                if(nodePlacemark.getNodeType() == Node.ELEMENT_NODE){
                    Element nodepoint = (Element)nodePlacemark;
                    kml =  nodepoint.getTextContent();
                }
            }
        }
        return kml;
    }

    public List<DarwinPoint> getPoint(){
        List<DarwinPoint> kml = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileKml);
            NodeList nodelist = doc.getElementsByTagName("Point");
            int tamanhoList = nodelist.getLength();

            if(tamanhoList >= 2){
                for(int i = 0; i < tamanhoList; i++){
                    String s = nodeList(nodelist, i);
                    kml.add(new DarwinPoint(stringToLong(s), stringToLat(s)));
                }
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            kml = null;

        }
        return kml;
    }

    private static Double stringToLat(String coords){
        String[] lat  = coords.split(",");
        return Double.parseDouble(lat[0]);
    }

    private static Double stringToLong(String coords){
        String[] lon  = coords.split(",");
        return Double.parseDouble(lon[1]);

    }
}
