package cl.javier.pool.db;

import android.content.ContentValues;
import android.content.Context;
import cl.javier.pool.entidades.Mesas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbMesas extends DbHelper {
    static Context context;
    public DbMesas(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    public long insertarMesas(Integer numero,String hora ,Integer precio, Integer extras ){
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nro", numero);
            values.put("hora_inicio",hora);
            values.put("precio", precio);
            values.put("extras",extras);
            id = db.insert(TABLE_MESAS,null,values);

        }
        catch (Exception ex){
            ex.toString();
        }
        return id;
    }
    public static ArrayList<Mesas> mostrarMesas(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Mesas> listaMesas = new ArrayList<>();
        Mesas mesa = null;
        Cursor cursorMesas = null;

        cursorMesas = db.rawQuery("SELECT * FROM "+ TABLE_MESAS, null);

        if (cursorMesas.moveToFirst()){
            do{
                mesa = new Mesas();
                mesa.setId(cursorMesas.getInt(0));
                mesa.setNro(cursorMesas.getInt(1));
                mesa.setHora_inicio(cursorMesas.getString(2));
                mesa.setPrecio(cursorMesas.getInt(3));
                mesa.setExtras(cursorMesas.getInt(4));
                listaMesas.add(mesa);
            } while (cursorMesas.moveToNext());
        }
        cursorMesas.close();
        return listaMesas;
    }

    public Mesas verMesas(int nro){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Mesas mesa = null;
        Cursor cursorMesas;

        cursorMesas = db.rawQuery("SELECT * FROM "+ TABLE_MESAS + " WHERE nro = " + nro + " LIMIT 1", null);

        if (cursorMesas.moveToFirst()){

                mesa = new Mesas();
                mesa.setId(cursorMesas.getInt(0));
                mesa.setNro(cursorMesas.getInt(1));
                mesa.setHora_inicio(cursorMesas.getString(2));
                mesa.setPrecio(cursorMesas.getInt(3));
                mesa.setExtras(cursorMesas.getInt(4));
        }
        cursorMesas.close();
        return mesa;
    }
    public boolean editarMesa(Integer numero,String hora ,Integer precio, Integer extras ){
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("nro", numero);
            values.put("hora_inicio",hora);
            values.put("precio", precio);
            values.put("extras",extras);
            db.execSQL("UPDATE " + TABLE_MESAS + " SET nro = '" + numero + "', hora_inicio = '" + hora + "', precio = '" + precio + "', extras = '" + extras + "' WHERE nro ='" + numero +"'" );
            correcto = true;

        }
        catch (Exception ex){
            ex.toString();
            correcto = false;
        }finally {
            db.close();
        }return correcto;
        }

    public boolean eliminarMesa(Integer numero){
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("nro", numero);
            db.execSQL("DELETE FROM " + TABLE_MESAS + "  WHERE nro ='" + numero +"'" );
            correcto = true;

        }
        catch (Exception ex){
            ex.toString();
            correcto = false;
        }finally {
            db.close();
        }return correcto;
    }


    public static ArrayList<Integer> obtenerMesasOcupadas() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Integer> mesasOcupadas = new ArrayList<>();
        Cursor cursorMesas = null;

        // Consulta para obtener solo los números de las mesas ocupadas
        cursorMesas = db.rawQuery("SELECT nro FROM " + TABLE_MESAS + " WHERE hora_inicio IS NOT NULL", null);

        if (cursorMesas.moveToFirst()) {
            do {
                int numeroMesa = cursorMesas.getInt(0); // Obtener el número de la mesa
                mesasOcupadas.add(numeroMesa);
            } while (cursorMesas.moveToNext());
        }
        cursorMesas.close();
        return mesasOcupadas;
    }


}
