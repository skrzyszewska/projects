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
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class Lista extends AppCompatActivity
{
    private String m_All = "Leki: Wszystkie";

    private String[] m_ListOfNames;
    private ListView ListView;
    private String Header = m_All;
    private boolean OnlyAvailable = false;
    private JSONArray PosortowanaTabelaLekow;
    private int ID;

    private View m_Form;
    private View m_ProgressBar;

    private boolean isAptekarz = false;
    private int typ = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @SuppressLint("CutPasteId") @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        try
        {
            ID = intent.getIntExtra("ID", 0);
            isAptekarz = intent.getBooleanExtra("AptekarzMode", false);
            typ = intent.getIntExtra("Typ", 0);
        }
        catch (Exception e)
        {
            Log.e("log_error", e.toString());
        }

        setActionBar(Header);
        setLists();

        ListView = findViewById(R.id.List);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,PojedynczyLekActivity.class);
                intent.putExtra("Name", m_ListOfNames[position]);
                intent.putExtra("ID", ID);
                intent.putExtra("AptekarzMode", isAptekarz);
                intent.putExtra("Typ", typ);
                startActivity(intent);
            }
        });

        m_Form = findViewById(R.id.List);
        m_ProgressBar = findViewById(R.id.list_progress);

        if(PosortowanaTabelaLekow == null)
        {
            showProgress(true);
            new Lista.Task().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_lista_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.is_Available)
        {
            if(OnlyAvailable)
            {
                Header = "Leki: Tylko Dostępne";
            }
            else
            {
                Header = "Leki: Wszystkie";
            }
            setActionBar(Header);
            setLists();
        }
        else
        {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        try
        {
            JSONArray tab;
            if(PosortowanaTabelaLekow != null)
            {
                tab = PosortowanaTabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki ORDER BY Nazwa");
            }

            ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<tab.length();i++)
            {
                JSONObject json_data = tab.getJSONObject(i);
                if(!OnlyAvailable)
                {
                    list.add(json_data.getString("Nazwa"));
                }
                else
                {
                    Integer count = json_data.getInt("Ilosc");
                    if(count > 0)
                    {
                        list.add(json_data.getString("Nazwa"));
                    }
                }
            }

            m_ListOfNames = new String[list.size()];
            int[] m_ListOfIcon = new int[list.size()];

            for(int i = 0; i < list.size(); i++)
            {
                m_ListOfNames[i] = list.get(i);
                Setters.setImageTab(m_ListOfIcon, i, m_ListOfNames[i]);
            }

            adapter = new Adapter(this, m_ListOfNames, m_ListOfIcon);
            ListView.setAdapter(adapter);

            OnlyAvailable = !OnlyAvailable;
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());
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
        @Override
        protected Boolean doInBackground(Void... voids)
        {
            try
            {
                PosortowanaTabelaLekow = new ConnectToDataBase().Execute("SELECT * FROM Leki ORDER BY Nazwa");
                Finish = PosortowanaTabelaLekow != null;
                return PosortowanaTabelaLekow != null;
            }
            catch (Exception e)
            {
                PosortowanaTabelaLekow = null;
                Finish = false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean succes)
        {
            showProgress(false);
            if(Finish || succes)
            {
                setLists();
            }
            else
            {
                Toast.makeText(Lista.this,"Błąd podczas pobierania danych!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
        }
    }
}
