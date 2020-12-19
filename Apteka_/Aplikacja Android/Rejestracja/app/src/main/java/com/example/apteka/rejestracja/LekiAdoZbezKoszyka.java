package com.example.apteka.rejestracja;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LekiAdoZbezKoszyka  extends AppCompatActivity{
    Context context;
    List<String> tasks;
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leki_a_do_z);
        Toolbar toolbar = (Toolbar) findViewById(R.id.klient_toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        context = this;
        tasks = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,tasks);
        ListView listView = (ListView) findViewById(R.id.lista);
        listView.setAdapter(adapter);

        String OutputData = "";
        try{
            JSONArray tab = new db_connect().getResults("SELECT * FROM Leki ORDER BY NAZWA");

            for(int i=0;i<tab.length();i++){
                // OBRÓBKA WSZYSTKICH DANYCH //
                JSONObject json_data = tab.getJSONObject(i);

                //int id = json_data.getInt("ID");
                String nazwa = json_data.getString("Nazwa");
                // String zdjecie = json_data.getString("");
                // String sklad = json_data.getString("Skład");
                //String opis = json_data.getString("Opis");
                //Integer ilosc = json_data.getInt("Ilość");
                // Integer cena = json_data.getInt("Cena");
                //boolean naRecepte = json_data.getBoolean("NaRecepte");

                //OutputData = +i + nazwa + "\n" + opis;
                tasks.add(nazwa);

                adapter.notifyDataSetChanged();
            }
        } catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }
}


