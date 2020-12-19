package com.example.apteka.rejestracja;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class KlientMode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Object Menu;
    private Button button1;
    private Button button2;
    int klientid;
    private MaterialSearchView m_MaterialSearchView;
    private String[] m_SearchPropositionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klient_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.klient_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button button = (Button) findViewById(R.id.button5);
        Button button2 = (Button) findViewById(R.id.button8);

        Intent intent = getIntent();
        klientid = intent.getIntExtra("ID", 0);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TextView textView = (TextView) findViewById(R.id.textView5);
                Context context;
                context = getApplicationContext();

                Intent intent = new Intent(context, LekiAdoZ.class);
                intent.putExtra("ID", klientid);

                   /* Toast.makeText(getApplicationContext(),
                            "Witaj :)      " + klientid, Toast.LENGTH_LONG)
                            .show();;
                            */
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TextView textView = (TextView) findViewById(R.id.textView5);
                Context context;
                context = getApplicationContext();

                Intent intent = new Intent(context, LekiCenaSort.class);
                intent.putExtra("ID", klientid);

                   /* Toast.makeText(getApplicationContext(),
                            "Witaj :)      " + klientid, Toast.LENGTH_LONG)
                            .show();;
                            */
                startActivity(intent);
            }
        });

        m_MaterialSearchView = findViewById(R.id.mysearch);
        m_MaterialSearchView.clearFocus();
        m_MaterialSearchView.setSuggestions(m_SearchPropositionList);
        m_MaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Here Create your filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //You can make change realtime if you typing here
                //See my tutorials for filtering with ListView
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.search || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.klient_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.search);
        m_MaterialSearchView.setMenuItem(item);

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

<<<<<<< HEAD
        if (id == R.id.HomePage) {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context, AnonimMode.class);
            startActivity(intent);
        } else if (id == R.id.Medicines) {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context, LekiCenaSort.class);
            startActivity(intent);
        } else if (id == R.id.SingOut) {
            Intent intent1 = new Intent(this, LoginActivity.class);
            Context context;
            context = getApplicationContext();
        }
            if (id == R.id.HomePage) {
                Intent intent = new Intent(context, AnonimMode.class);
                startActivity(intent);
            } else if (id == R.id.Medicines) {
                Intent intent = new Intent(context, LekiCenaSort.class);
                startActivity(intent);
            } else if (id == R.id.Koszyk) {
                Intent intent1 = new Intent(context, Koszyk.class);
                intent1.putExtra("ID", klientid);
                startActivity(intent1);
                Toast.makeText(this, "Koszyk!", Toast.LENGTH_LONG).show();
            } else if (id == R.id.SingOut) {
                Intent intent1 = new Intent(context, LoginActivity.class);

                String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + klientid + "'";
                new db_connect().getResults(query);

                this.startActivity(intent1);
                Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
            }
                else if (id == R.id.nav_share) {
                Toast.makeText(this, "Funkcja niedostepna!", Toast.LENGTH_LONG).show();
            } else if (id == R.id.nav_send) {
                Toast.makeText(this, "Funkcja niedostepna!", Toast.LENGTH_LONG).show();
            }

            DrawerLayout drawer = findViewById(R.id.klient_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
=======
        Context context;
        context = getApplicationContext();
        if (id == R.id.HomePage) {
            Intent intent = new Intent(context, AnonimMode.class);
            startActivity(intent);
        }
        else if (id == R.id.Medicines) {
            Intent intent = new Intent(context, LekiCenaSort.class);
            startActivity(intent);
        }
        else if (id == R.id.Koszyk){
                Intent intent1 = new Intent(context,Koszyk.class);
                intent1.putExtra("ID",klientid);
                startActivity(intent1);
                Toast.makeText(this, "Koszyk!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.SingOut){
            Intent intent1 = new Intent(context,LoginActivity.class);
            String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + klientid + "'";
            new db_connect().getResults(query);

            this.startActivity(intent1);
            Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_share) {
            Toast.makeText(this, "Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send) {
            Toast.makeText(this, "Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = findViewById(R.id.klient_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
>>>>>>> 727788db89d5ae433a3964691975d8b985a8ae6c
    }
}
