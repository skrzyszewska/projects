package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class SearchListResults extends AppCompatActivity
{
    private int[] m_PropositionListOfIcon;
    private String[] m_PropositionList;
    private ListView listView;
    private int ID;

    private View m_Form;
    private View m_ProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));



        m_Form = findViewById(R.id.SearchList);
        m_ProgressBar = findViewById(R.id.search_progress);

        showProgress(true);
        Intent intent = getIntent();
        String label = intent.getStringExtra("Query");
        ID = intent.getIntExtra("ID", 0);
        final String Imie = intent.getStringExtra("Imie");
        final String Nazwisko = intent.getStringExtra("Nazwisko");
        boolean IsAptekarz = intent.getBooleanExtra("AptekarzMode", false);
        setActionBar(label);
        setListView(label);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listView = findViewById(R.id.SearchList);
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
                intent.putExtra("Imie", Imie);
                intent.putExtra("Nazwisko", Nazwisko);
                intent.putExtra("AptekarzMode", intent.getBooleanExtra("AptekarzMode", false));
                intent.putExtra("Typ", intent.getIntExtra("Typ", 0));
                startActivity(intent);
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            showProgress(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onBackPressed();
        return true;
    }

    private void setListView(String query)
    {
        Adapter adapter;
        try {
            JSONArray tab;
            if(AnonimMode.TabelaLekow != null)
            {
                tab = AnonimMode.TabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki");
            }
            ArrayList<String> list = new ArrayList<>();

            for(int i=0;i<tab.length();i++)
            {
                JSONObject json_data = tab.getJSONObject(i);
                String name = json_data.getString("Nazwa");
                String temp = name.toLowerCase();
                String temp2 = query.toLowerCase();
                if(temp.contains(temp2))
                {
                    list.add(name);
                }
            }

            m_PropositionList = new String[list.size()];
            m_PropositionListOfIcon = new int[list.size()];

            for(int i = 0; i < list.size(); i++)
            {
                m_PropositionList[i] = list.get(i);
                Setters.setImageTab(m_PropositionListOfIcon, i, m_PropositionList[i]);
            }

            listView = findViewById(R.id.SearchList);
            adapter = new Adapter(this, m_PropositionList, m_PropositionListOfIcon);
            listView.setAdapter(adapter);
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

    private void setActionBar(String name)
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(name);
            actionBar.show();
        }
    }
}
