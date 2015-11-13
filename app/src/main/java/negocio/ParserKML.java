package negocio;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mpoo.ruralmaps.ruralmaps.Placemark;
import com.mpoo.ruralmaps.ruralmaps.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParserKML {

    public static ArrayList<Placemark> getPlaces(InputStream kmlContent)
            throws SAXException, IOException, ParserConfigurationException {
        ArrayList<Placemark> places = new ArrayList<Placemark>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(kmlContent);
        Element root = dom.getDocumentElement();
        NodeList items = root.getElementsByTagName("Placemark");
        Log.d("parsing", "" + items.getLength());
        for (int i = 0; i < items.getLength(); i++) {
            Placemark place = new Placemark();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String name = property.getNodeName();
                if (name.equalsIgnoreCase("name")
                        && property.getFirstChild() != null) {
                    place.setName(property.getFirstChild().getNodeValue());
                } else if (name.equalsIgnoreCase("styleUrl")
                        && property.getFirstChild() != null) {

                    place.setIconID(property.getFirstChild()
                            .getNodeValue().split("-")[1]);

                } else if (name.equalsIgnoreCase("description")
                        && property.getFirstChild() != null) {
                    place.setDescription(property.getFirstChild()
                            .getNodeValue());
                } else if (name.equalsIgnoreCase("Point")
                        && property.getFirstChild() != null) {
                    NodeList point = property.getChildNodes();
                    for (int l = 0; l < point.getLength(); l++) {
                        Node coordinatePoint = point.item(l);
                        if (coordinatePoint.getNodeName().equalsIgnoreCase(
                                "coordinates")
                                && coordinatePoint.getFirstChild() != null) {
                            String[] coordinatesString = coordinatePoint
                                    .getFirstChild().getNodeValue().split(",");
                            LatLng coordinatesLatLng = new LatLng(
                                    Double.parseDouble(coordinatesString[1]),
                                    Double.parseDouble(coordinatesString[0]));
                            place.setCoordinates(coordinatesLatLng);
                        }
                    }
                }
            }
            places.add(place);
        }
        return places;
    }

    public static int loadMapOfIcons(String key) {
        int keyInteger = Integer.parseInt(key);
        switch (keyInteger) {
            case 503:
                return R.drawable.icone503;
            case 971:
                return R.drawable.icone971;
            case 985:
                return R.drawable.icone985;
            case 1001:
                return R.drawable.icone1001;
            case 1037:
                return R.drawable.icone1037;
            case 1085:
                return R.drawable.icone1085;
            case 1127:
                return R.drawable.icone1127;
            case 1157:
                return R.drawable.icone1157;
            case 1197:
                return R.drawable.icone1197;
            case 1201:
                return R.drawable.icone1201;
            case 1353:
                return R.drawable.icone1353;
            case 1379:
                return R.drawable.icone1379;
            case 1389:
                return R.drawable.icone1389;
            case 1395:
                return R.drawable.icone1395;
            case 1423:
                return R.drawable.icone1423;
            case 1107:
                return R.drawable.icone1107;
            case 1035:
                return R.drawable.icone1035;
            case 1205:
                return R.drawable.icone1205;
            case 1223:
                return R.drawable.icone1223;
            case 1469:
                return R.drawable.icone1469;
        }
        return R.drawable.icone971;
    }
}