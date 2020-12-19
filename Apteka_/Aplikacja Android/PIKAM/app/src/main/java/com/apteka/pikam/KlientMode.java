package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

public class KlientMode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private String m_Imie, m_Nazwisko;
    private int ID;

    public JSONArray TabelaLekow;
    public JSONArray HistoriaOperacji;

    private String[] m_SearchPropositionList;
    private String[] m_PropositionList;
    private ListView listView;
    private MaterialSearchView m_MaterialSearchView;

    private View m_Form;
    private View m_ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klient_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        if(ID == 0)
        {
            Toast.makeText(this, "Błędne ID Klienta", Toast.LENGTH_LONG).show();
        }
        m_Imie = intent.getStringExtra("Imie");
        m_Nazwisko = intent.getStringExtra("Nazwisko");
        setActionBar("Witaj: " + m_Imie + " " + m_Nazwisko + " w trybie klienta");

        m_Form = findViewById(R.id.ListOfElements);
        m_ProgressBar = findViewById(R.id.klient_progress);

        if(TabelaLekow == null)
        {
            showProgress(true);
            Task Pobierz = new Task("SELECT * FROM Leki ORDER BY ID", "SELECT LekID, COUNT(LekID) AS Ilosc FROM HistoriaZamowien GROUP BY LekID ORDER BY Ilosc DESC");
            Pobierz.execute();
        }
        setLists();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showProgress(true);
                Task Pobierz = new Task("SELECT * FROM Leki ORDER BY ID", "SELECT LekID, COUNT(LekID) AS Ilosc FROM HistoriaZamowien GROUP BY LekID ORDER BY Ilosc DESC", view);
                Pobierz.execute();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,PojedynczyLekActivity.class);
                intent.putExtra("Name", m_PropositionList[position]);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", m_Imie);
                intent.putExtra("Nazwisko", m_Nazwisko);
                startActivity(intent);
            }
        });

        m_MaterialSearchView = findViewById(R.id.mysearch);
        m_MaterialSearchView.clearFocus();
        m_MaterialSearchView.setSuggestions(m_SearchPropositionList);
        m_MaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context, SearchListResults.class);
                intent.putExtra("Query", query);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", m_Imie);
                intent.putExtra("Nazwisko", m_Nazwisko);
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
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            showProgress(true);
            if(AnonimMode.m_Koszyk != null && AnonimMode.m_Koszyk.UsunKoszyk())
            {
                LoginActivity.UserType = "";
                LoginActivity.UserType = "";

                Toast.makeText(this,"Zostałeś Wylogowany!", Toast.LENGTH_LONG).show();

                Context context = getApplicationContext();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.search);
        m_MaterialSearchView.setMenuItem(item);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody") @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        Context context;
        context = getApplicationContext();
        switch(id)
        {
            case R.id.home_page:
                intent = new Intent(context,KlientMode.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", m_Imie);
                intent.putExtra("Nazwisko", m_Nazwisko);
                startActivity(intent);
                break;
            case R.id.medicines:
                intent = new Intent(context,Lista.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                break;
            case R.id.Koszyk:
                intent = new Intent(context,Koszyk.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", m_Imie);
                intent.putExtra("Nazwisko", m_Nazwisko);
                startActivity(intent);
                break;
            case R.id.Historia:
                intent = new Intent(context,Historia.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", m_Imie);
                intent.putExtra("Nazwisko", m_Nazwisko);
                startActivity(intent);
                break;
            case R.id.MojeKonto:
                intent = new Intent(context,MojeKonto.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                break;
            case R.id.Logout:
                showProgress(true);
                if(AnonimMode.m_Koszyk != null && AnonimMode.m_Koszyk.UsunKoszyk())
                {
                    Toast.makeText(this,"Zostałeś Wylogowany!", Toast.LENGTH_LONG).show();

                    intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Wylogowanie nieudane!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionBar(String heading)
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }

    private void setLists()
    {
        Adapter adapter;
        int[] m_PropositionListOfIcon;
        try
        {
            JSONArray tab, historia;
            if(TabelaLekow != null)
            {
                tab = TabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki ORDER BY ID");
            }
            if(HistoriaOperacji != null)
            {
                historia = HistoriaOperacji;
            }
            else
            {
                historia = new ConnectToDataBase().Execute("SELECT LekID, COUNT(LekID) AS Ilosc FROM HistoriaZamowien GROUP BY LekID ORDER BY Ilosc DESC");
            }
            m_SearchPropositionList = new String[tab.length()];
            for(int i = 0; i < tab.length(); i++) m_SearchPropositionList[i] = tab.getJSONObject(i).getString("Nazwa");

            int size = 6;
            if(historia.length() < size)
            {
                size = historia.length();
            }

            m_PropositionList = new String[size];
            m_PropositionListOfIcon = new int[size];

            for(int i=0;i<size;i++)
            {
                int index = 0;
                JSONObject json_data, json_data2;

                json_data = historia.getJSONObject(i);
                Integer id = json_data.getInt("LekID");

                for(int j=0;j<tab.length();j++)
                {
                    json_data2 = tab.getJSONObject(j);
                    Integer idx = json_data2.getInt("ID");
                    if(id.equals(idx))
                    {
                        index = j;
                        break;
                    }
                }

                json_data2 = tab.getJSONObject(index);
                m_PropositionList[i] = json_data2.getString("Nazwa");
                Setters.setImageTab(m_PropositionListOfIcon, i, m_PropositionList[i]);
            }
            listView = findViewById(R.id.ListOfElements);
            adapter = new Adapter(this, m_PropositionList, m_PropositionListOfIcon);
            listView.setAdapter(adapter);

        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error parsing data " +e.toString());
            m_PropositionList = new String[] { "Halset", "Ibuprom", "Pulneo", "Vocaler" };
            m_PropositionListOfIcon = new int[] {
                    R.drawable.halset,
                    R.drawable.ibuprom,
                    R.drawable.pulneo,
                    R.drawable.vocaler };

            listView = findViewById(R.id.ListOfElements);
            adapter = new Adapter(this, m_PropositionList, m_PropositionListOfIcon);
            listView.setAdapter(adapter);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        m_Form.setVisibility(show ? View.GONE : View.VISIBLE);
        m_Form.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                m_Form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        m_ProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        m_ProgressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                m_ProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Boolean>
    {
        Boolean Finish;
        private View m_View;
        private String Query, QuerySecond;
        Task(String query, String query2)
        {
            m_View = null;
            Query = query;
            QuerySecond = query2;
        }
        Task(String query, String query2, View view)
        {
            m_View = view;
            Query = query;
            QuerySecond = query2;
        }

        @Override
        protected void onPostExecute(final Boolean succes)
        {
            showProgress(false);
            if(m_View != null)
            {
                if(Finish || succes)
                {
                    Snackbar.make(m_View, "Pobrano dane z serwera!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    setLists();
                }
                else
                {
                    Snackbar.make(m_View, "Błąd podczas pobierania danych!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
            else
            {
                if(Finish || succes)
                {
                    Toast.makeText(KlientMode.this,"Pobrano dane z serwera!", Toast.LENGTH_LONG).show();
                    setLists();
                }
                else
                {
                    Toast.makeText(KlientMode.this,"Błąd podczas pobierania danych!", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Finish = false;
            try
            {
                TabelaLekow = new ConnectToDataBase().Execute(Query);
                HistoriaOperacji = new ConnectToDataBase().Execute(QuerySecond);
                Finish = HistoriaOperacji != null && TabelaLekow != null;
                return Finish;
            }
            catch (Exception e)
            {
                TabelaLekow = null;
                Finish = false;
            }
            return false;
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
        }
    }
}
