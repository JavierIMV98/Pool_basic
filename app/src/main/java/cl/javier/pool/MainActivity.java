package cl.javier.pool;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.javier.pool.adaptadores.ListaMesasAdapter;
import cl.javier.pool.db.DbHelper;
import cl.javier.pool.db.DbMesas;
import cl.javier.pool.entidades.Mesas;

public class MainActivity extends AppCompatActivity {
    private DbHelper dbase;
    private Button btnCr;
    private Button btnPer;
    private Spinner spmesa;
    private Spinner spprecio;
    private Integer numero;
    private Integer precio;
    private String hora;
    private Integer extra;
    private ArrayList<Mesas> listaArrayMesas;
    private RecyclerView listaMesas;
    private ImageView testt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.WHITE);

        listaMesas = findViewById(R.id.rv_lista_mesas);
        listaMesas.setLayoutManager(new LinearLayoutManager(this));

        DbMesas dbMesas = new DbMesas(MainActivity.this);

        listaArrayMesas = new ArrayList<>();

        listaArrayMesas = dbMesas.mostrarMesas();
        if (listaArrayMesas != null && !listaArrayMesas.isEmpty()) {
            ListaMesasAdapter adapter = new ListaMesasAdapter(listaArrayMesas);
            listaMesas.setAdapter(adapter);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "No hay mesas disponibles", Snackbar.LENGTH_SHORT).show();

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCr = findViewById(R.id.crearahora);
        btnPer= findViewById(R.id.crearcustom);
        spmesa = findViewById(R.id.spmesas);
        spprecio = findViewById(R.id.spPrecios);

        actualizarSpinnerMesas();

        btnCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch ((int) spprecio.getSelectedItemId()){
                    case 0:
                        precio = 75;
                        break;
                    case 1:
                        precio = 80;
                        break;
                    case 2:
                        precio = 85;
                        break;
                    case 3:
                        precio = 90;
                        break;
                    case 4:
                        precio = 92;
                        break;
                    case 5:
                        precio = 97;
                        break;
                    case 6:
                        precio = 100;
                        break;
                    case 7:
                        precio = 110;
                        break;
                    case 8:
                        precio = 113;
                        break;
                    case 9:
                        precio = 120;
                        break;
                    case 10:
                        precio = 125;
                        break;
                    case 11:
                        precio = 130;
                        break;
                    case 12:
                        precio = 135;
                        break;
                    default:
                        precio = 75;
                }
                extra = 0;


                hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                numero = Integer.parseInt(spmesa.getSelectedItem().toString());

                DbMesas dbMesas = new DbMesas(MainActivity.this);

                long id = dbMesas.insertarMesas(numero, hora, precio,extra);
                if(id >0){
                    Snackbar.make(findViewById(android.R.id.content), "REGISTRO GUARDADO", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(android.R.id.content), "ERROR REGISTRO", Snackbar.LENGTH_SHORT).show();
                }
                listaArrayMesas = new ArrayList<>();

                listaArrayMesas = dbMesas.mostrarMesas();
                actualizarSpinnerMesas();
                if (listaArrayMesas != null && !listaArrayMesas.isEmpty()) {
                    ListaMesasAdapter adapter = new ListaMesasAdapter(listaArrayMesas);
                    listaMesas.setAdapter(adapter);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No hay mesas disponibles", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        btnPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoTexto(new OnTextEnteredListener() {
                    @Override
                    public void onTextEntered(String horaIngresada) {
                        boolean correcto = true;
                        try {

                            hora = horaIngresada;
                            LocalTime hl = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
                        }catch (Exception ex){
                            correcto = false;
                            String texto = ex.toString();
                            Snackbar.make(findViewById(android.R.id.content), texto, Snackbar.LENGTH_SHORT).show();
                        }




                        switch ((int) spprecio.getSelectedItemId()) {
                            case 0: precio = 75; break;
                            case 1: precio = 80; break;
                            case 2: precio = 85; break;
                            case 3: precio = 90; break;
                            case 4: precio = 92; break;
                            case 5: precio = 97; break;
                            case 6: precio = 100; break;
                            case 7: precio = 110; break;
                            case 8: precio = 113; break;
                            case 9: precio = 120; break;
                            case 10: precio = 125; break;
                            case 11: precio = 130; break;
                            case 12: precio = 135; break;
                            default: precio = 75;
                        }

                        extra = 0;
                        numero = Integer.parseInt(spmesa.getSelectedItem().toString());
                        DbMesas dbMesas = new DbMesas(MainActivity.this);

                        if (correcto){

                            long id = dbMesas.insertarMesas(numero, hora, precio, extra);
                            if (id > 0) {
                                Snackbar.make(findViewById(android.R.id.content), "REGISTRO GUARDADO", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "ERROR REGISTRO", Snackbar.LENGTH_SHORT).show();
                            }
                            actualizarSpinnerMesas();
                            listaArrayMesas = new ArrayList<>();
                            listaArrayMesas = dbMesas.mostrarMesas();
                            if (listaArrayMesas != null && !listaArrayMesas.isEmpty()) {
                                ListaMesasAdapter adapter = new ListaMesasAdapter(listaArrayMesas);
                                listaMesas.setAdapter(adapter);
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "No hay mesas disponibles", Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            Snackbar.make(findViewById(android.R.id.content), "HORA MAL INGRESADA", Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


    }
    private void actualizarSpinnerMesas() {
        DbMesas dbMesas = new DbMesas(MainActivity.this);
        List<Integer> mesasOcupadas = dbMesas.obtenerMesasOcupadas(); // Obtener números de mesas ocupadas

        List<Integer> mesasDisponibles = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (!mesasOcupadas.contains(i)) {
                mesasDisponibles.add(i);
            }
        }


        // Ordenar la lista (en caso de que no esté en orden)
        Collections.sort(mesasDisponibles);
        mesasDisponibles.add(0);

        // Crear el adaptador y asignarlo al Spinner
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mesasDisponibles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spmesa.setAdapter(adapter);
    }

    public void mostrarDialogoTexto(OnTextEnteredListener listener) {

        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);


        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margenIzqDer = 50;
        params.setMargins(margenIzqDer, 0, margenIzqDer, 0);
        editText.setLayoutParams(params);


        editText.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        editText.setTextSize(36);
        container.addView(editText);


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Agregar texto");
        dialogo.setMessage("Ingresa tu texto:");


        dialogo.setView(container);


        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textoIngresado = editText.getText().toString();
                listener.onTextEntered(textoIngresado); // Llamar al callback con el texto ingresado
            }
        });


        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = dialogo.create();
        alertDialog.show();


        Button btnAceptar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnCancelar = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);


        btnAceptar.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        btnCancelar.setBackgroundColor(Color.parseColor("#F44336")); // Rojo


        btnAceptar.setTextColor(Color.WHITE);
        btnCancelar.setTextColor(Color.WHITE);


        btnAceptar.setTextSize(18);
        btnCancelar.setTextSize(18);
    }
    public interface OnTextEnteredListener {
        void onTextEntered(String texto);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbMesas dbMesas = new DbMesas(MainActivity.this);
        listaArrayMesas = new ArrayList<>();

        listaArrayMesas = dbMesas.mostrarMesas();
        actualizarSpinnerMesas();
        if (listaArrayMesas != null && !listaArrayMesas.isEmpty()) {
            ListaMesasAdapter adapter = new ListaMesasAdapter(listaArrayMesas);
            listaMesas.setAdapter(adapter);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "No hay mesas disponibles", Snackbar.LENGTH_SHORT).show();
        }
    }
}