package com.apteka.pikam;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class EdytujLek extends AppCompatActivity
{
    private int ID;

    private TextView Nazwa, Ilosc, Cena, Opis, Sklad;
    private CheckBox NaRecepte;
    private boolean bnazwa = false;
    private boolean bilosc = false;
    private boolean bcena = false;
    private boolean bopis = false;
    private boolean bsklad = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_lek);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Nazwa = findViewById(R.id.nazwa);
        Ilosc = findViewById(R.id.ilosc_edytuj);
        Cena = findViewById(R.id.cena_edytuj);
        Opis = findViewById(R.id.opis_edytuj);
        Sklad = findViewById(R.id.sklad_edytuj);
        NaRecepte = findViewById(R.id.na_recepte);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        String m_Nazwa = intent.getStringExtra("Nazwa");
        String m_Ilosc = intent.getStringExtra("Ilosc");
        String m_Cena = intent.getStringExtra("Cena");
        String m_Opis = intent.getStringExtra("Opis");
        String m_Sklad = intent.getStringExtra("Sklad");
        String m_NaRecepte = intent.getStringExtra("NaRecepte");

        Nazwa.setText(m_Nazwa);
        Ilosc.setText(m_Ilosc);
        Cena.setText(m_Cena);
        Opis.setText(m_Opis);
        Sklad.setText(m_Sklad);
        if(m_NaRecepte.equals("TAK"))
        {
            NaRecepte.setChecked(true);
        }
        else
        {
            NaRecepte.setChecked(false);
        }
        setButtoms();
    }

    @Override
    public void onBackPressed()
    {
        Commit();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Commit();
        return true;
    }

    private void Commit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EdytujLek.this);
        builder.setMessage("Chcesz zapisać zmainy?").setPositiveButton("Tak", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Task task;
                boolean check = NaRecepte.isChecked();

                if(bnazwa)
                {
                    task = new Task("UPDATE Leki SET Nazwa = '" + Nazwa.getText()+ "' WHERE ID = '" + ID + "'");
                    task.execute((Void) null);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(bilosc)
                {
                    task = new Task("UPDATE Leki SET Ilosc = '" + Ilosc.getText()+ "' WHERE ID = '" + ID + "'");
                    task.execute((Void) null);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(bcena)
                {
                    task = new Task("UPDATE Leki SET Cena = '" + Cena.getText()+ "' WHERE ID = '" + ID + "'");
                    task.execute((Void) null);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(bopis)
                {
                    task = new Task("UPDATE Leki SET Opis = '" + Opis.getText()+ "' WHERE ID = '" + ID + "'");
                    task.execute((Void) null);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(bsklad)
                {
                    task = new Task("UPDATE Leki SET Sklad = '" + Sklad.getText()+ "' WHERE ID = '" + ID + "'");
                    task.execute((Void) null);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(check)
                    task = new Task("UPDATE Leki SET NaRecepte = 'TAK' WHERE ID = '" + ID + "'");
                else
                    task = new Task("UPDATE Leki SET NaRecepte = 'NIE' WHERE ID = '" + ID + "'");
                task.execute((Void) null);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                onBackPressed();
                dialog.cancel();
            }
        }).setNegativeButton("Nie", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onBackPressed();
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setButtoms()
    {
        Button cnazwa = findViewById(R.id.change_nazwa);
        cnazwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField("nazwe leku", Nazwa);
                bnazwa = true;
            }
        });
        Button cilosc = findViewById(R.id.change_ilosc_edytuj);
        cilosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField("ilosc leku w magazynie", Ilosc);
                bilosc = true;
            }
        });
        Button ccena = findViewById(R.id.change_cena_edytuj);
        ccena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField("cene leku", Cena);
                bcena = true;
            }
        });
        Button copis = findViewById(R.id.change_opis_edytuj);
        copis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField("opis leku", Opis);
                bopis = true;
            }
        });
        Button csklad = findViewById(R.id.change_sklad_edytuj);
        csklad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField("sklad leku", Sklad);
                bsklad = true;
            }
        });
    }

    private void changeField(String type, final TextView view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EdytujLek.this);
        LayoutInflater inflater = EdytujLek.this.getLayoutInflater();
        final ViewGroup nullParent = null;
        final View dialogView = inflater.inflate(R.layout.dialog_change_text, nullParent);
        builder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.dialogEditText);

        builder.setTitle("Zmień " + type.toLowerCase()).setPositiveButton("Zmien", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.setText(edt.getText());
                dialog.cancel();
            }
        }).setNegativeButton("Porzuć", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                Toast.makeText(EdytujLek.this,"Błąd podczas pobierania lub aktualizacji danych danych!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Finish = false;
            try
            {
                new ConnectToDataBase().Execute(Query);
                Thread.sleep(300);
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
