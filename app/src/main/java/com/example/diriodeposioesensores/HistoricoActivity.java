package com.example.diriodeposioesensores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HistoricoActivity extends AppCompatActivity {

    private TextView txtHistorico;
    private Button btnVoltar;
    private BancoHelper bancoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        txtHistorico = findViewById(R.id.txtHistorico);
        btnVoltar = findViewById(R.id.btnVoltar);
        bancoHelper = new BancoHelper(this);

        carregarRegistrosDoBanco();

        // Ação do botão de voltar: fecha esta tela e retorna à MainActivity
        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void carregarRegistrosDoBanco() {
        // Abre o banco para leitura
        SQLiteDatabase db = bancoHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BancoHelper.TABELA, null);

        StringBuilder sb = new StringBuilder();

        // Percorre todos os registros salvos no SQLite
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(BancoHelper.COL_ID));
                float luz = cursor.getFloat(cursor.getColumnIndexOrThrow(BancoHelper.COL_LUZ));
                String gps = cursor.getString(cursor.getColumnIndexOrThrow(BancoHelper.COL_GPS));

                sb.append("ID: ").append(id)
                        .append("\nLuz: ").append(luz).append(" lx")
                        .append("\nGPS: ").append(gps)
                        .append("\n------------------------\n\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("Nenhum registro salvo no banco ainda.");
        }

        cursor.close();
        db.close();

        // Joga todos os dados formatados no TextView
        txtHistorico.setText(sb.toString());
    }
}