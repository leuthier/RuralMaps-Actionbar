package negocio;


import com.mpoo.ruralmaps.ruralmaps.Placemark;

import dao.PlacemarkDAO;

public class PlacemarkNegocio {
    private PlacemarkDAO placemarkDAO = new PlacemarkDAO();

    public void salvarPlace(Placemark placemark){
        placemarkDAO.salvarPlacemark(placemark);
    }
}
