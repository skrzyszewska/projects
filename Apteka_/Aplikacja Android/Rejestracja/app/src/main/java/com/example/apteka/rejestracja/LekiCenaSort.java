package com.example.apteka.rejestracja;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LekiCenaSort extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Context context;
    List<String> tasks;
    ArrayAdapter<String> adapter;
    int klientid = 0;

    private MaterialSearchView m_MaterialSearchView;
    private String[] m_SearchPropositionList;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leki_a_do_z);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.leki_a_z_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent2 = getIntent();
        klientid = intent2.getIntExtra("ID", 0);
        NavigationView navigationView = findViewById(R.id.anonim_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = (ListView) findViewById(R.id.lista);
        List<PojedynczyLek> contacts = new ArrayList<PojedynczyLek>();
        LekiAdapter adapter = new LekiAdapter(this, R.layout.simple_list_item_1, contacts);

        m_MaterialSearchView = findViewById(R.id.mysearch);
        m_MaterialSearchView.clearFocus();
        m_MaterialSearchView.setSuggestions(m_SearchPropositionList);
            m_MaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener(){
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

        String OutputData = "";
        try{
            JSONArray tab = new db_connect().getResults("SELECT * FROM Leki ORDER BY NAZWA");

            for(int i=0;i<tab.length();i++){
                // OBRÓBKA WSZYSTKICH DANYCH //
                JSONObject json_data = tab.getJSONObject(i);

                //int id = json_data.getInt("ID");
                String nazwa = json_data.getString("Nazwa");
                // String zdjecie = json_data.getString("");
                // String sklad = json_data.getString("Skład");
                //String opis = json_data.getString("Opis");
                //Integer ilosc = json_data.getInt("Ilość");
                //boolean naRecepte = json_data.getBoolean("NaRecepte");
                Integer cena = json_data.getInt("Cena");
                String cena2 = Integer.toString(cena);

                //OutputData = +i + nazwa + "\n" + opis;
                contacts.add(new PojedynczyLek(nazwa, cena2));
                listView.setAdapter(adapter);

            }
        } catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.leki_a_z_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.HomePage)
        {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,AnonimMode.class);
            startActivity(intent);
        }
        else if (id == R.id.Medicines)
        {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LekiCenaSort.class);
            startActivity(intent);
        }
        else if (id == R.id.Koszyk)
        {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,Koszyk.class);
            intent.putExtra("ID",klientid);
            startActivity(intent);
        }
        else if (id == R.id.SingIn)
        {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,Login.class);
            startActivity(intent);
        }
        else if (id == R.id.SingOut)
        {
            Intent intent1 = new Intent(this,LoginActivity.class);

            String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + klientid + "'";
            new db_connect().getResults(query);


            klientid = 0;
            this.startActivity(intent1);
            Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_share)
        {
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send)
        {
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.leki_a_z_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.search);
        m_MaterialSearchView.setMenuItem(item);

        return true;
    }
}


