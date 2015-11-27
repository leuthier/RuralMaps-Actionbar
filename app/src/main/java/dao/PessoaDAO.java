package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mpoo.ruralmaps.ruralmaps.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public PessoaDAO(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDatabase(){
        if (database == null){
            database = databaseHelper.getWritableDatabase();
        }
        return  database;
    }

    private Pessoa criarPessoa(Cursor cursor){
        Pessoa negocio = new Pessoa(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Pessoas._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Pessoas.NOME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Pessoas.EMAIL))
        );
        return negocio;
    }

    public List<Pessoa> listarTarefa(){
        Cursor cursor = getDatabase().query(DatabaseHelper.Pessoas.TABELA,
                DatabaseHelper.Pessoas.COLUNAS, null, null, null, null, null);

        List<Pessoa> pessoa = new ArrayList<Pessoa>();
        while (cursor.moveToNext()){
            Pessoa negocio = criarPessoa(cursor);
            pessoa.add(negocio);
        }
        cursor.close();
        return pessoa;
    }


    public long salvarPessoa(Pessoa negocio){
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelper.Pessoas.NOME, negocio.getNome());
        valores.put(DatabaseHelper.Pessoas.EMAIL, negocio.getEmail());

        if (negocio.get_id() != null){
            return getDatabase().update(DatabaseHelper.Pessoas.TABELA, valores, "_id = ?",
                    new String[]{negocio.get_id().toString()});
        }
        return getDatabase().insert(DatabaseHelper.Pessoas.TABELA, null, valores);
    }

    public boolean removerPessoa(int id){
        return getDatabase().delete(DatabaseHelper.Pessoas.TABELA,"_id = ?",
                new String[]{Integer.toString(id)}) > 0;
    }

    public Pessoa buscarPessoaPorId(int id){
        Cursor cursor = getDatabase().query(DatabaseHelper.Pessoas.TABELA,
                DatabaseHelper.Pessoas.COLUNAS,"_id = ?", new String[]{Integer.toString(id)},null, null, null);

        if (cursor.moveToNext()){
            Pessoa negocio = criarPessoa(cursor);
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
