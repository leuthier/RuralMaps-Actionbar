package negocio;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mpoo.ruralmaps.ruralmaps.R;
import com.mpoo.ruralmaps.ruralmaps.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.UsuarioDAO;
import gui.LoginActivity;
import gui.GuiUtil;
import gui.CadastroUsuarioActivity;

public class UsuarioNegocio {
    private static UsuarioNegocio instancia = new UsuarioNegocio();
    private UsuarioNegocio(){

    }

    public UsuarioDAO usuarioDAO;
    private Usuario usuario;
    public static UsuarioNegocio getInstancia(){
        return instancia;
    }
    private UsuarioDAO usuarioDao = UsuarioDAO.getInstancia();


    public static boolean validarEmail(Usuario user, UsuarioDAO usuarioDao){
        String email = user.getEmail();
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                if (usuarioDao.buscarUsuarioPorEmail(email) != null){
                    return false;
                }else{
                    isEmailIdValid = true;
                }
            }
        }
        return isEmailIdValid;
    }

    public boolean logarUsuario(String login, String senha){
       boolean flag = usuarioDao.logar(login,senha);
        return flag;
    }
    public static boolean validarDadosCadastro(Usuario user, UsuarioDAO usuarioDao){
        String login = user.getLogin();
        if (usuarioDao.buscarUsuarioPorLogin(login) != null){
            return false;

        }return true;
    }

    public void cadastrarUsuarioGoogle(String nome,String email, String idGPlus){
        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setLogin(email);
        usuario.setSenha(idGPlus);
        usuario.setEmail(email);
        usuarioDAO.salvarUsuario(usuario);
        logarUsuario(usuario.getLogin(),usuario.getSenha());
    }
//    public static boolean googlePlusConnected(GoogleApiClient googleApiClient){
//        if(googleApiClient!=null && googleApiClient.isConnected()){
//            googleApiClient.disconnect();
//            return false;
//        }return true;
//    }

}

