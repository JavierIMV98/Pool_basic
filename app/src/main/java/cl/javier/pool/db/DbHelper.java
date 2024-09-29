package cl.javier.pool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "pool.db";
    public static final String TABLE_MESAS = "mesas";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_MESAS + "("+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"+"nro INTEGER NOT NULL UNIQUE,"+"hora_inicio TEXT NOT NULL," +"precio INTEGER NOT NULL,"+"extras INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_MESAS);
        onCreate(sqLiteDatabase);
    }
}
