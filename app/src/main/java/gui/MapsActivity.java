package gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpoo.ruralmaps.ruralmaps.Placemark;
import com.mpoo.ruralmaps.ruralmaps.R;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import dao.PlacemarkDAO;
import negocio.ParserKML;
import negocio.PlacemarkNegocio;


public class MapsActivity extends FragmentActivity implements View.OnClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String MANTER_CONECTADO = "manter_conectado";

    private PlacemarkDAO placemarkDAO = new PlacemarkDAO(this);
    private PlacemarkNegocio placemarkNegocio = new PlacemarkNegocio(this);

    private Button btRevokeAccessMaps;

    private int started = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        final Button testButton = (Button) findViewById(R.id.Btype);
        testButton.setTag(1);
        testButton.setText("Satelite");
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    changeType(v);
                    testButton.setText("Normal");
                    v.setTag(0);
                } else {
                    changeType(v);
                    testButton.setText("Satelite");
                    v.setTag(1);
                }
        //Informaçoes do transito em tempo real
        mMap.setTrafficEnabled(true);
        //mMap.addPolyline();
            }
        });

//        Button revogarAccess = (Button) findViewById(R.id.btRevokeAccessMaps);
//        revogarAccess.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.btRevokeAccessMaps:
//                instanciaLogin.revokeAccess();
//                if(instanciaLogin == null){
//                    Log.i("SCRIPT","instancia login NAO nula ====================================");
//                }else{
//                    Log.i("SCRIPT","instancia login NULA ========================================");
//                }
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (started == 0){
            registrarPonto();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();

            }
        }
    }

    private void setUpMap() {
        InputStream mapa = getResources().openRawResource(R.raw.ruralmaps);
        mMap.setMyLocationEnabled(true);
        try {

            //SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();
            List<Placemark> lista = ParserKML.getPlaces(mapa);
            for (Placemark place : lista) {
                MarkerOptions mo = new MarkerOptions();
                LatLng coord = place.getCoordinates();
                LatLng position = new LatLng(coord.latitude, coord.longitude);

                mo.title(place.getName());
                mo.snippet(place.getDescription());
                mo.icon(BitmapDescriptorFactory.fromResource(ParserKML
                        .loadMapOfIcons(place.getIconID())));
                mo.position(position);

                //sessaoUsuario.putPlaceMarks(place, mo);
                mMap.addMarker(mo);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-8.0144, -34.95061), 15));
    }

    public void registrarPonto(){
        InputStream mapa = getResources().openRawResource(R.raw.ruralmaps);
        List<Placemark> lista = null;
        try {
            lista = ParserKML.getPlaces(mapa);
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Placemark ponto : lista){
            Log.i("Script", "========================================== placemarks " + ponto);
            placemarkDAO.salvarPlacemarkDAO(ponto);
        }started = 1;
    }

    public void enviarPonto(Placemark place){
        placemarkNegocio.salvarPlace(place);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_mapa, menu);


        SearchView sv = (SearchView) menu.findItem(R.id.search_b).getActionView();
        sv.setOnQueryTextListener(new SearchFiltro());

        return true;
    }

    /*public boolean pesquisarPonto(String ponto){

        placemarkNegocio.buscarPlace(ponto);

        return true;
    }*/

    private LoginActivity instanciaLogin = new LoginActivity();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_fechar:
                finish();
                break;
            case R.id.action_logout:
                logout();
                if (instanciaLogin.getApiClient() != null) {
                    instanciaLogin.signOutFromGplus();
//                    instanciaLogin.revokeAccess();
                }
//                }finish();
                chamarLoginActivity();
                break;
        }
        return true;
    }

    private void chamarLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

//    private void revokeAccessMaps(){
//        instanciaLogin.revokeAccess();
//        chamarLoginActivity();
//        finish();
//    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("LoginActivityPreferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(MANTER_CONECTADO, false);
        editor.clear();
        editor.commit();
//        finish();
    }

    public void changeType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private class SearchFiltro implements SearchView.OnQueryTextListener{

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i("Script", "onQueryTextChange "+ newText);
            // a logica da busca analise de texto, neste
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.i("Script", "onQueryTextSubmit " + query);
            LatLng coord;
            //coord = placemarkNegocio.buscarPlace(query);
            Placemark place;
            place = placemarkDAO.buscarPlacemarkPorName(query);
            Log.i("script", "testando=============== ");
            coord = place.getCoordinates();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(coord.latitude, coord.longitude), 15));
            return false;
        }
    }
    //metodo que limpa o mapa para posteriormente por meio de outro método motrar a lista desejada de pontos
    public void clearMap(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-8.0144, -34.95061), 15));
        mMap.clear();
    }
}