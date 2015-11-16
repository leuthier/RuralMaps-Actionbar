package negocio;

import com.google.android.gms.maps.model.MarkerOptions;
import com.mpoo.ruralmaps.ruralmaps.Placemark;
import com.mpoo.ruralmaps.ruralmaps.Usuario;

import java.util.HashMap;
import java.util.Map;

public class SessaoUsuario {
    private Usuario usuarioLogado;

    private static final SessaoUsuario sessao = new SessaoUsuario();

    private Map<Placemark,MarkerOptions> placemarks = new HashMap<Placemark,MarkerOptions>();

    public static SessaoUsuario getSessao(){
        return sessao;
    }

    public void setUsuarioLogado(Usuario usuario){
        this.usuarioLogado = usuario;
    }

    public void putPlaceMark(Placemark pm, MarkerOptions mo) {
        placemarks.put(pm,mo);


        placemarks.get(pm);
    }
}
