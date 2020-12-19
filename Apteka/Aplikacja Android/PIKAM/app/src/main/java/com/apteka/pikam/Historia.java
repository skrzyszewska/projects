package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class Historia extends AppCompatActivity
{
    private class Lek
    {
        String Nazwa;
        Double Cena;
        Boolean Recepta;

        Lek(String nazwa, Double cena, Boolean recepta)
        {
            Nazwa = nazwa;
            Cena = cena;
            Recepta = recepta;
        }
    }

    private JSONArray HistoriaZamowien;
    private View m_Form;
    private View m_ProgressBar;

    private int ID;
    private boolean All;
    private Task m_Task;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
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
            All = intent.getBooleanExtra("ALL", false);
        }
        catch (Exception e)
        {
            Log.e("log_error", e.toString());
        }

        if(All)
        {
            setActionBar();
        }

        m_Form = findViewById(R.id.Historia_Lista);
        m_ProgressBar = findViewById(R.id.Historia_Lista_progress);

        if (HistoriaZamowien == null)
        {
            showProgress(true);
            m_Task = new Task();
            m_Task.execute((Void) null);
        }

        setLists();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onBackPressed();
        return true;
    }

    private void setActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Oczekujące zamówiania");
            actionBar.show();
        }
    }

    private Lek szukajLeku(int id)
    {
        if(AnonimMode.TabelaLekow != null)
        {
            try
            {
                for(int i = 0; i < AnonimMode.TabelaLekow.length(); i++)
                {
                    JSONObject obj = AnonimMode.TabelaLekow.getJSONObject(i);
                    if(obj.getInt("ID") == id)
                    {
                        return new Lek(obj.getString("Nazwa"), obj.getDouble("Cena"), obj.getString("NaRecepte").equals("TAK"));
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void setLists()
    {
        try
        {
            AdapterHistoryczny adapter;
            JSONArray tab;
            boolean IsAny = false;

            int index = 0;
            while(!m_Task.Finish)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("MSG", String.valueOf(index));
                index++;
            }

            if(HistoriaZamowien != null)
            {
                tab = HistoriaZamowien;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM HistoriaZamowien ORDER BY KlientID");
            }
            if(tab == null)
            {
                HistoriaZamowien = AnonimMode.HistoriaOperacji;
            }

            HashMap<String, ObiektHistoryczny> lista = new HashMap<>();

            if(tab != null)
            {
                for(int i = 0; i < tab.length(); i++)
                {
                    JSONObject obj = tab.getJSONObject(i);

                    if(All)
                    {
                        IsAny = true;
                        String Data = obj.getString("Data");
                        String Status = obj.getString("Status");

                        if(Status.equals("Oczekuje na akceptacje"))
                        {
                            if(lista.get(Data) == null)
                            {
                                lista.put(Data, new ObiektHistoryczny());
                            }

                            lista.get(Data).Data = Data;
                            lista.get(Data).Status = Status;
                            lista.get(Data).Ikonka.add(Setters.getImageRes(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Nazwa));
                            lista.get(Data).Nazwa.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Nazwa);
                            lista.get(Data).Cena.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Cena);
                            lista.get(Data).CenaRazem.add(obj.getDouble("Cena"));
                            lista.get(Data).Ilosc.add(obj.getInt("Ilosc"));
                            lista.get(Data).Razem += obj.getDouble("Cena") * obj.getInt("Ilosc");
                            lista.get(Data).Ilosc.add(obj.getInt("Ilosc"));
                            lista.get(Data).ID.add(obj.getInt("LekID"));
                            lista.get(Data).Recepta.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Recepta);
                        }
                    }
                    else
                    {
                        if(obj.getInt("KlientID") == ID)
                        {
                            IsAny = true;
                            String Data = obj.getString("Data");

                            if(lista.get(Data) == null)
                            {
                                lista.put(Data, new ObiektHistoryczny());
                            }
                            lista.get(Data).Data = Data;
                            lista.get(Data).Status = obj.getString("Status");
                            lista.get(Data).Ikonka.add(Setters.getImageRes(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Nazwa));
                            lista.get(Data).Nazwa.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Nazwa);
                            lista.get(Data).Cena.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Cena);
                            lista.get(Data).CenaRazem.add(obj.getDouble("Cena"));
                            lista.get(Data).Ilosc.add(obj.getInt("Ilosc"));
                            lista.get(Data).Razem += obj.getDouble("Cena") * obj.getInt("Ilosc");
                            lista.get(Data).ID.add(obj.getInt("LekID"));
                            lista.get(Data).Recepta.add(Objects.requireNonNull(szukajLeku(obj.getInt("LekID"))).Recepta);
                        }
                    }
                }
                if(IsAny)
                {
                    ListView listView = findViewById(R.id.Historia_Lista);
                    adapter = new AdapterHistoryczny(this, lista);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(Historia.this,"Brak zamówień w historii.", Toast.LENGTH_LONG).show();
                }
            }
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
                HistoriaZamowien = new ConnectToDataBase().Execute("SELECT * FROM HistoriaZamowien ORDER BY KlientID");
                Finish = HistoriaZamowien != null;
                return HistoriaZamowien != null;
            }
            catch (Exception e)
            {
                HistoriaZamowien = null;
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
                Toast.makeText(Historia.this,"Błąd podczas pobierania danych!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
        }
    }
}
