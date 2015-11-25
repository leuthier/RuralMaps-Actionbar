package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mpoo.ruralmaps.ruralmaps.Placemark;

import java.util.ArrayList;
import java.util.List;

public class PlacemarkDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    //private static PlacemarkDAO instancia = new PlacemarkDAO();
    //public PlacemarkDAO(){
    //}

    //public static PlacemarkDAO getInstancia(){
    //    return instancia;
    //}

    public PlacemarkDAO(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDatabase(){
        if (database == null){
            database = databaseHelper.getWritableDatabase();
        }
        return  database;
    }

    private Placemark criarPlacemark(Cursor cursor){
        Integer _id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Placemarks._ID));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Placemarks.NAME));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Placemarks.DESCRIPTION));
        String iconID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Placemarks.ICONID));
        double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Placemarks.LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Placemarks.LONGITUDE));
        Placemark negocio = new Placemark(_id,name, description, iconID, latitude, longitude);
        return negocio;
    }

    public List<Placemark> listarPlacemarks(){
        List<Placemark> placemarks = new ArrayList<Placemark>();

        Cursor cursor = getDatabase().query(DatabaseHelper.Placemarks.TABELA,
                DatabaseHelper.Placemarks.COLUNAS, null, null, null, null, null);

        while (cursor.moveToNext()){
            Placemark negocio = criarPlacemark(cursor);
            placemarks.add(negocio);
        }

        cursor.close();
        return placemarks;
    }



    public long salvarPlacemarkDAO(Placemark placemark){
        database = databaseHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelper.Placemarks.NAME, placemark.getName());
        valores.put(DatabaseHelper.Placemarks.DESCRIPTION, placemark.getDescription());
        valores.put(DatabaseHelper.Placemarks.ICONID, placemark.getIconID());
        valores.put(DatabaseHelper.Placemarks.LATITUDE, placemark.getCoordinates().latitude);
        valores.put(DatabaseHelper.Placemarks.LONGITUDE, placemark.getCoordinates().longitude);

        if (placemark.getId() != null){
            return getDatabase().update(DatabaseHelper.Placemarks.TABELA, valores, "_id = ?",
                    new String[]{placemark.getId().toString()});
        }
        return getDatabase().insert(DatabaseHelper.Placemarks.TABELA, null, valores);
    }

    public boolean removerPlacemark(int id){
        return getDatabase().delete(DatabaseHelper.Placemarks.TABELA,
                "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }

    public Placemark buscarPlacemarkPorName(String name){
        Placemark placemarks = new Placemark();
        Log.i("script", "placemark pequisado " + name);
        Cursor cursor = getDatabase().query(DatabaseHelper.Placemarks.TABELA,
                DatabaseHelper.Placemarks.COLUNAS, "name = ?", new String[]{name}, null, null, null);
        //se encontou
        if (cursor.getCount() > 0) {
            Placemark p = new Placemark();
            //posiciona o primeiro resultado
            cursor.moveToFirst();
            p.setName(cursor.getString(1));
            Log.i("script", "testando cursor========= " + p);

        } return placemarks;
    }
    public Placemark buscarPlacemarkPorDescription(String description){
        Cursor cursor = getDatabase().query(DatabaseHelper.Placemarks.TABELA,
                DatabaseHelper.Placemarks.COLUNAS,"description = ?",new String[]{description}, null, null, null);

        if (cursor.moveToNext()){
            Placemark negocio = criarPlacemark(cursor);
            cursor.close();
            return negocio;
        }
        return null;
    }

    public void fechar(){
        databaseHelper.close();
        database = null;
    }

}