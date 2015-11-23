package negocio;


import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.mpoo.ruralmaps.ruralmaps.Placemark;

import dao.DatabaseHelper;
import dao.PlacemarkDAO;


public class PlacemarkNegocio {
    //private PlacemarkDAO placemarkDAO = new PlacemarkDAO();
    private PlacemarkDAO placemarkDAO;
    private DatabaseHelper databaseHelper;



    public PlacemarkNegocio(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public void salvarPlace(Placemark placemark){
        placemarkDAO.salvarPlacemarkDAO(placemark);
    }

    public LatLng buscarPlace(String ponto){
        LatLng coord;
        coord = placemarkDAO.buscarPlacemarkPorName(ponto);
        return coord;
    }
}
