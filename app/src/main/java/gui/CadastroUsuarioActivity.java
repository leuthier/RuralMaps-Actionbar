package gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mpoo.ruralmaps.ruralmaps.R;

import dao.UsuarioDAO;
import negocio.UsuarioNegocio;

import com.mpoo.ruralmaps.ruralmaps.Usuario;

public class CadastroUsuarioActivity extends Activity implements View.OnClickListener {

    private EditText edtNome, edtLogin, edtSenha, edtEmail;
    private UsuarioDAO usuarioDAO;
    private Usuario usuario;
    private int idUsuario;
    private Resources resources;
    private String nome;
    private String login;
    private String senha;
    private String email;
    private UsuarioNegocio userNegocio = UsuarioNegocio.getInstancia();
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        initViews();

        usuarioDAO = new UsuarioDAO(this);
        context = this;
        edtNome = (EditText) findViewById(R.id.cadastro_edt_Nome);
        edtLogin = (EditText) findViewById(R.id.cadastro_edt_Login);
        edtSenha = (EditText) findViewById(R.id.cadastro_edt_Senha);
        edtEmail = (EditText) findViewById(R.id.cadastro_edt_email);

        Button bntCadastrar = (Button) findViewById(R.id.cadastro_btn_cadastrar);
        bntCadastrar.setOnClickListener(this);

    }



    private boolean validarCamposCadastro() {
        boolean validacao = false;
        resources = getResources();

        nome = edtNome.getText().toString().trim();
        login = edtLogin.getText().toString().trim();
        senha = edtSenha.getText().toString();
        email = edtEmail.getText().toString().trim();

        if (validateFields()) {
            validacao = true;
        }
        return validacao;
    }

    private void cadastrar() {
        if (validarCamposCadastro()) {
            usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setLogin(login);
            usuario.setSenha(senha);
            usuario.setEmail(email);
            if (UsuarioNegocio.validarDadosCadastro(usuario, usuarioDAO)) {
                if (UsuarioNegocio.validarEmail(usuario, usuarioDAO)) {
                    long resultado = usuarioDAO.salvarUsuario(usuario);

                    if (resultado == -1) {
                        GuiUtil.Msg(this, getString(R.string.cadastro_erro));
                    } else {
                        GuiUtil.Msg(this, getString(R.string.cadastro_concluido));
                        chamarLoginActivity();
                    }
                } else {
                    GuiUtil.Msg(this, getString(R.string.cadastro_email_invalido));
                }
            } else {
                GuiUtil.Msg(this, getString(R.string.cadastro_login_invalido));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);

        if (idUsuario > 0) {
            menu.findItem(R.id.action_menu_salvar).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_menu_salvar:
                this.cadastrar();
                break;
            case R.id.action_menu_voltar:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chamarLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initViews() {
        resources = getResources();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                callClearErrors(s);
            }
        };

        edtNome = (EditText) findViewById(R.id.cadastro_edt_Nome);
        edtNome.addTextChangedListener(textWatcher);

        edtLogin = (EditText) findViewById(R.id.cadastro_edt_Login);
        edtLogin.addTextChangedListener(textWatcher);

        edtEmail = (EditText) findViewById(R.id.cadastro_edt_email);
        edtEmail.addTextChangedListener(textWatcher);

        edtSenha = (EditText) findViewById(R.id.cadastro_edt_Senha);
        edtSenha.addTextChangedListener(textWatcher);
    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(edtLogin);
        }
    }

    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }
    private boolean validateFields() {
        String user = edtLogin.getText().toString().trim();
        String pass = edtSenha.getText().toString().trim();
        String name = edtNome.getText().toString().trim();
        String mail = edtEmail.getText().toString().trim();
        return (!isEmptyFields(user, pass, name, mail) && hasSizeValid(user, pass) && !noHasSpaceLogin(user));
    }

    private boolean isEmptyFields(String user, String pass, String nome, String email) {
        if (TextUtils.isEmpty(nome)){
            edtNome.requestFocus();
            edtNome.setError(resources.getString(R.string.nome_required));
            return true;
        } else if (TextUtils.isEmpty(user)) {
            edtLogin.requestFocus();
            edtLogin.setError(resources.getString(R.string.login_user_required));
            return true;
        } else if (TextUtils.isEmpty(email)){
            edtEmail.requestFocus();
            edtEmail.setError(resources.getString(R.string.email_required));
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            edtSenha.requestFocus();
            edtSenha.setError(resources.getString(R.string.login_password_required));
            return true;
        }
        return false;
    }

    private boolean hasSizeValid(String user, String pass) {

        if (!(user.length() > 3)) {
            edtLogin.requestFocus();
            edtLogin.setError(resources.getString(R.string.login_user_size_invalid));
            return false;
        } else if (!(pass.length() > 5)) {
            edtSenha.requestFocus();
            edtSenha.setError(resources.getString(R.string.login_pass_size_invalid));
            return false;
        }
        return true;
    }

    private boolean noHasSpaceLogin(String user) {
        int idx = user.indexOf(" ");
        if (idx != -1){
            edtLogin.requestFocus();
            edtLogin.setError(resources.getString(R.string.login_user_has_space));
            return true;
        }return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cadastro_btn_cadastrar:
                this.cadastrar();
                break;
        }

    }
}
