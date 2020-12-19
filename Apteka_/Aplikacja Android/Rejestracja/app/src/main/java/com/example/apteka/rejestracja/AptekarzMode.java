package com.example.apteka.rejestracja;

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

public class AptekarzMode extends AppCompatActivity {
    private Button button1;
    private Object Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aptekarz_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.klient_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_aptekarz,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.wyloguj){
            Intent intent1 = new Intent(this,LoginActivity.class);
            this.startActivity(intent1);
            Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.dodaj_lek){
            Intent intent1 = new Intent(this,Dodaj_Leki.class);
           this.startActivity(intent1);
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        return;
    }

}



