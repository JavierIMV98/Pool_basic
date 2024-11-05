package cl.javier.pool.db;

import static cl.javier.pool.db.DbHelper.TABLE_HISTORIAL_MESAS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cl.javier.pool.entidades.HistorialMesa;
import cl.javier.pool.entidades.Mesas;

public class DbHistorialMesa extends DbHelper{
    static Context context;
    public DbHistorialMesa(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    public long insertarMesasHistorial(Integer numero,String hora,String horaf ,Integer precio, Integer valor, String fecha ){
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nro", numero);
            values.put("hora_inicio",hora);
            values.put("hora_final",horaf);
            values.put("precio", precio);
            values.put("valor",valor);
            values.put("fecha",fecha);
            id = db.insert(TABLE_HISTORIAL_MESAS,null,values);

        }
        catch (Exception ex){
            ex.toString();
        }
        return id;
    }
    public static ArrayList<HistorialMesa> mostrarMesasHistorial(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<HistorialMesa> listaMesas = new ArrayList<>();
        HistorialMesa mesa = null;
        Cursor cursorMesas = null;

        // Consulta SQL modificada para ordenar por Id de forma descendente
        cursorMesas = db.rawQuery("SELECT * FROM " + TABLE_HISTORIAL_MESAS + " ORDER BY id DESC", null);

        if (cursorMesas.moveToFirst()){
            do{
                mesa = new HistorialMesa();
                mesa.setId(cursorMesas.getInt(0));
                mesa.setNro(cursorMesas.getInt(1));
                mesa.setHora_inicio(cursorMesas.getString(2));
                mesa.setHora_final(cursorMesas.getString(3));
                mesa.setPrecio(cursorMesas.getInt(4));
                mesa.setValor(cursorMesas.getInt(5));
                mesa.setFecha(cursorMesas.getString(6));
                listaMesas.add(mesa);
            } while (cursorMesas.moveToNext());
        }
        cursorMesas.close();
        return listaMesas;
    }


    public HistorialMesa verHistorialMesas(int id){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        HistorialMesa mesa = null;
        Cursor cursorMesas;

        cursorMesas = db.rawQuery("SELECT * FROM "+ TABLE_HISTORIAL_MESAS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorMesas.moveToFirst()){

            mesa = new HistorialMesa();
            mesa.setId(cursorMesas.getInt(0));
            mesa.setNro(cursorMesas.getInt(1));
            mesa.setHora_inicio(cursorMesas.getString(2));
            mesa.setHora_final(cursorMesas.getString(3));
            mesa.setPrecio(cursorMesas.getInt(4));
            mesa.setValor(cursorMesas.getInt(5));
        }
        cursorMesas.close();
        return mesa;
    }

    public boolean eliminarMesasPorRango(Date fechaInicio, Date fechaFin) {
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            // Prepara los strings de fecha y hora
            String fechaInicioStr = dateOnlyFormat.format(fechaInicio);
            String horaInicioStr = "15:00";
            String horaFinStr = "10:00";

            // Ajusta la consulta SQL para el rango específico
            String query = "DELETE FROM " + TABLE_HISTORIAL_MESAS +
                    " WHERE (fecha = ? AND hora_final >= ?) OR " +  // Desde el día seleccionado a las 15:00
                    "      (fecha = ? AND hora_final <= ?)";       // Hasta el día siguiente a las 10:00

            // Ejecuta la consulta con las fechas y horas
            db.execSQL(query, new String[] {
                    fechaInicioStr, horaInicioStr,  // Primera condición (día y hora >= 15:00)
                    dateOnlyFormat.format(fechaFin), horaFinStr  // Segunda condición (día siguiente y hora <= 10:00)
            });

            correcto = true;

        } catch (Exception e) {
            e.printStackTrace();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }

    public boolean eliminarMesaHistorial(Integer id){
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("id", id);
            db.execSQL("DELETE FROM " + TABLE_HISTORIAL_MESAS + "  WHERE id ='" + id +"'" );
            correcto = true;

        }
        catch (Exception ex){
            ex.toString();
            correcto = false;
        }finally {
            db.close();
        }return correcto;
    }

}
