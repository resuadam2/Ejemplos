package com.pmul.ejemplos.view.caja_super;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.model.Product;

import java.util.ArrayList;

public class CajaSuperActivity extends AppCompatActivity {
    private ProductsAdapter adaptadorProducts;
    private ArrayList<Product> products;
/*
    protected void onCreate(Bundle saverInstanceState) {
        super.onCreate(saverInstanceState);

        setContentView(R.layout.activity_cajasuper);

        ListView listView = (ListView) this.findViewById(R.id.lvProducts);

        Button btAdd = (Button) this.findViewById(R.id.btAddCaja);
        Button btShop = (Button) this.findViewById(R.id.btShop);
        
        btAdd.setOnClickListener(view -> addProduct());
        
        btShop.setOnClickListener(view -> shop());

        //Lista
        this.products = new ArrayList<>();
        this.adaptadorProducts = new ProductsAdapter(
                this,
                R.layout.lvproducts,
                this.products);

        listView.setAdapter(adaptadorProducts);
        
        listView.setOnItemClickListener((adapterView, view, i, l) -> editProduct(i));

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> deleteProduct(i));
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void shop() {
    }

    private void addProduct() {
    }

    private boolean deleteProduct(int i) {
    }

    private void editProduct(int i) {
    }

    private double getTotalPrice() {
        double toret = 0;
        for(Product p : products) {
            toret += p.getPrice() * p.getStock();
        }
        return toret;
    }

    private void updateStatus() {
        TextView lblTotal = (TextView) this.findViewById(R.id.lblTotal);

        lblTotal.setText(Double.toString(getTotalPrice()));
    }
*/
}
