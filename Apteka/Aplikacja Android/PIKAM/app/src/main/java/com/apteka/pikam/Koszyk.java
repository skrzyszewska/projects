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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class Koszyk extends AppCompatActivity
{
    private Task Dodaj = null;
    private Task Usun = null;

    private int[] m_ListOfCounts;
    private int[] m_ListOfPrices;
    private String[] m_ListOfNames;

    private ListView m_ListView;

    private boolean tenSamKoszyk = false;
    private boolean[] m_ListOfBools;
    private int m_NrZamowienia, m_IloscLeku = 0;
    private String header;

    private JSONArray ZawartoscKoszyka;
    private JSONArray HistoriaZamowien;
    private JSONArray TabelaLekow;

    private View m_Form;
    private View m_ProgressBar;

    private Lek m_Lek;

    public int m_ID;
    public String m_Imie, m_Nazwisko;
    public static ArrayList<Integer> m_Koszyk;
    public static int m_IDKoszyka = -1;
    private boolean isEmpty = false;

    public Koszyk()
    {
        Intent intent = getIntent();
        try
        {
            m_Form = findViewById(R.id.koszyk_form);
            m_ProgressBar = findViewById(R.id.koszyk_progress);

            if(setTable())
            {
                m_Koszyk = new ArrayList<>();
                m_ID = intent.getIntExtra("ID", 0);
                m_Imie = intent.getStringExtra("Imie");
                m_Nazwisko = intent.getStringExtra("Nazwisko");
                if(m_IDKoszyka == -1)
                {
                    m_IDKoszyka = NastepnyNumerKoszyka();
                }
            }
        }
        catch (Exception e)
        {
            Log.e("log_error", e.toString());
        }
    }

    public Koszyk(int id, String imie, String nazwisko)
    {
        try
        {
            m_Form = findViewById(R.id.koszyk_form);
            m_ProgressBar = findViewById(R.id.koszyk_progress);
        } catch (Exception ignored) {}

        if(setTable())
        {
            m_ID = id;
            m_Imie = imie;
            m_Nazwisko = nazwisko;
            m_IDKoszyka = NastepnyNumerKoszyka();
        }
     }

    public boolean DodajDoKoszyka(int id, int ilosc)
    {
        if(setTable())
        {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SzukajDostepnegoLeku(id);

            if(m_IDKoszyka == -1)
            {
                m_IDKoszyka = NastepnyNumerKoszyka();
            }

            if (m_Lek != null) {
                return sendMessage(id, ilosc);
            }

            if(setTable())
            {
                SzukajDostepnegoLeku(id);
            }
        }
        return m_Lek != null && sendMessage(id, ilosc);
    }

    private boolean sendMessage(int id, int ilosc)
    {
        if(SzukajLekuWKoszyku(id))
        {
            String queryDelete = "DELETE FROM Koszyk WHERE KlientID =  '" + m_ID + "'AND LekID = '" + id + "'";
            Usun = new Task(queryDelete, 0);
            Usun.execute();

            int ilo = m_IloscLeku + ilosc;
            String query = "INSERT INTO Koszyk (KoszykID, KlientID, LekID, Ilosc) Values ('" + m_IDKoszyka +"', '" + m_ID + "', '" + id + "', '" + ilo + "')";
            Dodaj = new Task(query, 0);
            Dodaj.execute();
            m_Lek = null;
            return Dodaj != null && Usun != null;
        }
        else
        {
            String query = "INSERT INTO Koszyk (KoszykID, KlientID, LekID, Ilosc) Values ('" + m_IDKoszyka +"', '" + m_ID + "', '" + id + "', '" + ilosc + "')";
            Dodaj = new Task(query, 0);
            Dodaj.execute();
            m_Lek = null;
            return Dodaj != null;
        }
    }

    public boolean UsunLekZKoszyka(int id)
    {
        if(setTable())
        {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(SzukajLekuWKoszyku(id))
            {
                String queryDelete = "DELETE FROM Koszyk WHERE KlientID =  '" + m_ID + "'AND LekID = '" + id + "'" + "LIMIT 1";
                Usun = new Task(queryDelete, 0);
                Usun.execute();

                boolean straznik = false;
                for(int i = m_Koszyk.size() - 1; i >= 0; i--)
                {
                    if (m_Koszyk.get(i) == id)
                    {
                        m_Koszyk.remove(m_Koszyk.get(i));
                        straznik = true;
                    }
                }

                return straznik && Usun != null;
            }
        }
        return false;
    }

    public boolean UsunKoszyk()
    {
        String queryDelete = "DELETE FROM Koszyk WHERE KlientID =  '" + m_ID + "'";
        Usun = new Task(queryDelete, 0);
        Usun.execute();
        if(m_Koszyk != null)
        {
            m_Koszyk.clear();
        }
        return Usun != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koszyk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        m_Form = findViewById(R.id.koszyk_form);
        m_ProgressBar = findViewById(R.id.koszyk_progress);

        showProgress(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(m_IDKoszyka < 1)
        {
            m_IDKoszyka = NastepnyNumerKoszyka();
        }

        trySetProp();
        if(setTable())
        {
            header = "Koszyk[" + m_NrZamowienia + "]: " + m_Imie + " " + m_Nazwisko;

            setActionBar(header);
            m_ListView = findViewById(R.id.List);
            m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,PojedynczyLekActivity.class);
                    intent.putExtra("Name", m_ListOfNames[position]);
                    intent.putExtra("NR", m_NrZamowienia);
                    intent.putExtra("ID", m_ID);
                    intent.putExtra("Imie", m_Imie);
                    intent.putExtra("Nazwisko", m_Nazwisko);
                    startActivity(intent);
                }
            });

            Button zamow = findViewById(R.id.zamow_Button);
            zamow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(setTable())
                    {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        if(isEmpty)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Koszyk.this);
                            builder.setMessage("Koszyk jest pusty!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return;
                        }

                        boolean straznik = false;
                        for (boolean m_ListOfBool : m_ListOfBools)
                        {
                            if (m_ListOfBool)
                            {
                                straznik = true;
                                break;
                            }
                        }


                        if(straznik)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Koszyk.this);
                            builder.setMessage("Twoje zamówienie zawiera leki na recepte, są one niemożliwe do zamówienia przez aplikacje mobilną P.I.K.A.M. \nCzy mimo to chesz zamówić pozostałem leki? \n*Leki zostaną wysłane na dane adresowe").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    boolean wyswietlPodsumowanie = true;

                                    for(int j = 0; j < TabelaLekow.length(); j++)
                                    {
                                        for(int i = 0; i < m_ListOfNames.length; i++)
                                        {
                                            try
                                            {
                                                JSONObject obj = TabelaLekow.getJSONObject(j);
                                                String name = obj.getString("Nazwa");
                                                if(m_ListOfNames[i].equals(name))
                                                {
                                                    int ilosc = obj.getInt("Ilosc");
                                                    if (m_ListOfCounts[i] <= ilosc)
                                                    {
                                                        SimpleDateFormat FormatDaty = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.GERMANY);
                                                        Date AktualnaData = Calendar.getInstance().getTime();

                                                        int id = obj.getInt("ID");
                                                        int ilo = ilosc -  m_ListOfCounts[i];
                                                        String query = "UPDATE" + " Leki SET " + "Ilosc = '" + ilo + "'" + "WHERE ID = '" + id + "'";
                                                        Task Update = new Task(query, 0);
                                                        Update.execute();

                                                        if(m_ListOfBools[i])
                                                        {
                                                            Toast.makeText(Koszyk.this, "Zamawianie leków na recepte jest niedostepne w aplikacji mobilnej. \nZamówienie na lek: "+ m_ListOfNames[i] + " nie zostanie zrealizowane.", Toast.LENGTH_LONG).show();
                                                            wyswietlPodsumowanie = false;
                                                        }
                                                        else
                                                        {
                                                            query = "INSERT INTO" + " HistoriaZamowien " + "(KoszykID, KlientID, LekID, Cena, Ilosc, Data, Status, AkceptujacyID) "
                                                                    + "Values ('" + m_IDKoszyka + "','" + m_ID + "', '" + id + "', '" + m_ListOfPrices[i] + "', '" + m_ListOfCounts[i] + "'," +
                                                                    "'" + FormatDaty.format(AktualnaData) + "','" + "Zrealizowane" + "', '" + m_ID + "')";
                                                            Dodaj = new Task(query, 0);
                                                            Dodaj.execute();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Koszyk.this, "Nie ma wystarczającej ilości leku  " + m_ListOfNames[i], Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    if(UsunKoszyk())
                                    {
                                        if(wyswietlPodsumowanie)
                                        {
                                            Toast.makeText(getApplicationContext(),"Zrealizowano zamówienie!", Toast.LENGTH_LONG).show();
                                        }

                                        Intent intent;
                                        Context context = getApplicationContext();
                                        intent = new Intent(context, KlientMode.class);
                                        intent.putExtra("ID", m_ID);
                                        intent.putExtra("Imie", m_Imie);
                                        intent.putExtra("Nazwisko", m_Nazwisko);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Wystapił błąd przy realizacji zamówienia", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Koszyk.this);
                            builder.setMessage("Czy chcesz zamówić leki z koszyka? \n*Leki zostaną wysłane na dane adresowe").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    boolean wyswietlPodsumowanie = true;

                                    for(int j = 0; j < TabelaLekow.length(); j++)
                                    {
                                        for(int i = 0; i < m_ListOfNames.length; i++)
                                        {
                                            try
                                            {
                                                JSONObject obj = TabelaLekow.getJSONObject(j);
                                                String name = obj.getString("Nazwa");
                                                if(m_ListOfNames[i].equals(name))
                                                {
                                                    int ilosc = obj.getInt("Ilosc");
                                                    if (m_ListOfCounts[i] <= ilosc)
                                                    {
                                                        SimpleDateFormat FormatDaty = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.GERMANY);
                                                        Date AktualnaData = Calendar.getInstance().getTime();

                                                        int id = obj.getInt("ID");
                                                        int ilo = ilosc -  m_ListOfCounts[i];
                                                        String query = "UPDATE" + " Leki SET " + "Ilosc = '" + ilo + "'" + "WHERE ID = '" + id + "'";
                                                        Task Update = new Task(query, 0);
                                                        Update.execute();

                                                        query = "INSERT INTO" + " HistoriaZamowien " + "(KoszykID, KlientID, LekID, Cena, Ilosc, Data, Status, AkceptujacyID) "
                                                                + "Values ('" + m_IDKoszyka + "','" + m_ID + "', '" + id + "', '" + m_ListOfPrices[i] + "', '" + m_ListOfCounts[i] + "'," +
                                                                "'" + FormatDaty.format(AktualnaData) + "','" + "Zrealizowane" + "', '" + m_ID + "')";
                                                        Dodaj = new Task(query, 0);
                                                        Dodaj.execute();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Koszyk.this, "Nie ma wystarczającej ilości leku  " + m_ListOfNames[i], Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    if(UsunKoszyk())
                                    {
                                        if(wyswietlPodsumowanie)
                                        {
                                            Toast.makeText(getApplicationContext(),"Zrealizowano zamówienie!", Toast.LENGTH_LONG).show();
                                        }

                                        Intent intent;
                                        Context context = getApplicationContext();
                                        intent = new Intent(context, KlientMode.class);
                                        intent.putExtra("ID", m_ID);
                                        intent.putExtra("Imie", m_Imie);
                                        intent.putExtra("Nazwisko", m_Nazwisko);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Wystapił błąd przy realizacji zamówienia", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }
            });
        }
        if(isEmpty)
        {
            Toast.makeText(getApplicationContext(),"Koszyk jest pusty!", Toast.LENGTH_LONG).show();
        }
    }

    /*
    query = "INSERT INTO" + " HistoriaZamowien " + "(KoszykID, KlientID, LekID, Cena, Ilosc, Data, Status, AkceptujacyID) "
            + "Values ('" + m_IDKoszyka + "','" + m_ID + "', '" + id + "', '" + m_ListOfPrices[i] + "', '" + m_ListOfCounts[i] + "'," +
            "'" + FormatDaty.format(AktualnaData) + "','" + "Oczekuje na akceptacje" + ", '0')";
    Dodaj = new Task(query, 0);
    Dodaj.execute();
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        Context context = getApplicationContext();
        intent = new Intent(context, KlientMode.class);
        intent.putExtra("ID", m_ID);
        intent.putExtra("Imie", m_Imie);
        intent.putExtra("Nazwisko", m_Nazwisko);
        startActivity(intent);
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

    private void trySetProp()
    {
        if(m_ID == 0 || m_Imie == null || m_Imie.length() == 0 || m_Nazwisko == null || m_Nazwisko.length() == 0)
        {
            Intent intent = getIntent();
            try
            {
                m_ID = intent.getIntExtra("ID", 0);
                m_Imie = intent.getStringExtra("Imie");
                m_Nazwisko = intent.getStringExtra("Nazwisko");
            }
            catch (Exception e)
            {
                Log.e("log_error", e.toString());
            }
        }
    }

    private void setNrZamowienia()
    {
        try
        {
            JSONArray tab;
            if(HistoriaZamowien != null)
            {
                tab = HistoriaZamowien;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM HistoriaZamowien ORDER BY KoszykID ASC");
            }

            ArrayList<Integer> koszykId = new ArrayList<>();
            for(int i = 0; i < tab.length(); i++)
            {
                JSONObject json_data = tab.getJSONObject(i);
                trySetProp();
                if(json_data.getInt("KlientID") == m_ID)
                {
                    koszykId.add(json_data.getInt("KoszykID"));
                }
            }
            HashSet<Integer> hashSet = new HashSet<>(koszykId);

            m_NrZamowienia = hashSet.size();
            if(!tenSamKoszyk)
            {
                m_NrZamowienia++;
            }
        }
        catch (Exception e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }

    private boolean SzukajLekuWKoszyku(int id)
    {
        try
        {
            JSONArray tab;
            if(ZawartoscKoszyka != null)
            {
                tab = ZawartoscKoszyka;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Koszyk");
            }
            m_IloscLeku = 0;
            for(int i = 0; i < tab.length(); i++)
            {
                JSONObject json_data = tab.getJSONObject(i);
                int m_K_id = json_data.getInt("KlientID");
                trySetProp();
                if(m_K_id == AnonimMode.m_Koszyk.m_ID)
                {
                    int m_id = json_data.getInt("LekID");
                    if(m_id == id)
                    {
                        m_IloscLeku += json_data.getInt("Ilosc");
                    }
                }

            }
            if(m_IloscLeku > 0)
                return true;
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
        return false;
    }

    private int NastepnyNumerKoszyka()
    {
        try
        {
            JSONArray tab, tab2;
            if(HistoriaZamowien != null)
            {
                tab = HistoriaZamowien;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM HistoriaZamowien ORDER BY KoszykID DESC");
            }
            if(ZawartoscKoszyka != null)
            {
                tab2 = ZawartoscKoszyka;
            }
            else
            {
                tab2 = new ConnectToDataBase().Execute("SELECT * FROM Koszyk ORDER BY KoszykID DESC");
            }
            if(tab2.length() > 0)
            {
                for(int i = 0; i < tab2.length(); i++)
                {
                    JSONObject json_data = tab2.getJSONObject(i);
                    trySetProp();
                    if(json_data.getInt("KlientID") == m_ID)
                    {
                        tenSamKoszyk = true;
                        return json_data.getInt("KoszykID");
                    }
                }
            }
            if(tab.length() > 0 )
            {
                JSONObject json_data = tab.getJSONObject(0);
                return json_data.getInt("KoszykID") + 1;
            }
            return 1;
        }
        catch(Exception e)
        {
            if(ZawartoscKoszyka != null)
            {
                return NastepnyNumerKoszyka();
            }
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
        return -1;
    }

    private void SzukajDostepnegoLeku(int id)
    {
        try
        {
            JSONArray tab;
            if(TabelaLekow != null)
            {
                tab = TabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki");
            }

            for(int i=0;i<tab.length();i++)
            {
                JSONObject json_data = tab.getJSONObject(i);
                int Id = json_data.getInt("ID");
                int ilosc = json_data.getInt("Ilosc");
                if(id == Id && ilosc > 0)
                {
                    String nazwa = json_data.getString("Nazwa");
                    int cena = json_data.getInt("Cena");
                    String naRecepte = json_data.getString("NaRecepte");
                    m_Lek =  new Lek(id, nazwa, cena, ilosc, naRecepte.equals("TAK"));
                    return;
                }
            }
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
        m_Lek = null;
    }

    private void setLists()
    {
        showProgress(true);

        Adapter adapter;
        try
        {
            JSONArray tab, tabK;
            if(TabelaLekow != null)
            {
                tab = TabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki");
            }
            if(ZawartoscKoszyka != null)
            {
                tabK = ZawartoscKoszyka;
            }
            else
            {
                tabK = new ConnectToDataBase().Execute("SELECT * FROM Koszyk");
            }

            boolean jestCosWKoszyku = false;
            for(int i = 0; i < tabK.length(); i++)
            {
                JSONObject obj = tabK.getJSONObject(i);
                if(obj.getInt("KlientID") == m_ID)
                {
                    jestCosWKoszyku = true;
                }
            }

            if(jestCosWKoszyku)
            {
                int IloscLekowWKoszyku = 0;
                ArrayList<Integer> smallList = new ArrayList<>();
                ArrayList<Integer> smallListCount = new ArrayList<>();
                m_Koszyk = new ArrayList<>();
                for(int i = 0; i < tabK.length(); i++)
                {
                    JSONObject json_data = tabK.getJSONObject(i);
                    if(json_data.getInt("KlientID") == m_ID)
                    {
                        IloscLekowWKoszyku++;
                        smallList.add(json_data.getInt("LekID"));
                        smallListCount.add(json_data.getInt("Ilosc"));
                        m_Koszyk.add(json_data.getInt("LekID"));
                    }
                }

                m_ListOfNames = new String[IloscLekowWKoszyku];
                int[] m_ListOfIcon = new int[IloscLekowWKoszyku];
                m_ListOfPrices = new int[IloscLekowWKoszyku];
                m_ListOfCounts = new int[IloscLekowWKoszyku];
                m_ListOfBools = new boolean[IloscLekowWKoszyku];

                int suma = 0;
                for(int i=0; i<tab.length(); i++)
                {
                    JSONObject json_data = tab.getJSONObject(i);
                    for(int j = 0; j < IloscLekowWKoszyku; j++)
                    {
                        if(json_data.getInt("ID") == smallList.get(j))
                        {
                            m_ListOfCounts[j] = smallListCount.get(j);
                            m_ListOfPrices[j] = json_data.getInt("Cena") * m_ListOfCounts[j];
                            m_ListOfBools[j] = json_data.getString("NaRecepte").equals("TAK");
                            m_ListOfNames[j] = json_data.getString("Nazwa");
                            Setters.setImageTab(m_ListOfIcon, j, m_ListOfNames[j]);
                            suma += m_ListOfPrices[j];
                        }
                    }
                }
                m_ListView = findViewById(R.id.List);
                adapter = new Adapter(this, m_ListOfIcon, m_ListOfNames, m_ListOfPrices, m_ListOfCounts, m_ListOfBools);
                m_ListView.setAdapter(adapter);

                setActionBar(header + " Razem: " + suma + " zl");
            }
            else
            {
                isEmpty = true;
            }
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());

            if(TabelaLekow != null && ZawartoscKoszyka != null)
            {
                setLists();
            }

            e.printStackTrace();
        }
    }

    private boolean setTable()
    {
        Task pobierz;

        pobierz = new Task("SELECT * FROM HistoriaZamowien ORDER BY KoszykID DESC", 1);
        pobierz.execute((Void) null);

        pobierz = new Task("SELECT * FROM Koszyk ORDER BY KoszykID DESC", 2);
        pobierz.execute((Void) null);

        pobierz = new Task("SELECT * FROM Leki", 3);
        pobierz.execute((Void) null);

        return true;
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
    class Task extends AsyncTask<Void, Void, Boolean>
    {
        Boolean Finish;

        private int TypOperacji;
        private String Query;

        Task(String query, int type)
        {
            Query = query;
            TypOperacji = type;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(!success || !Finish)
            {
                Toast.makeText(Koszyk.this,"Błąd podczas pobierania danych!", Toast.LENGTH_LONG).show();
            }
            else
            {
                try
                {
                    setNrZamowienia();
                    header = "Koszyk[" + m_NrZamowienia + "]: " + m_Imie + " " + m_Nazwisko;
                    setActionBar(header);
                    setLists();
                }
                catch (Exception ignored){}
                Dodaj = Usun  = null;
            }
            try
            {
                showProgress(false);
            }catch (Exception ignored){}
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Finish = false;
            try
            {
                if(TypOperacji == 0)
                {
                    new ConnectToDataBase().Execute(Query);
                    Finish = true;
                }
                if (TypOperacji == 1)
                {
                    HistoriaZamowien = new ConnectToDataBase().Execute(Query);
                    Finish = HistoriaZamowien != null;
                }
                if (TypOperacji == 2)
                {
                    ZawartoscKoszyka = new ConnectToDataBase().Execute(Query);
                    Finish = ZawartoscKoszyka != null;
                }
                if (TypOperacji == 3)
                {
                    TabelaLekow = new ConnectToDataBase().Execute(Query);
                    Finish = TabelaLekow != null;
                }
                return true;
            }
            catch (Exception e)
            {
                if (TypOperacji == 1)
                {
                    HistoriaZamowien = null;
                }
                if (TypOperacji == 2)
                {
                    ZawartoscKoszyka = null;
                }
                if (TypOperacji == 3)
                {
                    TabelaLekow = null;
                }
                Finish = false;
            }

            return false;
        }

        @Override
        protected void onCancelled()
        {
            Dodaj = Usun = null;
        }
    }

    static class Lek
    {
        int ID, Ilosc, Cena;
        String Nazwa;
        Boolean Recepta;
        Lek(int id, String nazwa, int cena, int ilosc, boolean recepta)
        {
            ID = id;
            Nazwa = nazwa;
            Cena = cena;
            Ilosc = ilosc;
            Recepta = recepta;
        }
    }
}
