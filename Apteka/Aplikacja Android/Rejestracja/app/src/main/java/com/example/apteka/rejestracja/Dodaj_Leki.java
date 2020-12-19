package com.example.apteka.rejestracja;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Dodaj_Leki extends AppCompatActivity{
    String Nazwa1;
    String Grupa1;
    String Zdjecie1;
    String Sklad1;
    String Opis1;
    String Ilosc_String;
    String Cena_String;
    int Cena1;
    int Ilosc1;
    String String_Recepte;
    EditText N,G,Z,S,O,I,C,NR;
    Boolean CheckEditTextEmpty;
    Boolean Jest2;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_leki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.klient_toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button1 = (Button) findViewById(R.id.Dodaj_Lek);

        N = (EditText) findViewById(R.id.Nazwa);
        G = (EditText) findViewById(R.id.Grupa);
        Z = (EditText) findViewById(R.id.Zdjecie);
        S = (EditText) findViewById(R.id.Skład);
        O = (EditText) findViewById(R.id.Opis);
        I = (EditText) findViewById(R.id.Ilosc);
        C = (EditText) findViewById(R.id.Cena);
        G = (EditText) findViewById(R.id.Grupa);
        NR = (EditText) findViewById(R.id.Na_receptę);

        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Nazwa1 = N.getText().toString();
                Grupa1 = G.getText().toString();
                Sklad1 = S.getText().toString();
                Opis1 = O.getText().toString();
                Ilosc_String = I.getText().toString();
                Cena_String = C.getText().toString();
                String_Recepte = NR.getText().toString();
                Zdjecie1 = Z.getText().toString();

                CheckIfExits2();

                CheckEditTextIsEmptyOrNot(Nazwa1,Grupa1,Zdjecie1,Sklad1,Opis1,Ilosc_String,Cena_String,String_Recepte);


               if (CheckEditTextEmpty == true && Jest2 == true){
                   Ilosc1 = Integer.parseInt(Ilosc_String);
                   Cena1 = Integer.parseInt(Cena_String);

                    String query = "INSERT INTO" + " Leki " + "(Nazwa, Grupa, Sklad, Opis, Ilosc, Cena,NaRecepte) "
                            + "Values ('" + Nazwa1 + "', '" + Grupa1 + "', '" + Sklad1 + "', '" +
                            Opis1 + "','" + Ilosc1 + "','" + Cena1 + "','" + String_Recepte + "' )";

                    new db_connect().getResults(query);

                    Toast.makeText(Dodaj_Leki.this, "Lek został dodany!", Toast.LENGTH_LONG).show();

                }
                if(Jest2 == false && CheckEditTextEmpty == false){
                    Toast.makeText(Dodaj_Leki.this, "Podany lek istnieje!", Toast.LENGTH_LONG).show();
                    Toast.makeText(Dodaj_Leki.this, "Wypelnij wszystkie pola!", Toast.LENGTH_LONG).show();
                    N.getText().clear();

                }
                if(Jest2 == true && CheckEditTextEmpty == false){
                    Toast.makeText(Dodaj_Leki.this, "Wypelnij wszystkie pola!", Toast.LENGTH_LONG).show();

                }
                if(Jest2 == false && CheckEditTextEmpty == true){
                    Toast.makeText(Dodaj_Leki.this, "Podany lek istnieje!", Toast.LENGTH_LONG).show();
                    N.getText().clear();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_podstawowe,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void CheckEditTextIsEmptyOrNot(String Nazwa1, String Grupa1,String Zdjecie1, String Sklad1, String Opis1,
                                          String Ilosc_String,String Cena_String,String String_Recepte){

        if (TextUtils.isEmpty(Nazwa1) || TextUtils.isEmpty(Grupa1) || TextUtils.isEmpty(Zdjecie1) || TextUtils.isEmpty(Sklad1) || TextUtils.isEmpty(Opis1) ||
                TextUtils.isEmpty(Ilosc_String) || TextUtils.isEmpty(Cena_String) || TextUtils.isEmpty(String_Recepte))
            CheckEditTextEmpty = false;
        else
            CheckEditTextEmpty = true;

    }
    public void CheckIfExits2(){
        try{
            JSONArray tab = new db_connect().getResults("SELECT Nazwa FROM Leki ");
            //JSONArray tab = new db_connect().getResults("SELECT Login FROM Aptekarze ");
            //JSONArray tab2 = new db_connect().getResults("SELECT Login FROM Klienci");

            for (int i = 0; i < tab.length(); i++){
                JSONObject json_data = tab.getJSONObject(i);

                String nazwa = json_data.getString("Nazwa");

                if (nazwa.equals(Nazwa1)){
                    Jest2 = false;
                    break;
                } else
                    Jest2 = true;
            }

        } catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
      //  int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }





}
