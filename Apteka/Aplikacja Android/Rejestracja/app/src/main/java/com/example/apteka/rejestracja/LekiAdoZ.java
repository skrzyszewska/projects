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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LekiAdoZ extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView lekid;
    ListView listView;
    String nazwa_leku_textview;
    boolean czy_rozny;
    boolean dupa;
    int klientid;

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = (ListView) findViewById(R.id.lista);
        List<PojedynczyLek> contacts = new ArrayList<PojedynczyLek>();
        LekiAdapter adapter = new LekiAdapter(this, R.layout.plusiki, contacts);

        try{
            JSONArray tab = new db_connect().getResults("SELECT * FROM Leki ORDER BY CENA DESC");

            for (int i = 0; i < tab.length(); i++){
                JSONObject json_data = tab.getJSONObject(i);

                String nazwa = json_data.getString("Nazwa");
                Integer cena = json_data.getInt("Cena");
                String cena2 = Integer.toString(cena);

                contacts.add(new PojedynczyLek(nazwa, cena2));
                listView.setAdapter(adapter);
            }
        } catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Integer koszyk_id;

            String str =(String) ((TextView) view.findViewById(R.id.NazwaLeku)).getText();

            boolean czy_jest =czy_klient_id_jest_w_koszyku(klientid);
            int id_leku =pobierz_id_leku(str);
            int Najwieksze_koszyk_id_historia = get_najwiekszy_id_historiazamowien();
            /* Toast.makeText(getApplicationContext(),
                    "pnkihistoria " + Najwieksze_koszyk_id_historia, Toast.LENGTH_LONG)
                    .show();
                    */
            int Najwieksze_koszyk_id_z_koszyk = get_najwiekszy_id_koszyk();
            /* Toast.makeText(getApplicationContext(),
                    "z koszyk " + Najwieksze_koszyk_id_z_koszyk, Toast.LENGTH_LONG)
                    .show();
                    */
           /* Toast.makeText(getApplicationContext(),
                   "poka id lek " + Id_lek, Toast.LENGTH_LONG)
                   .show();
                   */
            int pobierz_id_z_leku = pobierz_id_leku(str);
            int ilosc_z_leku = zwroc_ilosc_z_leku(str, pobierz_id_z_leku);


            if (czy_jest == false && ilosc_z_leku > 0){

                if (Najwieksze_koszyk_id_historia + 1 > Najwieksze_koszyk_id_z_koszyk + 1){
                    koszyk_id = Najwieksze_koszyk_id_historia + 1;

                    String query="INSERT INTO" + " Koszyk " + "(KoszykID, KlientID, LekID, Ilosc) "
                            + "Values ('" + koszyk_id + "', '" + klientid + "', '" + id_leku + "', '" + 1 + "')";

                    new db_connect().getResults(query);

                    Toast.makeText(getApplicationContext(),"Dodano lek do koszyka", Toast.LENGTH_LONG).show();

                }
                else{
                    koszyk_id = Najwieksze_koszyk_id_z_koszyk + 1;
                  /*  Toast.makeText(getApplicationContext(),
                            "dup2a " + koszyk_id, Toast.LENGTH_LONG)
                            .show();
                            */
                    String query="INSERT INTO" + " Koszyk " + "(KoszykID, KlientID, LekID, Ilosc) "
                            + "Values ('" + koszyk_id + "', '" + klientid + "', '" + id_leku + "', '" + 1 + "')";
                    new db_connect().getResults(query);

                    Toast.makeText(getApplicationContext(),"Dodano lek do koszyka", Toast.LENGTH_LONG).show();
                }
            }
            if (czy_jest == true){
               // int pobierz_id_z_leku = pobierz_id_leku(str);
               // int ilosc_z_leku = zwroc_ilosc_z_leku(str, pobierz_id_z_leku);
                int pobierz_ilosc_z_koszyka = pobierz_ilosc_w_koszyku(str, klientid);
                boolean czy_jest_id_w_bazie = czy_id_leku_jest_w_bazie(pobierz_id_z_leku);
                boolean czy_klient_jest_w_bazie_ = czy_klient_jest_w_bazie(klientid);
                int b;

                JSONArray tab3 = new db_connect().getResults("SELECT * FROM Koszyk");
                try{
                    for (int j = 0; j < tab3.length(); j++){
                        JSONObject json_data2 = tab3.getJSONObject(j);
                        Integer klient_id2 = json_data2.getInt("KlientID");
                        Integer lek_id2 = json_data2.getInt("LekID");
                        Integer ilosc2 = json_data2.getInt("Ilosc");
                        Integer koszyk_id_ = json_data2.getInt("KoszykID");

                        if(klient_id2 == klientid && 0 == ilosc_z_leku){
                            Toast.makeText(getApplicationContext(),"Podany lek nie jest dostępny", Toast.LENGTH_LONG).show();
                            break;
                        }

                        if(lek_id2 != pobierz_id_z_leku && klient_id2 == klientid && ilosc2 < ilosc_z_leku){
                            String query = "INSERT INTO" + " Koszyk " + "(KoszykID, KlientID, LekID, Ilosc) "
                                        + "Values ('" + koszyk_id_ + "', '" + klientid + "', '" + id_leku + "', '" + 1 + "')";
                            new db_connect().getResults(query);

                            Toast.makeText(getApplicationContext(),"Dodano lek do koszyka", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                } catch (JSONException e){
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }
                }
        });

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
    }


    int get_najwiekszy_id_historiazamowien(){
        try{
            JSONArray tab3 = new db_connect().getResults("SELECT * FROM HistoriaZamowien");
            JSONObject json_data2 = tab3.getJSONObject(0);
            Integer najwiekszy = json_data2.getInt("KoszykID");

            for (int i = 1; i < tab3.length(); i++){
                JSONObject json_data = tab3.getJSONObject(i);
                Integer Najwieksze_koszyk_id_historia = json_data.getInt("KoszykID");

                if (Najwieksze_koszyk_id_historia > najwiekszy)
                    najwiekszy = Najwieksze_koszyk_id_historia;
            }
            return najwiekszy;
        } catch (JSONException e1){
            e1.printStackTrace();
        }
        return 0;
    }

    int get_najwiekszy_id_koszyk(){
        try{
            JSONArray tab3 = new db_connect().getResults("SELECT * FROM Koszyk");
            JSONObject json_data2 = tab3.getJSONObject(0);
            Integer najwiekszy = json_data2.getInt("KoszykID");

            for (int i = 1; i < tab3.length(); i++){
                JSONObject json_data = tab3.getJSONObject(i);
                Integer Najwieksze_koszyk_id_historia = json_data.getInt("KoszykID");

                if (Najwieksze_koszyk_id_historia > najwiekszy)
                    najwiekszy = Najwieksze_koszyk_id_historia;
            }
            return najwiekszy;
        } catch (JSONException e1){
            e1.printStackTrace();
        }
        return 0;
    }

    boolean czy_klient_id_jest_w_koszyku(int klientid){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                Integer klient_id = json_data.getInt("KlientID");

                if (klient_id == klientid){
                   return true;
                }
            }
        }catch (JSONException e){
                    Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return false;
    }

    int pobierz_id_leku(String Naazwa){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                String nazwa = json_data.getString("Nazwa");

                if (Naazwa.equals(nazwa)){
                    Integer lek_id = json_data.getInt("ID");
                    return lek_id;
                }
            }
        }catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return 0;
    }

    int zwroc_ilosc_z_leku(String nazwa,int d){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                int id_ = json_data.getInt("ID");

                if (d == id_){
                    int Ilosc= json_data.getInt("Ilosc");
                    return Ilosc;
                }
            }
        }catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return 0;
    }

    int pobierz_ilosc_w_koszyku(String nazwa,int d){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                int id_ = json_data.getInt("ID");

                if (d == id_){
                    int Ilosc= json_data.getInt("Ilosc");
                    return Ilosc;
                }
            }
        }catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return 0;
    }

    boolean czy_id_leku_jest_w_bazie(int id){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                int k3 = json_data.getInt("LekID");

                if (k3 == id){
                    return true;
                }
            }
        }catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return false;
    }

    boolean czy_klient_jest_w_bazie(int id){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(i);
                int k3 = json_data.getInt("KlientID");

                if (k3 == id){
                    return true;
                }
            }
        }catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.leki_a_z_drawer_layout);
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

        if (id == R.id.HomePage){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,AnonimMode.class);
            startActivity(intent);
        }
        else if (id == R.id.Medicines){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LekiCenaSort.class);
            startActivity(intent);
        }
        else if (id == R.id.Koszyk){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,Koszyk.class);
            intent.putExtra("ID",klientid);
            startActivity(intent);
        }
        else if (id == R.id.SingIn){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.SingOut){
            Intent intent1 = new Intent(this,LoginActivity.class);

            String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + klientid + "'";
            new db_connect().getResults(query);

            klientid = 0;
            this.startActivity(intent1);
            Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_share){
            Toast.makeText(LekiAdoZ.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send){
            Toast.makeText(LekiAdoZ.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.leki_a_z_drawer_layout);
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

}




