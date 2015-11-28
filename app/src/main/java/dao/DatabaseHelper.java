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
                + "login text not null, senha text not null)");

        //tabela de Pessoas
        db.execSQL("create table pessoa(_id integer primary key autoincrement,"
                + "nome text not null, email text not null, pts_pesquisados)");

        //tabela placemark
        db.execSQL("create table placemark(_id integer primary key autoincrement,"
                +"name text not null, description text, iconid text not null, "
                + "latitude real, longitude real)");

        //cadastrar usuario Admin
        db.execSQL("insert into usuario(login, senha) values('admin', '123456')");
        db.execSQL("insert into pessoa(nome, email) values('Admin', 'admin@gmail.com')");

        //para o birt
        db.execSQL("insert into usuario(login, senha) values('victor', '190496')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Victor', 'victor@gmail.com', 'Biblioteca Central')");

        db.execSQL("insert into usuario(login, senha) values('bruna', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Bruna', 'bruna@gmail.com', 'Biblioteca Central')");

        db.execSQL("insert into usuario(login, senha) values('gabriel', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Gabriel', 'java@gmail.com', 'CEGOE')");

        db.execSQL("insert into usuario(login, senha) values('airton', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Airton', 'airton@gmail.com', 'CEGOE')");

        db.execSQL("insert into usuario(login, senha) values('allan', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Allan', 'allan@gmail.com', 'CEGOE')");

        db.execSQL("insert into usuario(login, senha) values('stef', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Stefany', 'stefany@gmail.com', 'CEGOE')");

        db.execSQL("insert into usuario(login, senha) values('ronaldo', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Fenomeno', 'vaicorinthians@gmail.com', 'CEAGRI 2')");

        db.execSQL("insert into usuario(login, senha) values('andre', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Andre', 'andre@gmail.com', 'CEAGRI 2')");

        db.execSQL("insert into usuario(login, senha) values('catia', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Catia', 'catia@gmail.com', 'CEAGRI 2')");

        db.execSQL("insert into usuario(login, senha) values('patricia', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Patricia', 'patricia@gmail.com', 'CEAGRI 2')");

        db.execSQL("insert into usuario(login, senha) values('renata', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Renata', 'renata@gmail.com', 'DEINFO')");

        db.execSQL("insert into usuario(login, senha) values('cicero', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Cicero', 'cicero@gmail.com', 'DEINFO')");

        db.execSQL("insert into usuario(login, senha) values('silvana', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Silvana', 'silvana@gmail.com', 'DEINFO')");

        db.execSQL("insert into usuario(login, senha) values('gabriela', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Gabriela', 'gabihtinha@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('larissa', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Larissa', 'larissa@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('rodolfo', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Rodolfo', 'rodolfo@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('maria', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Maria', 'maria@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('beatriz', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Beatriz', 'biazinha@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('gleydson', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Gleydson', 'gleydson@gmail.com', 'CEAGRI 1')");

        db.execSQL("insert into usuario(login, senha) values('gustavo', '123456')");
        db.execSQL("insert into pessoa(nome, email, pts_pesquisados) values('Gustavo', 'gustpk@gmail.com', 'CEAGRI 1')");


        //cadastrar placemark
        db.execSQL("insert into placemark(name, description, iconid, latitude, longitude)"
                + "values('DCE', ' ', '985', -34.9490359, -8.0136736)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Usuarios.TABELA);
        db.execSQL("drop table if exists "+ Pessoas.TABELA);
        db.execSQL("drop table if exists"+ Placemarks.TABELA);
        onCreate(db);

    }

    public static class Usuarios {
        public static final String TABELA = "usuario";
        public static final String _ID = "_id";
        public static final String LOGIN = "login";
        public static final String SENHA = "senha";

        public static final String[] COLUNAS = new String[]{
                _ID, LOGIN, SENHA
        };
    }

    public static class Pessoas {
        public static final String TABELA = "pessoa";
        public static final String _ID = "_id";
        public static final String NOME = "nome";
        public static final String EMAIL = "email";
        public static final String PTS_PESQUISADOS = "pts_pesquisados";

        public static final String[] COLUNAS = new String[]{
                _ID, NOME, EMAIL, PTS_PESQUISADOS
        };
    }

    public static class Placemarks {
        public static final String TABELA = "placemark";
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String ICONID = "iconid";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";

        public static final String[] COLUNAS = new String[]{
                NAME, DESCRIPTION, ICONID, LATITUDE, LONGITUDE
        };
    }
}