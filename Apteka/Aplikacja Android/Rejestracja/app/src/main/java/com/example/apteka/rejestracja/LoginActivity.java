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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText L,P;
    private String Login1;
    private String Password1;
    private Boolean Jest;
    private Boolean Jest2;
    private MaterialSearchView m_MaterialSearchView;
    private String[] m_SearchPropositionList;
    public int id_klienta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button1 = (Button) findViewById(R.id.Rejestracja);
        Button  button2 = (Button) findViewById(R.id.Logowanie);
        Button button3 = (Button) findViewById(R.id.AnonimMode);
        L = (EditText) findViewById(R.id.LoginL);
        P = (EditText) findViewById(R.id.PasswordP);

            button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,RegisterActivity.class);
                    startActivity(intent);
                }
            });

            button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Login1 = L.getText().toString();
                    Password1 = P.getText().toString();

                    CheckIfExist();
                    int login=CheckIfExits2();

                    id_klienta = login;

                    if(Jest2 == false){
                       Toast.makeText(LoginActivity.this,"Zalogowales się!", Toast.LENGTH_LONG).show();

                        String query = "DELETE FROM " + " Koszyk " + "WHERE KlientID ='" + id_klienta + "'";
                        new db_connect().getResults(query);

                        Intent intent = new Intent(getApplicationContext(),KlientMode.class);

                        intent.putExtra("ID",id_klienta);

                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Zły login lub hasło!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,AnonimMode.class);
                    startActivity(intent);
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

    public void CheckIfExist(){
        try {
            JSONArray tab = new db_connect().getResults("SELECT Login,Haslo FROM Aptekarze ");
            //JSONArray tab = new db_connect().getResults("SELECT Login FROM Aptekarze ");
            //JSONArray tab2 = new db_connect().getResults("SELECT Login FROM Klienci");
            for (int i = 0; i < tab.length(); i++){
                JSONObject json_data = tab.getJSONObject(i);

                String login = json_data.getString("Login");
                String password = json_data.getString("Haslo");

                String name = MD5(Password1);

                if(login.equals(Login1) && name.equals(password)){
                    Jest = false;
                    break;
                }
                else
                    Jest = true;
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public int CheckIfExits2(){
        try{
            JSONArray tab = new db_connect().getResults("SELECT Login,Haslo,ID FROM Klienci ");
            for (int i = 0; i < tab.length(); i++) {
                JSONObject json_data = tab.getJSONObject(i);
                String login = json_data.getString("Login");
                String password = json_data.getString("Haslo");
                int ID = json_data.getInt("ID");
                String name = MD5(Password1);

                if(login.equals(Login1) && name.equals(password)){
                    Jest2 = false;
                    id_klienta = ID;
                    return id_klienta;
                }
                else{
                    Jest2 = true;
                }
            }
        } catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return 0;
}

    private String MD5(String s){
        byte[] data;
        try {
            data = s.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms;
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        return;
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
            startActivity(intent);
        }
        else if (id == R.id.SingIn){
            Context context;
            context = getApplicationContext();
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share){
            Toast.makeText(LoginActivity.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_send){
            Toast.makeText(LoginActivity.this,"Funkcja niedostepna!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
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

    /*
    private Dialog createAlertDialogWithButtons(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Wychodzę");
                LoginActivity.this.finish();
            }
        });

        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton){
                showToast("Anulowaleś wyjście");
            }
            private void showToast(String s){
            }
            });
        return dialogBuilder.create();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }
    public void onCancel(DialogInterface dialog){
        createAlertDialogWithButtons();
    }
    */
}

