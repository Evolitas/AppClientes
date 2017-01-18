package mx.com.taximex.taximex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registro, ingresar;
    private EditText correo, contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.etCorreo);
        contra = (EditText) findViewById(R.id.etContra);
        ingresar = (Button) findViewById(R.id.cmdIngresar);
        registro = (Button) findViewById(R.id.cmdRegistro);
        //registro.setOnClickListener(this);
        ingresar.setOnClickListener(this);
        registro.setOnClickListener(this);

        if(!isNetworkAvailable(this)){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Usted no tiene conexión a Internet")
                    .setTitle("Atención!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            Toast toast=Toast.makeText(this,"No tiene conexión a internet", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast=Toast.makeText(this,"Usted está conectado a interrnet", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmdIngresar: // Clic en el botón de ingresar
                new Thread((new Runnable() {
                    @Override
                    public void run() {

                        final String resultado = EnviarDatosGet(correo.getText().toString(), contra.getText().toString());
                        final int r = obtDatosJSON(resultado);
                        if (r > 0) {
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            i.putExtra("correo", correo.getText().toString());
                            startActivity(i);
                        } else {
                            Log.i("Error " + r, "Datos incorrectos");
                        }
                    }
                })).start();
                return;

            case R.id.cmdRegistro:
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
                return;
        }

    }

    public String EnviarDatosGet(String correo, String contra) {
        URL url = null;
        String linea;
        int respuesta = 0;
        StringBuilder resul = null;

        try {
            url = new URL("http://189.254.25.10:8080/wsTaximex/valida.php?correo=" + correo + "&contra=" + contra);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();

            resul = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea = reader.readLine()) != null) {
                    resul.append(linea);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resul.toString();
    }

    public int obtDatosJSON(String response) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(response);
            if (json.length() >= 1) {
                res = 1;
            }
        } catch (Exception ex) {
            Log.e("Error JASON", ex.getMessage().toString());
        }

        return res;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufru, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(getApplicationContext(),Registro.class);
            startActivity(intent);
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
