package com.pmul.ejemplos.view.ejemplo_db_sencillo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
        this.setContentView( R.layout.activity_android_contact );

        final ListView lvContacts = this.findViewById( R.id.lvContacts );
        final ImageButton btAdd = this.findViewById( R.id.btAdd );
        final ImageButton btSearch = this.findViewById( R.id.btSearch );

        btAdd.setOnClickListener(view -> addContact());
        btSearch.setOnClickListener(view -> searchContacts());

        this.registerForContextMenu( lvContacts );
        this.dbManager = new DBManagerEjemploSencillo( this.getApplicationContext() );
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final ListView lvContacts = this.findViewById( R.id.lvContacts );

        this.mainCursorAdapter = new SimpleCursorAdapter( this,
                R.layout.lvcontacts,
                null,
                new String[]{ DBManagerEjemploSencillo.CONTACTS_COL_NAME, DBManagerEjemploSencillo.CONTACTS_COL_TLF },
                new int[] { R.id.lvContacts_Name, R.id.lvContacts_Tlf } );

        lvContacts.setAdapter( this.mainCursorAdapter );
        this.updateContacts();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        this.mainCursorAdapter.getCursor().close();
        this.dbManager.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if ( v.getId() == R.id.lvContacts ) {
            this.getMenuInflater().inflate( R.menu.lvcontacts_context_menu, menu );
        }

        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position;
        if(item.getItemId() == R.id.lvcontacts_menu_item_delete) {
            this.deleteContact(pos);
            return true;
        }
        if(item.getItemId() == R.id.lvcontacts_menu_item_modify) {
            this.modifyContact(pos);
            return true;
        }
        if(item.getItemId() == R.id.lvcontacts_menu_item_call) {
            this.callContact(pos);
            return true;
        }
        return false;
    }



    private void searchContacts()
    {
        final EditText edName = this.findViewById( R.id.edName );
        final String text = edName.getText().toString();

        if ( !text.isEmpty() ) {
            Cursor cursor = this.dbManager.searchFor( text );

            if ( cursor != null ) {
                if ( cursor.moveToFirst() ) {
                    String rows = "";

                    do {
                        rows += cursor.getString( 0 ) + ": " + cursor.getString( 1 ) + "\n";
                    } while( cursor.moveToNext() );

                    AlertDialog.Builder dlg = new AlertDialog.Builder( this );
                    dlg.setMessage( rows );
                    dlg.create().show();
                    edName.setText( "" );
                } else {
                    Toast.makeText( this, this.getString( R.string.msgNoContacts ),
                            Toast.LENGTH_LONG ).show();
                }
            } else {
                Toast.makeText( this, "Internal BD Error", Toast.LENGTH_LONG ).show();
            }
        } else {
            Toast.makeText( this, this.getString( R.string.lblName ) + "??",
                    Toast.LENGTH_LONG ).show();
        }

        return;
    }

    private void deleteContact(int pos)
    {
        Cursor cursor = this.mainCursorAdapter.getCursor();

        if ( cursor.moveToPosition( pos ) ) {
            this.dbManager.remove( cursor.getString( 0 ) );
            this.updateContacts();
        } else {
            String errMsg = this.getString( R.string.msgInvalidPosition ) + ": " + pos;

            Log.e( "main.deleteContact", errMsg );
            Toast.makeText( this, errMsg, Toast.LENGTH_LONG ).show();
        }

        return;
    }

    private void modifyContact(int pos)
    {
        Cursor cursor = this.mainCursorAdapter.getCursor();

        if ( cursor.moveToPosition( pos ) ) {
            final EditText edTlf = new EditText( this );
            final String tlf = cursor.getString( 1 );
            final String name = cursor.getString( 0 );
            AlertDialog.Builder dlg = new AlertDialog.Builder( this );

            dlg.setTitle( this.getString( R.string.lblPhone ) + "?" );
            edTlf.setText( tlf );
            dlg.setView( edTlf );
            dlg.setNegativeButton( this.getString( R.string.lblCancel ), null );
            dlg.setPositiveButton( this.getString( R.string.lblOk ), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AndroidContactActivity.this.dbManager.add( name, edTlf.getText().toString() );
                    AndroidContactActivity.this.updateContacts();
                }
            });
            dlg.create().show();
        } else {
            String errMsg = this.getString( R.string.msgInvalidPosition ) + ": " + pos;

            Log.e( "main.modifyContact", errMsg );
            Toast.makeText( this, errMsg, Toast.LENGTH_LONG ).show();
        }

        return;
    }

    private void callContact(int pos)
    {
        Cursor cursor = this.mainCursorAdapter.getCursor();

        if ( cursor.moveToPosition( pos ) ) {
            final String tlf = cursor.getString( 1 );
            Intent intent = new Intent( Intent.ACTION_CALL, Uri.parse( "tel:" + tlf ) );

            try {
                this.startActivity( intent );
            } catch(SecurityException exc) {
                Toast.makeText( this,
                        this.getString( R.string.msgNoCall ),
                        Toast.LENGTH_LONG ).show();
            }
        } else {
            String errMsg = this.getString( R.string.msgInvalidPosition ) + ": " + pos;

            Log.e( "main.deleteContact", errMsg );
            Toast.makeText( this, errMsg, Toast.LENGTH_LONG ).show();
        }

        return;
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
