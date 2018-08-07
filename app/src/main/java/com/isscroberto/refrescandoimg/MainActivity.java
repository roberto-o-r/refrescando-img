package com.isscroberto.refrescandoimg;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layout_configuracion)
    LinearLayout layoutConfiguracion;
    @BindView(R.id.text_url)
    EditText textUrl;
    @BindView(R.id.image_main)
    ImageView imageMain;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Obtener url si se ha guardado previamente.
        this.url = getSharedPreferences("com.isscroberto.refrescandoimg", 0).getString("url", "");
        if(!this.url.equals("")){
            IniciarTimer();
        }

    }

    private void IniciarTimer() {
        TimerTask local1 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.CargarImg();
                    }
                });
            }
        };
        new Timer().schedule(local1, 10L, 300000L);
    }

    private void CargarImg() {
        // Ocultar configuración.
        this.layoutConfiguracion.setVisibility(View.GONE);

        // Actualizar hora de última actualización.
        Calendar localCalendar = Calendar.getInstance();
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm");
        getSupportActionBar().setTitle("Última Actualización: " + localSimpleDateFormat.format(localCalendar.getTime()));

        // Cargar y mostrar imagen.
        this.imageMain.setVisibility(View.VISIBLE);
        Picasso.get().load(this.url).into(imageMain);
    }

    @OnClick(R.id.button_aceptar)
    public void buttonAceptarOnClick() {
        this.url = this.textUrl.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences("com.isscroberto.refrescandoimg", 0).edit();
        editor.putString("url", this.url);
        editor.apply();

        IniciarTimer();
    }

}
