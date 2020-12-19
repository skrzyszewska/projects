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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


    public class Koszyk extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener{

            private MaterialSearchView m_MaterialSearchView;
            private String[] m_SearchPropositionList;
            String G;
            TextView t;
            TextView t2;
            int klientid;
            List<Integer> l;
            int wynik;
            static int aa[];
            Button button;
            Date cc;
            SimpleDateFormat df;
            String formattedDate;

        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.koszyk);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.koszyk_drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

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

            Intent intent2 = getIntent();
            klientid = intent2.getIntExtra("ID", 0);
            int dupa;
            t = (TextView) findViewById(R.id.suma);
            t2 = (TextView) findViewById(R.id.sumacalkowita);
            String suma;
            l = wrzuc_id(klientid);
            aa = new int[l.size()];
            wynik = 0;
            int b;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.GERMANY);
            cc = Calendar.getInstance().getTime();
            formattedDate = df.format(cc);
            // System.out.println("Current time => " + c);


            if (l.isEmpty()){
                Toast.makeText(this, "Twój koszyk jest pusty!", Toast.LENGTH_LONG).show();
            }
            else{
                for (int i = 0; i < l.size(); i++){

                    dupa = zlicz_ilosc(l.get(i), klientid);

                    String g = pobierz_nazwe_leku(l.get(i));

                    b = cena_dla_poszczegolnego(l.get(i));

                    aa[i] = b;
                    wynik = wynik + aa[i];

                    String c = Integer.toString(dupa);

                    String bb = g + " " + c + " " + "\n";
                    t.append(bb);
                }
                String ccc = Integer.toString(wynik);
                t2.append(ccc);

                Toast.makeText(this, "Cena za wszystko = " + wynik, Toast.LENGTH_LONG).show();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ListView listView = (ListView) findViewById(R.id.lista);
            List<PojedynczyLek> contacts = new ArrayList<PojedynczyLek>();
            LekiAdapter adapter = new LekiAdapter(this,
                    R.layout.minusiki, contacts);
            String nazwa_leku;

            try{
                JSONArray tab = new db_connect().getResults("SELECT * FROM Koszyk");

                for (int i = 0; i < tab.length(); i++){

                    JSONObject json_data = tab.getJSONObject(i);

                    int lek_id = json_data.getInt("LekID");
                    int Ilosc = json_data.getInt("Ilosc");
                    int Klient_id = json_data.getInt("KlientID");

                    nazwa_leku = pobierz_nazwe_leku(lek_id);

                    if (Klient_id == klientid){

                        String ilosc = Integer.toString(Ilosc);

                        contacts.add(new PojedynczyLek(nazwa_leku, ilosc));
                        listView.setAdapter(adapter);
                    }
                }

            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    String str = (String) ((TextView) view.findViewById(R.id.NazwaLeku)).getText();
                    int id_leku = pobierz_id_leku(str);

                    String query = "DELETE " + "FROM Koszyk "
                            + "WHERE KlientID =  '" + klientid + "'AND LekID = '" + id_leku + "'" + "LIMIT 1";
                    new db_connect().getResults(query);

                    view.setVisibility(View.GONE);

                    startActivity(getIntent());
                }
            });

            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int qq = pobierz_koszyk(klientid);
                    int d;

                    for (int i = 0; i < l.size(); i++){
                        String nazwa = pobierz_nazwe_leku(l.get(i));

                        int g = pobierz_ilosc_w_koszyku(nazwa, l.get(i));
                        int c = pobierz_ilosc_z_leku(l.get(i));
                        int cc = pobierz_cene(l.get(i));
                        int ggg = zlicz_ilosc(l.get(i), klientid);

                        if (g < c){
                            d = c - 1;
                            String query = "UPDATE" + " Leki SET " + "Ilosc = '" + d + "'" + "WHERE ID = '" + l.get(i) + "'";
                            new db_connect().getResults(query);

                            String query2 = "INSERT INTO" + " HistoriaZamowien " + "(KoszykID, KlientID, LekID, Cena, Ilosc, Data," +
                                    " AkceptujacyID, Status) "
                                    + "Values ('" + qq + "','" + klientid + "', '" + l.get(i) + "', '" + cc + "', '" + ggg + "'," +
                                    "'" + formattedDate + "','" + "NULL" + "','" + "Zrealizowane" + "')";
                            new db_connect().getResults(query2);
                        }
                        else{
                            Toast.makeText(Koszyk.this, "Nie ma wystarczającej ilości leku  " + nazwa, Toast.LENGTH_LONG).show();
                            break;
                        }
                        Toast.makeText(getApplicationContext(),
                                "Zrealizowano zamówienie!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

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
            else if (id == R.id.SingOut){
                Intent intent1 = new Intent(this,LoginActivity.class);

                String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + klientid + "'";
                new db_connect().getResults(query);

                this.startActivity(intent1);
                Toast.makeText(this, "Zostałeś wylogowany!", Toast.LENGTH_LONG).show();
            }
            else if (id == R.id.koszyk){
                Intent intent1 = new Intent(this,Koszyk.class);
                intent1.putExtra("ID",klientid);
                startActivity(intent1);
            }
            else if (id == R.id.nav_share){
                Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
            }
            else if (id == R.id.nav_send){
                Toast.makeText(this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
            }

            DrawerLayout drawer = findViewById(R.id.koszyk_drawer_layout);
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
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();
            return id == R.id.search || super.onOptionsItemSelected(item);
        }
        @Override
        public void onBackPressed(){
            DrawerLayout drawer = findViewById(R.id.klient_drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), KlientMode.class);
                intent.putExtra("ID", klientid);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "klientid" + klientid + " ", Toast.LENGTH_LONG)
                        .show();
            }
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

            } catch (JSONException e1) {
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
            } catch (JSONException e){
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
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        int zwroc_ilosc_z_leku(String nazwa, int d){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int id_ = json_data.getInt("ID");

                    if (d == id_){
                        int Ilosc = json_data.getInt("Ilosc");

                        return Ilosc;
                    }
                }
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        int pobierz_ilosc_w_koszyku(String nazwa, int d){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int id_ = json_data.getInt("ID");

                    if (d == id_){
                        int Ilosc = json_data.getInt("Ilosc");

                        return Ilosc;
                    }
                }
            } catch (JSONException e){
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
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return false;
        }

        String pobierz_nazwe_leku(int id){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int k3 = json_data.getInt("ID");
                    String nazwa = json_data.getString("Nazwa");

                    if (k3 == id){
                        return nazwa;
                    }
                }
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return "";
        }

        int zlicz_ilosc(int id, int id_klienta){
            int ilosc = 0;
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int id_ = json_data.getInt("LekID");
                    int id_k = json_data.getInt("KlientID");
                    if (id_ == id && id_klienta == id_k) {
                        ilosc = ilosc + 1;
                    }
                }
                return ilosc;
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        int cena_dla_wszystkich(int cena){
            int suma = 0;
            suma = suma + cena;
            return suma;
        }

        int cena_dla_poszczegolnego(int id){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
            int cena2 = 0;
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int id_ = json_data.getInt("ID");
                    int cena = json_data.getInt("Cena");

                    if (id == id_){
                        cena2 = cena2 + cena;
                    }
                }
                return cena2;
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        int pobierz_ilosc_z_leku(int id){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
            int cena2 = 0;
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int ilosc = json_data.getInt("Ilosc");
                    int id_ = json_data.getInt("ID");

                    if (id == id_){
                        return ilosc;
                    }
                }
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        List<Integer> wrzuc_id(int klientid){
            List<Integer> listIds = new ArrayList<Integer>();
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int id_ = json_data.getInt("KlientID");
                    int lek_id = json_data.getInt("LekID");

                    if (id_ == klientid){
                        listIds.add(lek_id);
                    }
                }
                return listIds;
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return listIds;
        }

        int pobierz_koszyk(int id){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Koszyk");
            try{
                for (int i = 0; i < tab2.length(); i++){
                    JSONObject json_data = tab2.getJSONObject(i);

                    int koszyk_id = json_data.getInt("KoszykID");
                    int klientid = json_data.getInt("KlientID");

                    if (klientid == id){
                        return koszyk_id;
                    }
                }
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

        int pobierz_cene(int id_leka){
            JSONArray tab2 = new db_connect().getResults("SELECT * FROM Leki");
            try{
                for (int i = 0; i < tab2.length(); i++){

                    JSONObject json_data = tab2.getJSONObject(i);

                    int id = json_data.getInt("ID");
                    int cena = json_data.getInt("Cena");

                    if (id_leka == id) {
                        return cena;
                    }
                }
            } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return 0;
        }

    }