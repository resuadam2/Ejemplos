package com.pmul.ejemplos.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;


public class DBManagerCajaSuper extends SQLiteOpenHelper {
    public static final String DB_NAME = "cajaSuperDB";

    public static final int DB_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";

    public static final String TABLE_CARRITO = "carrito";

    public static final String PRODUCTS_COL_CANT = "cantidad";

    public static final String PRODUCTS_COL_ID = "id";
    public static final String PRODUCTS_COL_NAME = "name";
    public static final String PRODUCTS_COL_STOCK = "stock";
    public static final String PRODUCTS_COL_PRICE = "price";
    public static final String PRODUCTS_COL_WARNING_STOCK = "w_stock";

    public DBManagerCajaSuper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBManagerCaja", DB_NAME + " creating " + TABLE_PRODUCTS);

        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS "+
                  TABLE_PRODUCTS + " (" +
                    PRODUCTS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PRODUCTS_COL_NAME + " string(255) NOT NULL UNIQUE, " +
                    PRODUCTS_COL_STOCK + " INTEGER NOT NULL, " +
                    PRODUCTS_COL_PRICE + " real NOT NULL, " +
                    PRODUCTS_COL_WARNING_STOCK + "INTEGER NOT NULL)");
            // Necesario hacer las dos por separado?
            db.execSQL("CREATE TABLE IF NOT EXISTS "+
                    TABLE_CARRITO + " (" +
                    PRODUCTS_COL_ID + " INTEGER PRIMARY KEY, " +
                    PRODUCTS_COL_CANT + " INTEGER NOT NULL ," +
                    " FOREIGN KEY ("+ PRODUCTS_COL_ID + ") REFERENCES "
                    + TABLE_PRODUCTS +"(" + PRODUCTS_COL_ID + "))");
            db.setTransactionSuccessful();
            createBasics();
        } catch (SQLException exc) {
            Log.e("DBManager.onCreate", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    private void createBasics() {
        addProduct("Patata", 40, 10, 3);
        addProduct("Tomate", 60, 20, 2);
        addProduct("Mandarina", 30, 5, 2.5);
        addProductCarrito("Patata", 5);
        addProductCarrito("Tomate", 10);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        Log.i("DBManagerCajaSuper", DB_NAME + " " + v1 + " -> " + v2);
        try {
            db.beginTransaction();
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onUpgrade", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    private void addProductCarrito(String name, int cant) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();

        values.put(PRODUCTS_COL_NAME, name);
        values.put(PRODUCTS_COL_CANT, cant);
        // esto hay que refactorizarlo
        try {
            db.beginTransaction();
            cursor = db.query( TABLE_PRODUCTS,
                    new String[] { PRODUCTS_COL_ID },
                    PRODUCTS_COL_NAME + " = ?",
                    new String[] { name }, null, null, null, "1");
            if( cursor.getCount() > 0 ) {
                cursor = db.query( TABLE_PRODUCTS,
                        new String [] { PRODUCTS_COL_ID, PRODUCTS_COL_STOCK },
                        PRODUCTS_COL_STOCK + "> ?",
                        new String[] { Integer.toString(cant) }, null, null, null, 1);
                if(cursor.getCount() > 0) {
                    ContentValues valuesStock = new ContentValues();
                    valuesStock.put(PRODUCTS_COL_ID, cursor.getString(0));
                    valuesStock.put(PRODUCTS_COL_STOCK, cursor.getInt(1));
                    db.update(TABLE_PRODUCTS,
                            values,
                            PRODUCTS_COL_ID + "= ?",
                            new String[] { cursor.getString(0) });
                }

            } else {
                db.insert(TABLE_PRODUCTS,
                        null,
                        values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onAdd", exc.getMessage());
        } finally {
            cursor.close();
            db.endTransaction();
        }
    }

    public void addProduct(String name, int stock, int w_stock, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();

        values.put(PRODUCTS_COL_NAME, name);
        values.put(PRODUCTS_COL_PRICE, price);
        values.put(PRODUCTS_COL_STOCK, stock);
        values.put(PRODUCTS_COL_WARNING_STOCK, w_stock);

        try {
            db.beginTransaction();
            cursor = db.query( TABLE_PRODUCTS,
                    new String[] { PRODUCTS_COL_ID },
                    PRODUCTS_COL_NAME + " = ?",
                    new String[] { name }, null, null, null, "1");
            if( cursor.getCount() > 0 ) {
                db.update(TABLE_PRODUCTS,
                        values,
                        PRODUCTS_COL_ID + "= ?",
                        new String[] { cursor.getString(0) });
            } else {
                db.insert(TABLE_PRODUCTS,
                        null,
                        values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onAdd", exc.getMessage());
        } finally {
            cursor.close();
            db.endTransaction();
        }
    }

    public void removeProduct(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            db.beginTransaction();
            cursor = db.query( TABLE_PRODUCTS,
                    new String[] { PRODUCTS_COL_ID },
                    PRODUCTS_COL_NAME + " = ?",
                    new String[] { name }, null, null, null, "1");
            if( cursor.getCount() > 0 ) {
                db.delete(TABLE_PRODUCTS, PRODUCTS_COL_ID + " = ?", new String[]{cursor.getString(0)});
            } else {
                Log.e("db.Remove", "Couldnt find an item with that name");
            }
            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( "dbRemove", exc.getMessage() );
        }
        finally {
            cursor.close();
            db.endTransaction();
        }
    }

    public void removeProduct(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete( TABLE_PRODUCTS, PRODUCTS_COL_ID + " = ?", new String[]{ Integer.toString(id) } );
            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( "dbRemove", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(DBManagerCajaSuper.TABLE_PRODUCTS,
                null, null, null, null, null, PRODUCTS_COL_NAME);
    }
}
