package com.pmul.ejemplos.view.ejemplo_db_sencillo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.bd.DBManagerEjemploSencillo;

public class AndroidContactActivity extends AppCompatActivity {
    private DBManagerEjemploSencillo dbManager;
    private CursorAdapter mainCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_main );

        final ListView lvContacts = this.findViewById( R.id.lvContacts );
        final ImageButton btAdd = this.findViewById( R.id.btAdd );
        final ImageButton btSearch = this.findViewById( R.id.btSearch );

        btAdd.setOnClickListener(view -> addContact());
        btSearch.setOnClickListener(view -> searchContacts());

        this.registerForContextMenu( lvContacts );
        this.dbManager = new DBManagerEjemploSencillo( this.getApplicationContext() );
    }

    private void searchContacts() {
    }

    private void addContact() {
        final EditText edName = this.findViewById( R.id.edName );
        final String name = edName.getText().toString();

        if ( !name.isEmpty() ) {
            final EditText edTlf = new EditText( this );
            AlertDialog.Builder dlg = new AlertDialog.Builder( this );
            dlg.setTitle( this.getString( R.string.lblPhone ) + "?" );
            dlg.setView( edTlf );
            dlg.setNegativeButton( this.getString( R.string.btCancelar ), null );
            dlg.setPositiveButton( this.getString( R.string.btGuardar ), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AndroidContactActivity.this.dbManager.add( name, edTlf.getText().toString() );
                    edName.setText( "" );
                    AndroidContactActivity.this.updateContacts();
                }
            });
            dlg.create().show();
        } else {
            Toast.makeText( this, this.getString( R.string.lblName ) + "??",
                    Toast.LENGTH_LONG ).show();
        }
    }

    private void updateContacts() {
        this.mainCursorAdapter.changeCursor( this.dbManager.getAllContacts() );
    }
}
