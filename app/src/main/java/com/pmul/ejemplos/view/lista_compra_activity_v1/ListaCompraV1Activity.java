package com.pmul.ejemplos.view.lista_compra_activity_v1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.model.Item;
import com.pmul.ejemplos.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraV1Activity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;

    private ArrayAdapter<Item> adaptadorItems;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra_v1);

        ListView lvLista = (ListView) this.findViewById( R.id.lvLista );
        Button btInserta = (Button) this.findViewById( R.id.btInsertaItem );

        // Lista
        this.items = new ArrayList<>();
        this.adaptadorItems = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_selectable_list_item,
                this.items );
        lvLista.setAdapter( this.adaptadorItems );

        // Inserta
        btInserta.setOnClickListener(v->onAdd());

        // Modifica
        lvLista.setOnItemLongClickListener((adapterView, view, i, l) -> onDelete(i));

        lvLista.setOnItemClickListener((adapterView, view, i, l) -> onEdit(i));

    }

    private boolean onDelete(int i) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            String msg = String.valueOf(R.string.seguro_de_borrar).concat(items.get(i).getNombre());
            builder.setTitle(msg);
            builder.setPositiveButton( R.string.btBorrar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int x) {
                    adaptadorItems.remove(items.get(i));
                    updateStatus();
                }
            });
            builder.setNegativeButton(R.string.btCancelar, null);
            builder.create().show();

            return true;
        } catch(NullPointerException exception) {
            Toast.makeText(this, R.string.error_borrando, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean onEdit(int i) {
            Intent subActividad = new Intent( this, ItemEditionActivity.class );
            Item item = this.adaptadorItems.getItem( i );

            subActividad.putExtra( "nombre", item.getNombre() );
            subActividad.putExtra( "cantidad", item.getNum() );
            subActividad.putExtra( "pos", i );
            this.startActivityForResult( subActividad, CODIGO_EDICION_ITEM );

            return true;
    }

    private void onAdd() {
        Intent subActividad = new Intent( this, ItemEditionActivity.class );

        subActividad.putExtra( "nombre", "" );
        subActividad.putExtra( "cantidad", 1 );
        this.startActivityForResult( subActividad, CODIGO_ADICION_ITEM );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_ADICION_ITEM
                && resultCode == Activity.RESULT_OK) {
            Item item = new Item(data.getExtras().getString("nombre").toString());
            item.setNum(data.getExtras().getInt("cantidad"));
            this.adaptadorItems.add(item);
            this.updateStatus();
        }

        if (requestCode == CODIGO_EDICION_ITEM
                && resultCode == Activity.RESULT_OK) {
            int pos = data.getExtras().getInt("pos");
            Item item = new Item(data.getExtras().getString("nombre").toString());

            item.setNum(data.getExtras().getInt("cantidad"));
            this.items.set(pos, item);
            this.adaptadorItems.notifyDataSetChanged();
        }
    }

    private void updateStatus() {
        TextView lblNum = (TextView) this.findViewById( R.id.lblNum );

        lblNum.setText( Integer.toString( this.adaptadorItems.getCount() ) );
    }
}
