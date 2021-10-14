package com.example.mysqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


//inheritence
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";

    public DataBaseHelper(@Nullable Context context) {
        //name of database
        super(context, "customer.db", null, 1);
    }

    @Override
    /*
        We have to define a constructor below from documanteaion
        SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        Create a helper object to create, open, and/or manage a database.


        context	Context: to use for locating paths to the the database This value may be null.
        name	String: of the database file, or null for an in-memory database This value may be null.
        factory	SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default This value may be null.
        version	int: number of the database (starting at 1); if the database is older,
        onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database; if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to downgrade the database
     */
    //this is called the first time a database is accessed.
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement= "CREATE TABLE " + CUSTOMER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT," + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);// for executing sql commands


    }

    @Override
    //this is called if the database version number changes
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CustomerModel customerModel)
    {
        SQLiteDatabase db=this.getWritableDatabase(); // inserting into data

        //COntent values is special class works like hashmap
        ContentValues cv= new ContentValues();


        //WE don;t have to do do ID number as its in auto increment mode lol
        cv.put(COLUMN_CUSTOMER_NAME,customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE,customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER,customerModel.isActive());

        //NUll column hacks middle parameter see documnetation
       long insert= db.insert(CUSTOMER_TABLE,null,cv);

       //returns postive which means we have added successfully
        if(insert==-1)
        {
            return  false;
        }

        return true;
    }

    public boolean deleteOne(CustomerModel customerModel)
    {
        //find customer model in database if its found delete it and reutn true else return false
        //We use writable as we are diting the database

        SQLiteDatabase db=this.getWritableDatabase();
        String queryString="DELETE FROM "+CUSTOMER_TABLE+" WHERE "+COLUMN_ID+" = "+customerModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst())
        {
            return true;
        }

        return false;
    }

    //A function to get the data from internal database of our mobile
    public List<CustomerModel> getEveryone()
    {
        List<CustomerModel> returnList=new ArrayList<>();
        //get everything from our CustomerTable

        String queryString ="SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db=this.getReadableDatabase(); //As we are only interested in getting the results from database not modify it.


        //Cursor is the result we get from our sql query (dataframe in python , df['colname']

        Cursor cursor = db.rawQuery(queryString,null);

        //cursor.moveToFirst returns true if there were items selected.
        if(cursor.moveToFirst())
        {
            //loop through cursor (result set ), and create new customer objects for each row . Put them in returnList . And we wamt view all button to display all this list
            do{
                int customerID=cursor.getInt(0); //position 0 is of int type
                String customerName=cursor.getString(1);
                int customerAge=cursor.getInt(2);
                boolean customerActive=(cursor.getInt(3)==1)?true:false;

                CustomerModel customerModel=new CustomerModel(customerID,customerName,customerAge,customerActive);
                returnList.add(customerModel);
            }while(cursor.moveToNext());
        }
        else{
            //failure no elements found
           // Toast.makeText(DataBaseHelper.this, "ERROR NO ELEMENTS", Toast.LENGTH_SHORT).show();
            Log.d("we","EROOR SEES");
        }
        //close the cursor and db
        cursor.close();
        db.close();
        return returnList;

    }

    public List<CustomerModel> search(String str)
    {
        List<CustomerModel> searched=new ArrayList<>();
        String query_search="SELECT * FROM " + CUSTOMER_TABLE + " WHERE "+ COLUMN_CUSTOMER_NAME +" LIKE ?";
        String param1 = "%" + str + "%";

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_search,new String[]{param1});

        if(cursor.moveToFirst()) //if found
        {
            do{
                int customerID=cursor.getInt(0); //position 0 is of int type
                String customerName=cursor.getString(1);
                int customerAge=cursor.getInt(2);
                boolean customerActive=(cursor.getInt(3)==1)?true:false;

                CustomerModel customerModel=new CustomerModel(customerID,customerName,customerAge,customerActive);
                searched.add(customerModel);


            }while(cursor.moveToNext());

        }

        else{

        }
        cursor.close();
        db.close();
        return searched;
    }



}
