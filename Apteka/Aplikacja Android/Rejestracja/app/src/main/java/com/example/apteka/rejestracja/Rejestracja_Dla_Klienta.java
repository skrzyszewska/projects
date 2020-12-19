package com.example.apteka.rejestracja;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Rejestracja_Dla_Klienta extends AppCompatActivity{
    EditText L,H,I,N,T,M,KP,A,D,E,PH;
    String Login1;
    String Haslo1;
    String Email1;
    String Imie1;
    String Nazwisko1;
    String Telefon1;
    String Miejscowosc1;
    String DataUrodzenia1;
    String KodPocztowy1;
    String Adres1;
    Boolean CheckEditTextEmpty ;
    Boolean Jest;
    Boolean Jest2;
    String PowtorzHaslo;
    static String AB;
    static SecureRandom rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja__dla__klienta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.klient_toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button1 = (Button) findViewById(R.id.button2);

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        L = (EditText) findViewById(R.id.Login);
        H = (EditText) findViewById(R.id.Haslo);
        E = (EditText) findViewById(R.id.Email);
        I = (EditText) findViewById(R.id.Imie);
        N = (EditText) findViewById(R.id.Nazwisko);
        T = (EditText) findViewById(R.id.Telefon);
        M = (EditText) findViewById(R.id.Miejscowosc);
        KP = (EditText) findViewById(R.id.Kod_Pocztowy);
        A = (EditText) findViewById(R.id.Adres);
        D = (EditText) findViewById(R.id.DataUrodzenia);
        PH = (EditText) findViewById(R.id.Powtorz_haslo);

        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Login1 = L.getText().toString();
                Haslo1 = H.getText().toString();
                Email1 = E.getText().toString();
                Imie1 = I.getText().toString();
                Nazwisko1 = N.getText().toString();
                Telefon1 = T.getText().toString();
                Miejscowosc1 = M.getText().toString();
                DataUrodzenia1 = D.getText().toString();
                KodPocztowy1 = KP.getText().toString();
                Adres1 = A.getText().toString();
                PowtorzHaslo = PH.getText().toString();

                CheckEditTextIsEmptyOrNot(Login1, Haslo1,Email1, Imie1, Nazwisko1,
                        Telefon1,Miejscowosc1,DataUrodzenia1,KodPocztowy1, Adres1);

                CheckIfExist();
                CheckIfExits2();
                if(Login1.length() >= 4 && Haslo1.length() >= 8){
                    if(Haslo1.equals(PowtorzHaslo) && PowtorzHaslo.equals(Haslo1)){
                        if (CheckEditTextEmpty == true && Jest == true && Jest2 == true){
                            String nazwa = MD5(Haslo1);
                            String kodaktywacyjny = random();

                            String query = "INSERT INTO" + " Klienci " + "(Login, Haslo, Email, Imie, Nazwisko, Telefon, Miejscowosc, KodPocztowy, Adres, DataUrodzenia,Aktywny,KodAktywacyjny) "
                                    + "Values ('" + Login1 + "', '" + nazwa + "', '" + Email1 + "', '" + Imie1 + "','" + Nazwisko1 + "','" + Telefon1 + "','" + Miejscowosc1 + "','" + KodPocztowy1 + "','" + Adres1 + "','" + DataUrodzenia1 + "','" + 0 + "','" + kodaktywacyjny + "')";
                            new db_connect().getResults(query);

                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Rejestracja Wykonana Pomyślnie!", Toast.LENGTH_LONG).show();

                            int idik = zwroc_ostatni_klient_id();

                            Toast.makeText(Rejestracja_Dla_Klienta.this, "pokaz id" + idik , Toast.LENGTH_LONG).show();

                            String du = Integer.toString(idik);
                            /////////////////to gowno ponizej nie chce dzialać
                            postData(du);

                            Context context;
                            context = getApplicationContext();
                            Intent intent = new Intent(context, LoginActivity.class);
                            //tu dla klienta
                            startActivity(intent);
                        }
                        if (CheckEditTextEmpty == true && Jest == false && Jest2 == true || CheckEditTextEmpty == true && Jest == true && Jest2 == false){
                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Podany Login istnieje!", Toast.LENGTH_LONG).show();
                            L.getText().clear();
                        }
                        if (CheckEditTextEmpty == false && Jest == true && Jest2 == true || CheckEditTextEmpty == false && Jest == true && Jest2 == true){
                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Wypełnij wszystkie pola!", Toast.LENGTH_LONG).show();
                        }
                        if (CheckEditTextEmpty == false && Jest == false && Jest2 == true || CheckEditTextEmpty == false && Jest == true && Jest2 == false){
                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Podany Login istnieje!", Toast.LENGTH_LONG).show();
                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Wypełnij wszystkie pola!", Toast.LENGTH_LONG).show();
                            L.getText().clear();
                        }
                        if (TextUtils.isEmpty(Login1) && TextUtils.isEmpty(Haslo1) && TextUtils.isEmpty(Email1) &&
                                TextUtils.isEmpty(Imie1) && TextUtils.isEmpty(Nazwisko1) && TextUtils.isEmpty(Telefon1) &&
                                TextUtils.isEmpty(Miejscowosc1) && TextUtils.isEmpty(DataUrodzenia1) && TextUtils.isEmpty(KodPocztowy1) && TextUtils.isEmpty(Adres1)){
                            Toast.makeText(Rejestracja_Dla_Klienta.this, "Wypełnij wszystkie pola!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{ Toast.makeText(Rejestracja_Dla_Klienta.this, "Hasła nie są równe! ", Toast.LENGTH_LONG).show();}
                }
                else{ Toast.makeText(Rejestracja_Dla_Klienta.this, "Błąd w formularzu! Login ma mieć długość min. 4 znaki, a Hasło 8! ", Toast.LENGTH_LONG).show();}
            }
        });
    }

    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 32; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void postData(String id){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id));
        InputStream is = null;
