package com.pmul.ejemplos.view.ajustes_v2;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.pmul.ejemplos.R;

public class AjustesActivity extends AppCompatActivity {
    public AjustesActivity() {
        super(R.layout.activity_ajustes_v2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, AjustesFragment.class, null)
                    .commit();
        }
    }
}
