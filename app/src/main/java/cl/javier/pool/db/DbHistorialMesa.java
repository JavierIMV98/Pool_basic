package cl.javier.pool.db;

import static cl.javier.pool.db.DbHelper.TABLE_HISTORIAL_MESAS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import cl.javier.pool.entidades.HistorialMesa;
import cl.javier.pool.entidades.Mesas;

public class DbHistorialMesa extends DbHelper{
    static Context context;
    public DbHistorialMesa(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    public long insertarMesasHistorial(Integer numero,String hora,String horaf ,Integer precio, Integer valor ){
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
