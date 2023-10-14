package com.pmul.ejemplos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.view.ajustes_v2.AjustesActivity;
import com.pmul.ejemplos.view.ajustes_v2.AjustesFragment;
import com.pmul.ejemplos.view.ejemplo_db_sencillo.AndroidContactActivity;
import com.pmul.ejemplos.view.lista_compra_activity_v1.ListaCompraV1Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.lista_compra_v1);

        b1.setOnClickListener(v->openListaCompraV1());

        Button b2 = (Button) findViewById(R.id.ejemplo_sencillo_bd);

        b2.setOnClickListener(v->openEjemploSencilloDB());

        Button b3 = (Button) findViewById(R.id.ajustes_v2);

        b3.setOnClickListener(v->openAjustesV2());
    }

    private void openAjustesV2() {
        Intent intent = new Intent(this, AjustesActivity.class);
        startActivity(intent);
    }

    private void openListaCompraV1() {
        Intent intent = new Intent(this, ListaCompraV1Activity.class);
        startActivity(intent);
    }

    private void openEjemploSencilloDB() {
        Intent intent = new Intent(this, AndroidContactActivity.class);
        startActivity(intent);
    }
}