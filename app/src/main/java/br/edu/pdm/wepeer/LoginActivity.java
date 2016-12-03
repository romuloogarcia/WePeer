package br.edu.pdm.wepeer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtLogin;
    public EditText edtSenha;
    ImageButton btnLogin;
    ImageButton btnSair;
    String strLogin;
    String strSenha;
    boolean logged = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // recuperando instâncias do XML(findViewById)
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnSair = (ImageButton) findViewById(R.id.btnSair);

        btnLogin.setOnClickListener(this);
        btnSair.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                // recuperar valores da tela
                strLogin = edtLogin.getText().toString();
                strSenha = edtSenha.getText().toString();
                if ((strLogin.length() != 0 && !strLogin.equals("")) && (strSenha.length() != 0 && !strSenha.equals(""))) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://ipeer.com.br/session/loginMobile/" + strLogin + "/" + strSenha);

                                HttpURLConnection urlC = (HttpURLConnection) url.openConnection();
                                urlC.setDoInput(true);
                                urlC.setReadTimeout(200000);
                                urlC.connect();

                                InputStream in = new BufferedInputStream(urlC.getInputStream());

                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                String var = br.readLine();
                                do {
                                    Log.d("px", var);
                                    if (var.contains("1")) {
                                        logged = true;
                                    }
                                } while ((var = br.readLine()) != null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (logged) {
                                Intent it = new Intent(LoginActivity.this, PrincipalActivity.class);
                                startActivity(it);
                            } else {
                                edtLogin.setText("");
                                edtSenha.setText("");
                                Toast.makeText(LoginActivity.this, R.string.msgLoginSenha, Toast.LENGTH_LONG).show();
                                edtLogin.requestFocus();
                            }
                        }
                    }).start();
                } else {
                    edtLogin.setText("");
                    edtSenha.setText("");
                    Toast.makeText(this, R.string.msgLoginSenha, Toast.LENGTH_LONG).show();
                    edtLogin.requestFocus();
                }
                break;
            case R.id.btnSair:
                sair();
                break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void sair() {
        // finalizar a atividade
        AlertDialog.Builder dialogoSair = new AlertDialog.Builder(this);
        dialogoSair.setTitle(R.string.dlgSair);
        dialogoSair.setMessage(R.string.msgSair);
        dialogoSair.setNegativeButton(R.string.opNao, null);
        dialogoSair.setPositiveButton(R.string.opSim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });
        dialogoSair.show();
    }
}


