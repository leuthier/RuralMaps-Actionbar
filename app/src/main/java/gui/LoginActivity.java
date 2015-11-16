package gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mpoo.ruralmaps.ruralmaps.R;
import com.mpoo.ruralmaps.ruralmaps.Usuario;

import dao.UsuarioDAO;
import negocio.UsuarioNegocio;

public class LoginActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {

    private EditText edtUser;
    private EditText edtPassword;
    private Resources resources;
    public UsuarioDAO helper;
    private CheckBox checkbox_conectado;
    private UsuarioNegocio userNegocio = UsuarioNegocio.getInstancia();
    private static final String MANTER_CONECTADO = "manter_conectado";
    private static final String PREFERENCE_NAME = "LoginActivityPreferences";
    private static final int SIGN_IN_CODE = 1206;

    private GoogleApiClient googleApiClient;
    private ConnectionResult connectionResult;

    private boolean isConsentScreenOpened;
    private boolean isSignInButtonClicked;

    private static LoginActivity instancia;
    private SignInButton btSignInDefault;
    private TextView tvName;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        helper = new UsuarioDAO(this);

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        boolean conectado =  preferences.getBoolean(MANTER_CONECTADO, false);

        if(conectado){
            chamarMapsActivity();
        }
        
        Button btnEnter = (Button) findViewById(R.id.login_btn_enter);
        btnEnter.setOnClickListener(this);

        Button btnCadastrar = (Button) findViewById(R.id.login_btn_cadastro);
        btnCadastrar.setOnClickListener(this);

        btSignInDefault = (SignInButton) findViewById(R.id.btSignInDefault);
        btSignInDefault.setOnClickListener(this);

        Button btRevokeAcess = (Button) findViewById(R.id.btRevokeAcess);
        btRevokeAcess.setOnClickListener(this);

        checkbox_conectado = (CheckBox) findViewById(R.id.login_checkbox_conectado);

        tvName = (TextView) findViewById(R.id.cadastro_edt_Nome);
        tvEmail = (TextView) findViewById(R.id.cadastro_edt_email);
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

        edtUser = (EditText) findViewById(R.id.login_edt_user);
        edtUser.addTextChangedListener(textWatcher);

        edtPassword = (EditText) findViewById(R.id.login_edt_password);
        edtPassword.addTextChangedListener(textWatcher);
    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(edtUser);
        }
    }
    public void logar(View view) {
        String usuario = edtUser.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();

        if (helper.logar(usuario, senha)) {
            if(checkbox_conectado.isChecked()){
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MANTER_CONECTADO, true);
                editor.commit();
                }
                chamarMapsActivity();
        }else {
            GuiUtil.Msg(this, getString(R.string.login_auth_deny));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_btn_cadastro:
                startActivity(new Intent(this, CadastroUsuarioActivity.class));
                break;
            case R.id.login_btn_enter:
                if (validateFields()){
                    logar(v);
                }
                break;
            case R.id.btSignInDefault:
                if(!googleApiClient.isConnecting()){
                    if(conexaoInternetOk()){
                        isSignInButtonClicked = true;
                        resolveSignIn();
                    }else{
                        GuiUtil.Msg(this,"Sem conexão com a internet");
                    }
                }
                break;
            case R.id.btRevokeAcess:
                if(googleApiClient.isConnected()) {
                    revokeAccess();
                    GuiUtil.Msg(this, "Acesso revogado!");
                }else{
                    GuiUtil.Msg(this, "Conta Google+ deve estar conectada");
                }
                break;
        }
    }


    private boolean validateFields() {
        String user = edtUser.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        return (!isEmptyFields(user, pass) && hasSizeValid(user, pass) && !noHasSpaceLogin(user));
    }

    private boolean isEmptyFields(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            edtUser.requestFocus();
            edtUser.setError(resources.getString(R.string.login_user_required));
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            edtPassword.requestFocus();
            edtPassword.setError(resources.getString(R.string.login_password_required));
            return true;
        }
        return false;
    }

    private boolean hasSizeValid(String user, String pass) {

        if (!(user.length() > 3)) {
            edtUser.requestFocus();
            edtUser.setError(resources.getString(R.string.login_user_size_invalid));
            return false;
        } else if (!(pass.length() > 5)) {
            edtPassword.requestFocus();
            edtPassword.setError(resources.getString(R.string.login_pass_size_invalid));
            return false;
        }
        return true;
    }

    private boolean noHasSpaceLogin(String user) {
        int idx = user.indexOf(" ");
        if (idx != -1){
            edtUser.requestFocus();
            edtUser.setError(resources.getString(R.string.login_user_has_space));
            return true;
        }return false;
    }

    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }
    public void abrirCadastro() {
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
        finish();
    }

    private void chamarMapsActivity(){
        startActivity(new Intent(this, TelaSplashActivity.class));
        finish();
    }

    @Override
    public void onStart(){
        super.onStart();

        if(googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(googleApiClient!=null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (requestCode == SIGN_IN_CODE) {
            isConsentScreenOpened = false;

            if (resultCode != RESULT_OK) {
                isSignInButtonClicked = false;
            }
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    public void getDataProfile(){
        Person p = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        if(p != null){
            String name = p.getDisplayName();
            String email = Plus.AccountApi.getAccountName(googleApiClient);
            String idGPlus = p.getId();

//            tvName.setText(name);
//            tvEmail.setText(email);

            GuiUtil.Msg(this, "Bem vindo "+name+"!");
            GuiUtil.Msg(this, "Email: " + email);
            idGPlus = idGPlus.substring(0,14);
            GuiUtil.Msg(this, idGPlus);
            cadastrarUsuarioGoogle(name,email,idGPlus);
            chamarMapsActivity();

        }
        else{
            GuiUtil.Msg(this, "Dados não liberados");
        }
    }
    public void cadastrarUsuarioGoogle(String nome,String email, String idGPlus){
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setLogin(email);
        usuario.setSenha(idGPlus);
        usuario.setEmail(email);
        helper.salvarUsuario(usuario);
        helper.logar(usuario.getLogin(),usuario.getSenha());
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        isSignInButtonClicked = false;
        getDataProfile();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if(!result.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        if(!isConsentScreenOpened){
            connectionResult = result;
            if(isSignInButtonClicked){
                resolveSignIn();
            }
        }
    }

    public void resolveSignIn(){
        if(connectionResult != null && connectionResult.hasResolution()){
            try {
                isConsentScreenOpened = true;
                connectionResult.startResolutionForResult(this, SIGN_IN_CODE);
            }
            catch(IntentSender.SendIntentException e) {
                isConsentScreenOpened = false;
                googleApiClient.connect();
            }
        }
    }

    public void revokeAccess(){
        Plus.AccountApi.clearDefaultAccount(googleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient).setResultCallback
                (new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status result) {
                        finish();
                    }
                });
    }

    public static LoginActivity instancia(){
        if (instancia == null){
            instancia = new LoginActivity();
        }
        return instancia;
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    public boolean conexaoInternetOk() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
    public boolean signOutGoogle() {
//        if (googleApiClient == null) {
//            Log.i("SCRIPT", "===========GOOGLE API EH NULO===============================================");
//        } else {
//            Log.i("SCRIPT", "====OBJETO GOOGLE API NAO NULO!====================================================");
            if (googleApiClient.isConnected()) {
                Log.i("SCRIPT", "=======CONECTADO!================================================");
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
                return true;
            }return false;
        }
//    }
}