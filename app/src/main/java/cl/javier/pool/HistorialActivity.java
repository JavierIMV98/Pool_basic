package cl.javier.pool;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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
//TODO: HACER QUE SE CARGUE EL TOTAL DE HOY Y EL TOTAL DE COBROS DE HOY

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
}