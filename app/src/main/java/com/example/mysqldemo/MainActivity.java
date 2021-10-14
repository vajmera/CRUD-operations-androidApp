package com.example.mysqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // reference to buttons and other controls of layout
    Button btn_add,btn_viewAll;
    EditText et_name,et_age,et_search;
    Switch sw_active;//boolean
    ListView lv_customerList;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    //It starts the application, onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name=findViewById(R.id.et_nam);
        et_age=findViewById(R.id.et_age);
        btn_add=findViewById(R.id.btn_add);
        btn_viewAll=findViewById(R.id.btn_viewAll);
        et_search=findViewById(R.id.et_search);

        sw_active=findViewById(R.id.sw_active);
        lv_customerList=findViewById(R.id.lv_customerList);

        //We wanted to update the table as soon as we start the app hence we copied ur code of viewAll button here, also we want same thing update when we click viewAll hence this code will be there also
        //We use Array Adapter in order to display the Lis in our app in a tabular manner instead of toast
        dataBaseHelper=new DataBaseHelper(MainActivity.this);
        List<CustomerModel> Lis=dataBaseHelper.getEveryone();

        customerArrayAdapter=new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone()); //second args creates the most basic layout for us
        //assosiating with our listVIew

        lv_customerList.setAdapter(customerArrayAdapter);
        
        





        //Listener methods
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerModel customerModel;
                try {

                    customerModel=new CustomerModel(-1,et_name.getText().toString(),Integer.parseInt(et_age.getText().toString()),sw_active.isChecked());
                    Toast.makeText(MainActivity.this,customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    customerModel=new CustomerModel(-1,"error",0,false);
                    Toast.makeText(MainActivity.this,"Please reenter",Toast.LENGTH_SHORT).show();
                }

                //takes in context, as reference to onClickListener()
                DataBaseHelper dataBaseHelper=new DataBaseHelper(MainActivity.this);

                boolean success=dataBaseHelper.addOne(customerModel);
                customerArrayAdapter=new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone()); //second args creates the most basic layout for us
                //assosiating with our listVIew

                lv_customerList.setAdapter(customerArrayAdapter);

                Toast.makeText(MainActivity.this,"Success : " + success,Toast.LENGTH_SHORT).show();
            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createing reference to our database

                DataBaseHelper dataBaseHelper=new DataBaseHelper(MainActivity.this);
                List<CustomerModel> Lis=dataBaseHelper.getEveryone(); // calling the function which we created before

                //We use Array Adapter in order to display the Lis in our app in a tabular manner instead of toast

               ArrayAdapter customerArrayAdapter=new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,Lis); //second args creates the most basic layout for us
                //assosiating with our listVIew

                lv_customerList.setAdapter(customerArrayAdapter);
                //Toast.makeText(MainActivity.this,Lis.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Seton item click listener : it responds to the click of the item
        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            //postion is which item was clicke in list
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                CustomerModel customerModel= (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(customerModel); //delete that row : for telling the deleteOne function what row to delete

                //We need to again refresh the list after deleting
                ArrayAdapter customerArrayAdapter=new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone()); //second args creates the most basic layout for us
                //assosiating with our listVIew

                lv_customerList.setAdapter(customerArrayAdapter);

                Toast.makeText(MainActivity.this,"Deleted : " + customerModel.toString(),Toast.LENGTH_SHORT).show();


            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                //String text=charSequence.toString();
                //We want to search above string in our db and then show it in our listView dynamically


                   DataBaseHelper dataBaseHelper=new DataBaseHelper(MainActivity.this);
                   List<CustomerModel> searching = dataBaseHelper.search(charSequence.toString());

                   //NOw we put the searched listview in our listview

                   ArrayAdapter customerArrayAdapter=new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,searching); //second args creates the most basic layout for us
                   //assosiating with our listVIew

                   lv_customerList.setAdapter(customerArrayAdapter);

                   Toast.makeText(MainActivity.this,"Found or not found",Toast.LENGTH_SHORT).show();






            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}