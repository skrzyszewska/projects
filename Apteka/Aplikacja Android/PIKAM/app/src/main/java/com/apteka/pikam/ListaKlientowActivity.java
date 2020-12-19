package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class ListaKlientowActivity extends AppCompatActivity
{
    private ListView List;
    private ArrayList<Integer> IDList;
    private String[] m_ListOfNames;
    private int[] m_ListOfIcon ;

    private View m_ProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_klientow);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        m_ProgressBar = findViewById(R.id.list_progress);
        List = findViewById(R.id.List);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                showProgress(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaKlientowActivity.this);
                builder.setMessage("Co chcesz zrobić?").setPositiveButton("Edytuj konto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context,MojeKonto.class);
                        intent.putExtra("ID", IDList.get(position));
                        startActivity(intent);
                        dialog.cancel();
                        showProgress(false);
                    }
                }).setNegativeButton("Usun konto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task task = new Task("DELETE FROM Klienci WHERE ID = '" + IDList.get(position) + "'");
                        task.execute((Void) null);
                        setList();
                        dialog.cancel();
                        showProgress(false);
                    }
                }).setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showProgress(false);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        setList();
    }

    private void setList()
    {
        UserListTask task = new UserListTask();
        task.execute((Void) null);

        while(!task.FINISH)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Adapter adapter = new Adapter(this, m_ListOfNames, m_ListOfIcon);
        List.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onBackPressed();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        List.setVisibility(show ? View.GONE : View.VISIBLE);
        List.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                List.setVisibility(show ? View.GONE : View.VISIBLE);
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
    public class UserListTask extends AsyncTask<Void, Void, Boolean>
    {
        public boolean FINISH;

        @Override
        protected Boolean doInBackground(Void... params)
        {
            FINISH = false;
            showProgress(true);
            try
            {
                JSONArray tabelaKlienci = new ConnectToDataBase().Execute("SELECT * FROM Klienci ORDER BY Login");
                if(tabelaKlienci != null && tabelaKlienci.length() > 0)
                {
                    IDList = new ArrayList<>(tabelaKlienci.length());
                    ArrayList<String> list = new ArrayList<>(tabelaKlienci.length());
                    for(int i=0;i<tabelaKlienci.length();i++)
                    {
                        JSONObject json_data = tabelaKlienci.getJSONObject(i);
                        String name = json_data.getString("Imie") + " " + json_data.getString("Nazwisko");
                        IDList.add(json_data.getInt("ID"));
                        list.add(name);
                    }

                    m_ListOfNames = new String[list.size()];
                    m_ListOfIcon = new int[list.size()];

                    for(int i = 0; i < list.size(); i++)
                    {
                        m_ListOfNames[i] = list.get(i);
                        m_ListOfIcon[i] = R.drawable.ic_account_circle_50dp;
                    }
                    FINISH = true;
                    return true;
                }
            }
            catch (Exception e)
            {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            showProgress(false);
            if(success)
            {
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Task extends AsyncTask<Void, Void, Boolean>
    {
        Boolean Finish;

        private String Query;

        Task(String query)
        {
            Query = query;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(!success || !Finish)
            {
                Toast.makeText(ListaKlientowActivity.this,"Błąd podczas pobierania lub aktualizacji danych danych!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ListaKlientowActivity.this,"Pomyślnie usunięto użytkownika z bazy banych!", Toast.LENGTH_LONG).show();
            }
            try
            {
                showProgress(false);
            }
            catch (Exception ignored){}
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Finish = false;
            try
            {
                new ConnectToDataBase().Execute(Query);

                Thread.sleep(200);
                Finish = true;
                return Finish;
            }
            catch (Exception e)
            {
                Finish = false;
            }
            return false;
        }
    }
}
