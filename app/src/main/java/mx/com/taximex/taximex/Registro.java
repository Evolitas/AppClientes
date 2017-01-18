package mx.com.taximex.taximex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends AppCompatActivity implements View.OnClickListener {
    EditText nombre,correo,celular,contra,repcontra;
    TextView msg;
    Button registro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre=(EditText) findViewById(R.id.etNombre);
        correo=(EditText)findViewById(R.id.etCorreo);
        celular=(EditText)findViewById(R.id.etCelular);
        contra=(EditText)findViewById(R.id.etContra);
        repcontra=(EditText) findViewById(R.id.etRepContra);
        msg=(TextView) findViewById(R.id.tvMsg);
        registro=(Button) findViewById(R.id.cmdRegistro);

        registro.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        try
        {
            if (contra.getText().toString().compareTo(repcontra.getText().toString()) != 0){

                msg.setText("Las contraseñas no coinciden");
                throw new Exception("Las contraseñas no coinciden");
            }else{
                msg.setText("");
            }
        }
        catch (Exception e){
            Log.e("Contraseña incorrecta","Las contraseñas no coinciden");
        }

    }
}
