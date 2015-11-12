package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "tasks";
    private static final int VERSAO = 1;

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tabela de usuarios
        db.execSQL("create table usuario(_id integer primary key autoincrement, "
                + "nome text not null, login text not null, senha text not null, email text not null)");

        //tabela de Pessoas
        db.execSQL("create table pessoa(_id integer primary key autoincrement,"
                + "pessoa text not null, dt_criacao text , dt_completado text)");

        //cadastrar usuario Admin
        db.execSQL("insert into usuario(nome, login, senha, email) values('Admin','admin', '123456','admin@hotmail.com.br')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Usuarios.TABELA);
        db.execSQL("drop table if exists "+ Pessoas.TABELA);
        onCreate(db);

    }

    public static class Usuarios {
        public static final String TABELA = "usuario";
        public static final String _ID = "_id";
        public static final String NOME = "nome";
        public static final String LOGIN = "login";
        public static final String SENHA = "senha";
        public static final String EMAIL = "email";

        public static final String[] COLUNAS = new String[]{
                _ID, NOME, LOGIN, SENHA, EMAIL
        };
    }

    public static class Pessoas {
        public static final String TABELA = "pessoas";
        public static final String _ID = "_id";
        public static final String PESSOA = "pessoa";
        public static final String DT_CRIACAO = "dt_criacao";
        public static final String DT_COMPLETADO = "dt_completado";

        public static final String[] COLUNAS = new String[]{
                _ID, PESSOA, DT_CRIACAO, DT_COMPLETADO
        };
    }
}