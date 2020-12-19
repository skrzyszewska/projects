package com.example.apteka.rejestracja;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.klient_toolbar);
        setSupportActionBar(toolbar);

        Button button1 = (Button) findViewById(R.id.Button_klient);
        Button button2 = (Button) findViewById(R.id.Button_aptekarz);


            button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //TextView textView = (TextView) findViewById(R.id.textView5);
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,Rejestracja_Dla_Klienta.class);
                    startActivity(intent);
                }
            });

            button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //TextView textView = (TextView) findViewById(R.id.textView5);
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,Rejestracja_Dla_Aptekarza.class);
                    startActivity(intent);
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_podstawowe,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

}


