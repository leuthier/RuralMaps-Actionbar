package negocio;

import com.mpoo.ruralmaps.ruralmaps.Usuario;

public class SessaoUsuario {
    private Usuario usuarioLogado;
    private static final SessaoUsuario sessao = new SessaoUsuario();

    public static SessaoUsuario getSessao(){
        return sessao;
    }

    public void setUsuarioLogado(Usuario usuario){
        this.usuarioLogado = usuario;
    }

}
