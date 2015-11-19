package negocio;


import com.google.android.gms.maps.model.LatLng;
import com.mpoo.ruralmaps.ruralmaps.Placemark;

import dao.PlacemarkDAO;

public class PlacemarkNegocio {
    private PlacemarkDAO placemarkDAO = new PlacemarkDAO();

    public void salvarPlace(Placemark placemark){
        placemarkDAO.salvarPlacemark(placemark);
    }

    public LatLng buscarPlace(String ponto){
        LatLng coord;
        coord = placemarkDAO.bucarPlacemarkPorName(ponto);
        return coord;
    }
}
