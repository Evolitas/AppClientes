package mx.com.taximex.taximex;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button registro,ingresar;
    private EditText correo,contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo=(EditText) findViewById(R.id.etCorreo);
        contra=(EditText) findViewById(R.id.etContra);
        ingresar=(Button) findViewById(R.id.cmdIngresar);
        registro=(Button) findViewById(R.id.cmdRegistro);
        //registro.setOnClickListener(this);
        ingresar.setOnClickListener(this);
        registro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {
        switch (v.getId()) {
            case R.id.cmdIngresar: // Clic en el botón de ingresar
                new Thread((new Runnable() {
                    @Override
                    public void run() {

                        final String resultado=EnviarDatosGet(correo.getText().toString(),contra.getText().toString());
                        final int r=obtDatosJSON(resultado);
                        if(r>0){
                            Intent i =new Intent(getApplicationContext(),MapsActivity.class);
                            i.putExtra("correo", correo.getText().toString());
                            startActivity(i);
                        }else{
                            Log.i("Error "+r,"Datos incorrectos");
                        }
                    }
                })).start();
                return;

            case R.id.cmdRegistro:
                Intent i=new Intent(getApplicationContext(),Registro.class);
                startActivity(i);
                return;
        }

    }

    public String EnviarDatosGet  (String correo, String contra){
        URL url=null;
        String linea;
        int respuesta=0;
        StringBuilder resul=null;

        try {
            url=new URL("http://189.254.25.10:8080/wsTaximex/valida.php?correo="+correo+"&contra="+contra);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            respuesta=connection.getResponseCode();

            resul = new StringBuilder();

            if (respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){

                    resul.append(linea);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resul.toString();
    }

    public int obtDatosJSON(String response){
        int res=0;
        try {
            JSONArray json=new JSONArray(response);
            if(json.length()>=1){
                res=1;
            }
          }
        catch (Exception ex){
            Log.e("Error JASON",ex.getMessage().toString());
        }

        return res;
    }

}
