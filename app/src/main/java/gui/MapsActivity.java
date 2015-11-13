package gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;
import com.mpoo.ruralmaps.ruralmaps.Placemark;
import com.mpoo.ruralmaps.ruralmaps.R;

import java.io.InputStream;
import java.util.ArrayList;

import negocio.ParserKML;
import negocio.SessaoUsuario;
import negocio.UsuarioNegocio;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String MANTER_CONECTADO = "manter_conectado";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        InputStream mapa = getResources().openRawResource(R.raw.ruralmaps);

        try {

            SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();
            ArrayList<Placemark> lista =  ParserKML.getPlaces(mapa);
            for (Placemark place : lista) {
                MarkerOptions mo = new MarkerOptions();
                LatLng position = new LatLng(place.getCoordinates().latitude, place.getCoordinates().longitude);

                mo.title(place.getName());
                mo.snippet(place.getDescription());
                mo.icon(BitmapDescriptorFactory.fromResource(ParserKML
                        .loadMapOfIcons(place.getIconID())));
                mo.position(position);

                sessaoUsuario.putPlaceMarks(place, mo);
                mMap.addMarker(mo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-8.0144, -34.95061), 15));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_fechar:
                finish();
                break;
            case R.id.action_logout:
//                if (LoginActivity.instancia().signOutGoogle()){
//                    chamarLoginActivity();
//                    finish();
//                    break;
//                }
                logout();
                finish();

                break;
        }
        return true;
    }

    private void chamarLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void logout(){
        Log.i("SCRIPT","=====ENTREI NO ELSE!================================================");
        SharedPreferences preferences = getSharedPreferences("LoginActivityPreferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(MANTER_CONECTADO, false);
        editor.clear();
        editor.commit();
        finish();
        chamarLoginActivity();
    }
}