//http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://212.182.24.105:8010/?page_id=1307");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_podstawowe,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void ClearEditTextAfterDoneTask(){
        L.getText().clear();
        H.getText().clear();
        E.getText().clear();
        I.getText().clear();
        N.getText().clear();
        T.getText().clear();
        M.getText().clear();
        KP.getText().clear();
        A.getText().clear();
        D.getText().clear();
    }

    public void CheckEditTextIsEmptyOrNot( String Login1,String Haslo1,String Email1, String Imie1, String Nazwisko1,
                                           String Telefon1, String Miejscowosc1, String DataUrodzenia1, String KodPocztowy1, String Adres1){
        if(TextUtils.isEmpty(Login1) || TextUtils.isEmpty(Haslo1) || TextUtils.isEmpty(Email1)  ||
                TextUtils.isEmpty(Imie1) || TextUtils.isEmpty(Nazwisko1) || TextUtils.isEmpty(Telefon1)  ||
                TextUtils.isEmpty(Miejscowosc1) || TextUtils.isEmpty(DataUrodzenia1) || TextUtils.isEmpty(KodPocztowy1) || TextUtils.isEmpty(Adres1)){
            CheckEditTextEmpty = false ;
        }
        else{
            CheckEditTextEmpty = true ;
        }
    }

    public void CheckIfExist(){
        try {
            JSONArray tab = new db_connect().getResults("SELECT Login FROM Klienci ");
            //JSONArray tab = new db_connect().getResults("SELECT Login FROM Aptekarze ");
            //JSONArray tab2 = new db_connect().getResults("SELECT Login FROM Klienci");
            for (int i = 0; i < tab.length(); i++){
                JSONObject json_data = tab.getJSONObject(i);
                String login = json_data.getString("Login");

                if(login.equals(Login1)){
                    Jest = false;
                    break;
                }
                else
                    Jest = true;
            }
        } catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public void CheckIfExits2(){
        try {
            JSONArray tab = new db_connect().getResults("SELECT Login FROM Aptekarze ");
            //JSONArray tab = new db_connect().getResults("SELECT Login FROM Aptekarze ");
            //JSONArray tab2 = new db_connect().getResults("SELECT Login FROM Klienci");
            for (int i = 0; i < tab.length(); i++){
                JSONObject json_data = tab.getJSONObject(i);
                String login = json_data.getString("Login");

                if(login.equals(Login1)){
                    Jest2 = false;
                    break;
                }
                else
                    Jest2 = true;
            }
        } catch (JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
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

    int zwroc_ostatni_klient_id(){
        JSONArray tab2 = new db_connect().getResults("SELECT * FROM Klienci");
        try{
            for (int i = 0; i < tab2.length(); i++){
                JSONObject json_data = tab2.getJSONObject(tab2.length()-1);
                int id_ = json_data.getInt("ID");
                return id_;
            }
        } catch (JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

}
