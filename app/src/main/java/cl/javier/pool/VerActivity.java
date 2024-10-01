package cl.javier.pool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import cl.javier.pool.adaptadores.ListaMesasAdapter;
import cl.javier.pool.db.DbMesas;
import cl.javier.pool.entidades.Mesas;

public class VerActivity extends AppCompatActivity {
    private ArrayList<Mesas> listaArrayMesas;


    EditText etnro, ethora, etprecio, etextra;
    TextView tvtotal;
    Button btneditar, btneliminar;
    Mesas mesa;
    int nro = 0;
    boolean correcto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.WHITE);


        etnro = findViewById(R.id.etnro);
        ethora = findViewById(R.id.ethora);
        etprecio = findViewById(R.id.etprecio);
        etextra = findViewById(R.id.etextra);
        tvtotal = findViewById(R.id.tvtotal);
        btneditar = findViewById(R.id.btneditar);


        ethora.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);



        btneliminar = findViewById(R.id.btneliminar);

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarEliminar();

            }
        });

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                nro = Integer.parseInt(null);
            }else{
                nro = extras.getInt("nro");
            }
        }else{
            nro = (int) savedInstanceState.getSerializable("nro");
        }

        DbMesas dbMesas = new DbMesas(VerActivity.this);
        mesa = dbMesas.verMesas(nro);

        if (mesa != null){
            etnro.setText(String.valueOf(mesa.getNro()));
            ethora.setText(mesa.getHora_inicio());
            etprecio.setText(String.valueOf(mesa.getPrecio()));
            etextra.setText(String.valueOf(mesa.getExtras()));
            int minutos = (int) (TimeUtils.calcularDiferenciaEnMinutos(mesa.getHora_inicio()));
            int prhora = mesa.getPrecio()*minutos;
            if (minutos >= 55){
                tvtotal.setText( "Minutos: (" + minutos + ") " + (prhora) + "$" + " + Extras = " + (prhora + mesa.getExtras()));
            }else{
                tvtotal.setText( "Minutos: (" + minutos + ") " + " + Extras : " + ( mesa.getExtras()));
            }


        }


        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etnro.getText().toString().equals("") && !(ethora.getText().toString()).equals("") && !etprecio.getText().toString().equals("") && !etextra.getText().toString().equals("")){

                    boolean correct = true;
                    try {

                        String hora = ethora.getText().toString();
                        LocalTime hl = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
                        correcto = dbMesas.editarMesa((Integer.parseInt(etnro.getText().toString())),(ethora.getText().toString()),(Integer.parseInt(etprecio.getText().toString())),(Integer.parseInt(etextra.getText().toString())));
                        mesa = dbMesas.verMesas(Integer.parseInt(etnro.getText().toString()));
                    }catch (Exception ex){
                        correct = false;
                        String texto = ex.toString();
                        Snackbar.make(findViewById(android.R.id.content), texto, Snackbar.LENGTH_SHORT).show();
                    }



                    if (correct && correcto){

                        int minutos = (int) (TimeUtils.calcularDiferenciaEnMinutos(mesa.getHora_inicio()));
                        int prhora = mesa.getPrecio()*minutos;
                        if (minutos >= 55){
                            tvtotal.setText( "Minutos: (" + minutos + ") " + (prhora) + "$" + " + Extras = " + (prhora + mesa.getExtras()));
                        }else{
                            tvtotal.setText( "Minutos: (" + minutos + ") " + " + Extras : " + ( mesa.getExtras()));
                        }
                        Snackbar.make(findViewById(android.R.id.content), "Actualizado", Snackbar.LENGTH_SHORT).show();


                    }else{
                        Snackbar.make(findViewById(android.R.id.content), "Error al modificar", Snackbar.LENGTH_SHORT).show();

                    }
                }else{
                    Snackbar.make(findViewById(android.R.id.content), "Indica los datos", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void confirmarEliminar() {
        DbMesas dbMesas = new DbMesas(VerActivity.this);
        // Crear el TextView
        TextView textView = new TextView(this);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        // Configuración del TextView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(50, 0, 50, 0); // Márgenes
        textView.setLayoutParams(params);
        textView.setText("¿Seguro que desea Eliminar la Mesa?");
        textView.setTextSize(24);
        textView.setTextColor(Color.WHITE);
        container.setBackgroundColor(Color.parseColor("#333333"));
        container.addView(textView);

        // Crear el AlertDialog
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setView(container);

        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean correcto = dbMesas.eliminarMesa(nro);

                if(correcto){
                    Snackbar.make(findViewById(android.R.id.content), "Eliminado", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(VerActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    }, 1000);

                }
            }
        });

        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Cancelado", Snackbar.LENGTH_SHORT).show();// Cerrar el diálogo sin hacer nada
            }
        });

        AlertDialog alertDialog = dialogo.create();
        alertDialog.show();

        // Cambiar estilo de los botones
        Button btnSi = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNo = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        // Establecer colores
        btnSi.setBackgroundColor(Color.parseColor("#4CAF50"));
        btnNo.setBackgroundColor(Color.parseColor("#F44336"));
        btnSi.setTextColor(Color.WHITE);
        btnNo.setTextColor(Color.WHITE);

        // Ajustar el tamaño del texto
        btnSi.setTextSize(36);
        btnNo.setTextSize(36);

        // Aplicar márgenes y padding
        LinearLayout.LayoutParams layoutParamsSi = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams layoutParamsNo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Establecer márgenes
        int margin = 10;
        layoutParamsSi.setMargins(margin, 0, margin, 0);
        layoutParamsNo.setMargins(margin, 0, margin, 0);

        // Aplicar layoutParams a los botones
        btnSi.setLayoutParams(layoutParamsSi);
        btnNo.setLayoutParams(layoutParamsNo);

        // Establecer padding
        int padding = 36;
        btnSi.setPadding(padding + 100, padding, padding + 100, padding);
        btnNo.setPadding(padding + 75, padding, padding +75, padding);
    }




    public static class TimeUtils {

        public static long calcularDiferenciaEnMinutos(String horaString) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime horaProporcionada = LocalTime.parse(horaString, formatter);

            LocalTime horaActual = LocalTime.now();

            long minutos;


            if (horaProporcionada.getHour() <= horaActual.getHour()) {
                Duration diferencia = Duration.between(horaActual,horaProporcionada);
                minutos = diferencia.toMinutes()*-1;

            } else {

                int horain = horaProporcionada.getHour();
                int horaact = horaActual.getHour() + 24;
                int minin = horaProporcionada.getMinute();
                int minact = horaActual.getMinute();
                if (minact - minin < 0){
                    int difhor = ((minact - minin)*1) + ((horain - horaact) * -60) ;
                    minutos = difhor;
                } else{
                    int difhor = (minact - minin) + ((horaact - horain) * 60) ;
                    minutos = difhor;
                }

            }

            return minutos;
        }
    }

}