package com.example.diriodeposioesensores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Requisito 2 do trabalho: Armazenamento utilizando SQLite
public class BancoHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "diario_sensores.db";
    private static final int VERSAO_BANCO = 1;

    public static final String TABELA = "registros";
    public static final String COL_ID = "id";
    public static final String COL_LUZ = "luz";
    public static final String COL_GPS = "gps";

    public BancoHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LUZ + " REAL, " +
                COL_GPS + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    // Metodo para salvar os dados no banco
    public boolean inserirRegistro(float luz, String gps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_LUZ, luz);
        valores.put(COL_GPS, gps);
        long resultado = db.insert(TABELA, null, valores);
        return resultado != -1; // Retorna true se salvou com sucesso
    }
}