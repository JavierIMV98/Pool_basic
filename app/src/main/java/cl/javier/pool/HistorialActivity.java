package cl.javier.pool;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cl.javier.pool.adaptadores.ListaHistorialAdapter;
import cl.javier.pool.adaptadores.ListaMesasAdapter;
import cl.javier.pool.db.DbHistorialMesa;
import cl.javier.pool.db.DbMesas;
import cl.javier.pool.entidades.HistorialMesa;
import cl.javier.pool.entidades.Mesas;

public class HistorialActivity extends AppCompatActivity {
    private RecyclerView listaMesas;
    private ArrayList<HistorialMesa> listaArrayMesas;
    private TextView histcant, histtot;
    private EditText etFechaHistCalc;
    private Button btnCalcPer, btnEliminarHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.WHITE);
//TODO: PENDIENTE ESTOS ELEMENTOS
        histcant = findViewById(R.id.histcant);
        histtot = findViewById(R.id.histtot);
        etFechaHistCalc = findViewById(R.id.etFechaHistCalc);
        btnCalcPer = findViewById(R.id.buttonCalcPer);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaHoy = dateFormat.format(Calendar.getInstance().getTime());

        // Establece la fecha de hoy en el EditText
        etFechaHistCalc.setText(fechaHoy);

        btnEliminarHistorial = findViewById(R.id.btnEliminarHistorial);

        btnEliminarHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarHistorialPorFecha();
            }
        });

        btnCalcPer.setOnClickListener(v -> {
            String fechaInput = etFechaHistCalc.getText().toString();
            calcularTotalCobrosPorDia(fechaInput);
        });



        listaMesas = findViewById(R.id.rv_lista_historial_mesas);
        listaMesas.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = getWidth();
                return true;
            }
        });

        cargarHistorial();


        actualizarContadores(listaArrayMesas, histcant, histtot);
//TODO: HACER QUE SE CARGUE EL TOTAL DE HOY Y EL TOTAL DE COBROS DE HOY

    }

    private void eliminarHistorialPorFecha() {
        String fechaSeleccionada = etFechaHistCalc.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            // Fecha inicial a las 15:00 del día seleccionado
            Date fechaInicio = dateFormat.parse(fechaSeleccionada + " 15:00");

            // Calcula la fecha de las 10:00 del día siguiente
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            calendar.add(Calendar.DAY_OF_YEAR, 1);  // Día siguiente
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            Date fechaFin = calendar.getTime();

            // Llamada al método para eliminar registros en el rango
            DbHistorialMesa dbHistorialMesa = new DbHistorialMesa(HistorialActivity.this);
            boolean resultado = dbHistorialMesa.eliminarMesasPorRango(fechaInicio, fechaFin);

            if (resultado) {
                Toast.makeText(this, "Historial eliminado para el día productivo seleccionado.", Toast.LENGTH_SHORT).show();
                // Actualiza la vista del historial si es necesario
                cargarHistorial();
            } else {
                Toast.makeText(this, "No se encontraron registros para eliminar.", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Formato de fecha incorrecto.", Toast.LENGTH_SHORT).show();
        }
    }


    private void calcularTotalCobrosPorDia(String fechaInput) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        int totalCobros = 0;
        int cantidadCobros = 0;

        try {
            // Define el rango de fechas para el período productivo de 15:00 a 10:00 del día siguiente
            Date inicioPeriodo = sdf.parse(fechaInput + " 15:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inicioPeriodo);
            calendar.add(Calendar.DAY_OF_YEAR, 1);  // Sumar un día
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            Date finPeriodo = calendar.getTime();

            for (HistorialMesa mesa : listaArrayMesas) {
                // Convierte la fecha y hora final de cada mesa a Date
                Date horaFinalMesa = sdf.parse(mesa.getFecha() + " " + mesa.getHora_final());

                // Verifica si la hora_final está dentro del rango
                if (horaFinalMesa != null && horaFinalMesa.after(inicioPeriodo) && horaFinalMesa.before(finPeriodo)) {
                    totalCobros += mesa.getValor();  // Suma el valor de los cobros
                    cantidadCobros++;  // Incrementa la cantidad de cobros
                }
            }

            // Muestra los resultados en los TextView correspondientes
            histtot.setText("Total cobros día: $" + totalCobros);
            histcant.setText("Cantidad cobros día: " + cantidadCobros);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Formato de fecha incorrecto. Usa DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarHistorial(){
        DbHistorialMesa dbMesas = new DbHistorialMesa(HistorialActivity.this);
        listaArrayMesas = new ArrayList<>();

        listaArrayMesas = dbMesas.mostrarMesasHistorial();
        if (listaArrayMesas != null && !listaArrayMesas.isEmpty()) {
            ListaHistorialAdapter adapter = new ListaHistorialAdapter(this,listaArrayMesas);
            listaMesas.setAdapter(adapter);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "No hay registros disponibles", Snackbar.LENGTH_SHORT).show();

        }
    }


    public void actualizarContadores(ArrayList<HistorialMesa> listaMesas, TextView tvCantidad, TextView tvTotalValor) {
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        int cantidad = 0;
        int sumaValor = 0;

        try {
            // Horas límite para el rango
            Date horaInicioRango = formatoHora.parse("15:00");
            Date horaFinRango = formatoHora.parse("09:00");

            for (HistorialMesa mesa : listaMesas) {
                Date horaFinal = formatoHora.parse(mesa.getHora_final());

                // Verificar si la hora final está entre 15:00 y 09:00
                if ((horaFinal.after(horaInicioRango) && horaFinal.before(formatoHora.parse("23:59")))
                        || (horaFinal.after(formatoHora.parse("00:00")) && horaFinal.before(horaFinRango))) {
                    cantidad++;
                    sumaValor += mesa.getValor();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Actualizar los TextViews con la cantidad y el valor total
        histcant.setText("Cantidad cobros: " + String.valueOf(cantidad));
        histtot.setText("Cantidad total: " +String.valueOf(sumaValor));
    }
}