package com.pmul.ejemplos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.view.lista_compra_activity_v1.ListaCompraV1Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.lista_compra_v1);

        b1.setOnClickListener(v->openListaCompraV1());
    }

    private void openListaCompraV1() {
        Intent intent = new Intent(this, ListaCompraV1Activity.class);
        startActivity(intent);
    }
}