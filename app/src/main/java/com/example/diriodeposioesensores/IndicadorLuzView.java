package com.example.diriodeposioesensores;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

// Requisito 1 do trabalho: Componente gráfico customizado desenvolvido pelo grupo
public class IndicadorLuzView extends View {

    private Paint paint;
    private float intensidadeLuz = 0f;

    public IndicadorLuzView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    // Método para receber o valor do sensor e mandar redesenhar a tela
    public void setIntensidadeLuz(float luz) {
        this.intensidadeLuz = luz;
        invalidate(); // Força o Android a atualizar o desenho da View
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Define a cor do círculo com base na luminosidade
        if (intensidadeLuz > 50f) {
            paint.setColor(Color.parseColor("#FFEB3B")); // Amarelo (Ambiente claro)
        } else {
            paint.setColor(Color.parseColor("#2196F3")); // Azul (Ambiente escuro)
        }

        // Desenha um círculo centralizado na View
        float raio = Math.min(getWidth(), getHeight()) / 3f;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, raio, paint);
    }
}