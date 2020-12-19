package com.example.apteka.rejestracja;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class AnonimMode extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialSearchView m_MaterialSearchView;
    private String[] m_SearchPropositionList;
    private String[] m_PropositionList;
    private int[] m_PropositionListOfIcon;
    private ListView listView;
    private Adapter adapter;
//   private StrictMode.ThreadPolicy policy;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonim_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.anonim_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        try {
            JSONArray tab = new db_connect().getResults("SELECT * FROM Leki ORDER BY NAZWA");
            m_SearchPropositionList = new String[tab.length()];
            for(int i = 0; i < tab.length(); i++) m_SearchPropositionList[i] = tab.getJSONObject(i).getString("Nazwa");

            m_PropositionList = new String[4];
            m_PropositionListOfIcon = new int[4];

            Random generator = new Random();
            for(int i=0;i<4;i++)
            {
                int random = generator.nextInt(tab.length());
                JSONObject json_data = tab.getJSONObject(i);
                m_PropositionList[i] = json_data.getString("Nazwa");
                setImageTab(i, random);
            }
            listView = findViewById(R.id.ListOfElements);
            adapter = new Adapter(this, m_PropositionList, m_PropositionListOfIcon);
            listView.setAdapter(adapter);

        } catch(Exception e){
            Log.e("log_tag", "Error parsing data "+e.toString());
            m_PropositionList = new String[] { "Halset", "Ibuprom", "Pulneo", "Vocaler" };
            m_PropositionListOfIcon = new int[] {
                    R.drawable.halset,
                    R.drawable.ibuprom,
                    R.drawable.pulneo,
                    R.drawable.vocaler };

            //m_SearchPropositionList = m_PropositionList;

            listView = findViewById(R.id.ListOfElements);
            adapter = new Adapter(this, m_PropositionList, m_PropositionListOfIcon);
            listView.setAdapter(adapter);
        }

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
                intent.putExtra("Search Icon Result", m_PropositionListOfIcon);
                startActivity(intent);
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
        DrawerLayout drawer = findViewById(R.id.anonim_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        return id == R.id.search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        Context context;
        context = getApplicationContext();
        if (id == R.id.HomePage){
            Intent intent = new Intent(context,AnonimMode.class);
            startActivity(intent);
        }
        else if (id == R.id.Medicines){
            Intent intent = new Intent(context,LekiCenaSort.class);
            startActivity(intent);
        }
        else if (id == R.id.SingIn){
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share){
            Toast.makeText(AnonimMode.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send){
            Toast.makeText(AnonimMode.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.anonim_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.search);
        m_MaterialSearchView.setMenuItem(item);

        return true;
    }

    private void setImageTab(int i, int index)
    {
        switch (index)
        {
            case 1:
                m_PropositionListOfIcon[i] = R.drawable.acatar;
                break;
            case 2:
                m_PropositionListOfIcon[i] = R.drawable.acidolac;
                break;
            case 3:
                m_PropositionListOfIcon[i] = R.drawable.amertil;
                break;
            case 4:
                m_PropositionListOfIcon[i] = R.drawable.apap;
                break;
            case 5:
                m_PropositionListOfIcon[i] = R.drawable.bellissa;
                break;
            case 6:
                m_PropositionListOfIcon[i] = R.drawable.centrum;
                break;
            case 7:
                m_PropositionListOfIcon[i] = R.drawable.etopiryna;
                break;
            case 8:
                m_PropositionListOfIcon[i] = R.drawable.gripblokerexpress;
                break;
            case 9:
                m_PropositionListOfIcon[i] = R.drawable.gripexnoc;
                break;
            case 10:
                m_PropositionListOfIcon[i] = R.drawable.halset;
                break;
            case 11:
                m_PropositionListOfIcon[i] = R.drawable.hascosept;
                break;
            case 12:
                m_PropositionListOfIcon[i] = R.drawable.herbapect;
                break;
            case 13:
                m_PropositionListOfIcon[i] = R.drawable.hialeye;
                break;
            case 14:
                m_PropositionListOfIcon[i] = R.drawable.ibufendladzieci;
                break;
            case 15:
                m_PropositionListOfIcon[i] = R.drawable.ibuprom;
                break;
            case 16:
                m_PropositionListOfIcon[i] = R.drawable.juvitc;
                break;
            case 17:
                m_PropositionListOfIcon[i] = R.drawable.manti;
                break;
            case 18:
                m_PropositionListOfIcon[i] = R.drawable.metafen;
                break;
            case 19:
                m_PropositionListOfIcon[i] = R.drawable.multilac;
                break;
            case 20:
                m_PropositionListOfIcon[i] = R.drawable.neomag;
                break;
            case 21:
                m_PropositionListOfIcon[i] = R.drawable.nervomix;
                break;
            case 22:
                m_PropositionListOfIcon[i] = R.drawable.oeparol;
                break;
            case 23:
                m_PropositionListOfIcon[i] = R.drawable.plusssz;
                break;
            case 24:
                m_PropositionListOfIcon[i] = R.drawable.polopiryna;
                break;
            case 25:
                m_PropositionListOfIcon[i] = R.drawable.pranalen;
                break;
            case 26:
                m_PropositionListOfIcon[i] = R.drawable.pulneo;
                break;
            case 27:
                m_PropositionListOfIcon[i] = R.drawable.ranigast;
                break;
            case 28:
                m_PropositionListOfIcon[i] = R.drawable.rutinacea;
                break;
            case 29:
                m_PropositionListOfIcon[i] = R.drawable.scorbolamid;
                break;
            case 30:
                m_PropositionListOfIcon[i] = R.drawable.stoperan;
                break;
            case 31:
                m_PropositionListOfIcon[i] = R.drawable.sylimaryna;
                break;
            case 32:
                m_PropositionListOfIcon[i] = R.drawable.uligixt;
                break;
            case 33:
                m_PropositionListOfIcon[i] = R.drawable.uniben;
                break;
            case 34:
                m_PropositionListOfIcon[i] = R.drawable.urosept;
                break;
            case 35:
                m_PropositionListOfIcon[i] = R.drawable.vitaminer;
                break;
            case 36:
                m_PropositionListOfIcon[i] = R.drawable.vocaler;
                break;
            case 37:
                m_PropositionListOfIcon[i] = R.drawable.xenna;
                break;
            default:
                m_PropositionListOfIcon[i] = R.drawable.logo50x50;
                break;
        }
    }
}


