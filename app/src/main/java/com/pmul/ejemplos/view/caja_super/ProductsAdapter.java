package com.pmul.ejemplos.view.caja_super;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pmul.ejemplos.R;
import com.pmul.ejemplos.model.Product;

import java.util.ArrayList;

public class ProductsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Product> products;

    private int layout;

    public ProductsAdapter(Context context, int layout, ArrayList<Product> products) {
        this.context = context;
        this.layout = layout;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return products.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View v = view;

        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);
        }

        Product product = getItem(i);
        TextView name = (TextView) v.findViewById(R.id.lvProductsName);
        TextView cant = (TextView) v.findViewById(R.id.lvProductsCant);
        TextView price = (TextView) v.findViewById(R.id.lvProductsPriceTotal);
        TextView price_uni = (TextView) v.findViewById(R.id.lvProductsPriceUni);

        name.setText(product.getName());
        cant.setText(product.getStock());
        price.setText(Double.toString(product.getPrice()));
        price_uni.setText(Double.toString(product.getPrice()*product.getStock()));
        return v;
    }
}
