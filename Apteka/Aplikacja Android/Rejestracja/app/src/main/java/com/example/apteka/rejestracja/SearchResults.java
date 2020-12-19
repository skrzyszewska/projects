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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class SearchResults extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialSearchView m_MaterialSearchView;
    private String[] m_SearchPropositionList;
    private int[] m_SearchIconResult;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer=findViewById(R.id.search_drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
        this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Intent intent2=getIntent();
        m_SearchPropositionList=intent2.getStringArrayExtra("Search String Result");
        m_SearchIconResult=intent2.getIntArrayExtra("Search Icon Result");
        NavigationView navigationView=findViewById(R.id.anonim_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = findViewById(R.id.ListOfElements);
        Adapter adapter = new Adapter(this, m_SearchPropositionList, m_SearchIconResult);
        listView.setAdapter(adapter);


        m_MaterialSearchView = findViewById(R.id.mysearch);
        m_MaterialSearchView.clearFocus();
        m_MaterialSearchView.setSuggestions(m_SearchPropositionList);
        m_MaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context, SearchResults.class);
                intent.putExtra("Search String Result", m_SearchPropositionList);
                intent.putExtra("Search Icon Result", m_SearchIconResult);
                startActivity(intent);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.search_drawer_layout);
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
        else if (id == R.id.SingIn)
        {
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share)
        {
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send)
        {
            Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.search_drawer_layout);
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
