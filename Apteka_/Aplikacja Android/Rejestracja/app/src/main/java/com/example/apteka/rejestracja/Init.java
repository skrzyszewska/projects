package com.example.apteka.rejestracja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class Init extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public Toolbar toolbar;
    public MenuItem item;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public MaterialSearchView m_MaterialSearchView;
    public String[] m_SearchPropositionList;
    public boolean Stay = false;

    public Init(boolean stay){
        if(stay){
            Stay = true;
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.anonim_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        m_MaterialSearchView = findViewById(R.id.mysearch);
        m_MaterialSearchView.clearFocus();
        m_MaterialSearchView.setSuggestions(m_SearchPropositionList);
        m_MaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query){
                //Here Create your filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                //You can make change realtime if you typing here
                //See my tutorials for filtering with ListView
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(Stay){
            return;
        }
        drawer = findViewById(R.id.anonim_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.HomePage){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,AnonimMode.class);
            startActivity(intent);
        }
        else if (id == R.id.Medicines){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LekiAdoZ.class);
            startActivity(intent);
        }
        else if (id == R.id.Koszyk){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,Koszyk.class);
            startActivity(intent);
        }
        else if (id == R.id.SingIn){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share){
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send){
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        drawer = findViewById(R.id.anonim_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        item = menu.findItem(R.id.search);
        m_MaterialSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        return id == R.id.search || super.onOptionsItemSelected(item);
    }
}
