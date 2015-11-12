package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mpoo.ruralmaps.ruralmaps.Usuario;

import java.util.ArrayList;
import java.util.List;

import gui.CadastroUsuarioActivity;

public class UsuarioDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static UsuarioDAO instancia = new UsuarioDAO();
    private UsuarioDAO(){
    }

    public static UsuarioDAO getInstancia(){
        return instancia;
    }

    public UsuarioDAO(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDatabase(){
        if (database == null){
            database = databaseHelper.getWritableDatabase();
        }
        return  database;
    }

    private Usuario criarUsuario(Cursor cursor){
        Usuario negocio = new Usuario(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Usuarios._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Usuarios.NOME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Usuarios.LOGIN)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Usuarios.SENHA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Usuarios.EMAIL))
        );
        return negocio;
    }

    public List<Usuario> listarUsuarios(){
        Cursor cursor = getDatabase().query(DatabaseHelper.Usuarios.TABELA,
                DatabaseHelper.Usuarios.COLUNAS, null, null, null, null, null);

        List<Usuario> usuarios = new ArrayList<Usuario>();
        while (cursor.moveToNext()){
            Usuario negocio = criarUsuario(cursor);
            usuarios.add(negocio);
        }
        cursor.close();
        return usuarios;
    }

    public long salvarUsuario(Usuario usuario){
        database = databaseHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelper.Usuarios.NOME, usuario.getNome());
        valores.put(DatabaseHelper.Usuarios.LOGIN, usuario.getLogin());
        valores.put(DatabaseHelper.Usuarios.SENHA, usuario.getSenha());
        valores.put(DatabaseHelper.Usuarios.EMAIL, usuario.getEmail());

        if (usuario.get_id() != null){
            return getDatabase().update(DatabaseHelper.Usuarios.TABELA, valores, "_id = ?",
                    new String[]{usuario.get_id().toString()});
        }
        return getDatabase().insert(DatabaseHelper.Usuarios.TABELA, null, valores);
    }

    public boolean removerUsario(int id){
        return getDatabase().delete(DatabaseHelper.Usuarios.TABELA,
                "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }

    public Usuario buscarUsuarioPorLogin(String login){
        Cursor cursor = getDatabase().query(DatabaseHelper.Usuarios.TABELA,
                DatabaseHelper.Usuarios.COLUNAS,"login = ?",new String[]{login}, null, null, null);
        if (cursor.moveToNext()){
            Usuario negocio = criarUsuario(cursor);
            cursor.close();
            return negocio;
        }
        return null;
    }
    public Usuario buscarUsuarioPorEmail(String email){
        Cursor cursor = getDatabase().query(DatabaseHelper.Usuarios.TABELA,
                DatabaseHelper.Usuarios.COLUNAS,"EMAIL = ?",new String[]{email}, null, null, null);

        if (cursor.moveToNext()){
            Usuario negocio = criarUsuario(cursor);
            cursor.close();
            return negocio;
        }
        return null;
    }

    public boolean logar (String usuario, String senha){
        Cursor cursor = getDatabase().query(DatabaseHelper.Usuarios.TABELA,
                null, "LOGIN = ? AND SENHA = ?", new String[]{usuario, senha}, null, null, null);
        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    public void fechar(){
        databaseHelper.close();
        database = null;
    }

}
