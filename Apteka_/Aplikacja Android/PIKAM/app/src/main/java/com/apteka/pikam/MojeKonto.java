package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class MojeKonto extends AppCompatActivity
{
    private int ID;
    private String m_Login, m_Haslo;
    private boolean Klient;

    private JSONArray DaneOsobiste;

    private TextView Haslo, Imie, Nazwisko, Kod, Miasto, Adress, Numer, Data, Email;

    private View m_Form;
    private View m_ProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_konto);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        try {
            ID = intent.getIntExtra("ID", 0);
        } catch (Exception e) {
            Log.e("log_error", e.toString());
        }

        m_Form = findViewById(R.id.mojekonto_form);
        m_ProgressBar = findViewById(R.id.mojekonto_progress);

        if (DaneOsobiste == null)
        {
            showProgress(true);
            Task m_Task = new Task("", 1);
            m_Task.execute((Void) null);
        }

        Email = findViewById(R.id.email);
        Haslo = findViewById(R.id.haslocrypted);
        Imie = findViewById(R.id.imie);
        Nazwisko = findViewById(R.id.nazwisko);
        Kod = findViewById(R.id.kod);
        Miasto = findViewById(R.id.miasto);
        Adress = findViewById(R.id.adres);
        Numer = findViewById(R.id.numer);
        Data = findViewById(R.id.data);

        setDane();
        setButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onBackPressed();
        return true;
    }

    private String cryptPassword(String pass)
    {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < pass.length(); i++)
        {
            r.append("*");
        }
        return r.toString();
    }

    private void setDane()
    {
        if(DaneOsobiste != null)
        {
            try
            {
                JSONObject obj = DaneOsobiste.getJSONObject(0);

                Email.setText(obj.getString("Email"));
                Haslo.setText(cryptPassword(obj.getString("Haslo")));
                Imie.setText(obj.getString("Imie"));
                Nazwisko.setText(obj.getString("Nazwisko"));
                Adress.setText(obj.getString("Adres"));
                Kod.setText(obj.getString("KodPocztowy"));
                Data.setText(obj.getString("DataUrodzenia"));
                Miasto.setText(obj.getString("Miejscowosc"));
                Numer.setText(obj.getString("Telefon"));

                m_Login = obj.getString("Login");
                m_Haslo = obj.getString("Haslo");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private String MD5(String s)
    {
        byte[] data;
        try
        {
            data = s.getBytes("UTF-8");
            String res = Base64.encodeToString(data, Base64.DEFAULT);

            if(res.contains("\n"))
            {
                res = res.replace("\n", "");
            }

            return res;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void setButtons()
    {
        Button change_haslo = findViewById(R.id.change_haslo);
        change_haslo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MojeKonto.this);
                LayoutInflater inflater = MojeKonto.this.getLayoutInflater();
                final ViewGroup nullParent = null;
                final View dialogView = inflater.inflate(R.layout.dialog_change_text, nullParent);
                dialogBuilder.setView(dialogView);

                final EditText edt = dialogView.findViewById(R.id.dialogEditText);
                edt.setHint("Stare hasło: ");
                edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                TextInputLayout snp = dialogView.findViewById(R.id.newP);
                snp.setVisibility(View.VISIBLE);
                snp = dialogView.findViewById(R.id.repeat);
                snp.setVisibility(View.VISIBLE);
                final EditText newPAss = dialogView.findViewById(R.id.new_password);
                final EditText rPass = dialogView.findViewById(R.id.repet_password);
                newPAss.setHint("Nowe hasło: ");
                newPAss.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                rPass.setHint("Powtórz hasło: ");
                rPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                dialogBuilder.setTitle("Zmień hasło");
                dialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String old = edt.getText().toString();
                        if(Objects.equals(MD5(old), m_Haslo))
                        {
                            String nP = newPAss.getText().toString();
                            String rP = rPass.getText().toString();
                            if(nP.equals(rP))
                            {
                                String query;
                                if(Klient)
                                {
                                    query = "UPDATE Klienci SET Haslo = '" + MD5(nP) + "' WHERE ID = '" + ID + "'";
                                }
                                else
                                {
                                    query = "UPDATE Aptekarze SET Haslo = '" + MD5(nP) + "' WHERE ID = '" + ID + "'";
                                }
                                Task m_Task = new Task(query, 0);
                                m_Task.execute((Void) null);
                            }
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Porzuć zmiany", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        Button change_email = findViewById(R.id.change_email);
        change_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MojeKonto.this);
                LayoutInflater inflater = MojeKonto.this.getLayoutInflater();
                final ViewGroup nullParent = null;
                final View dialogView = inflater.inflate(R.layout.dialog_change_text, nullParent);
                dialogBuilder.setView(dialogView);

                final EditText edt = dialogView.findViewById(R.id.dialogEditText);
                edt.setHint(Email.getText());

                dialogBuilder.setTitle("Zmień adress eMail");
                dialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String old = Email.getText().toString();
                        if(!Objects.equals(edt.getText().toString(), old))
                        {
                            String query, query2, newEmail = edt.getText().toString();
                            if(Klient)
                            {
                                query = "UPDATE Klienci SET Email = '" + newEmail + "' WHERE ID = '" + ID + "'";
                                query2 = "UPDATE Klienci SET Aktywny = '0' WHERE ID = '" + ID + "'";
                            }
                            else
                            {
                                query = "UPDATE Aptekarze SET Email = '" + newEmail + "' WHERE ID = '" + ID + "'";
                                query2 = "UPDATE Aptekarze SET Aktywny = '0' WHERE ID = '" + ID + "'";
                            }
                            Task m_Task = new Task(query, 0);
                            m_Task.execute((Void) null);

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            int i = 0;
                            while(!m_Task.Finish)
                            {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("MSG", String.valueOf(i));
                                i++;
                            }

                            m_Task = new Task(query2, 0);
                            m_Task.execute((Void) null);

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            i = 0;
                            while(!m_Task.Finish)
                            {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("MSG", String.valueOf(i));
                                i--;
                            }

                            m_Task = new Task(newEmail, 2);
                            m_Task.execute((Void) null);
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Porzuć zmiany", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        Button change_imie = findViewById(R.id.change_imie);
        change_imie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Imie", Imie);
            }
        });
        Button change_nazwisko = findViewById(R.id.change_nazwisko);
        change_nazwisko.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Nazwisko", Nazwisko);
            }
        });
        Button change_kod = findViewById(R.id.change_kod);
        change_kod.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Kod Pocztowy", Numer);
            }
        });
        Button change_miasto = findViewById(R.id.change_miasto);
        change_miasto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Miejscowosc", Miasto);
            }
        });
        Button change_adres = findViewById(R.id.change_adres);
        change_adres.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Adres", Imie);
            }
        });
        Button change_data = findViewById(R.id.change_data);
        change_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Data Urodzenia", Numer);
            }
        });
        Button change_numer = findViewById(R.id.change_numer);
        change_numer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeString("Telefon", Numer);
            }
        });
    }

    private void changeString(final String type, final TextView view)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MojeKonto.this);
        LayoutInflater inflater = MojeKonto.this.getLayoutInflater();
        final ViewGroup nullParent = null;
        final View dialogView = inflater.inflate(R.layout.dialog_change_text, nullParent);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.dialogEditText);

        dialogBuilder.setTitle("Zmień " + type.toLowerCase());
        dialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String old = edt.getText().toString();
                if(!old.contentEquals(view.getText()))
                {
                    String query;
                    if(Klient)
                    {
                        query = "UPDATE Klienci SET " + type.replace(" ", "") + " = '" + old + "' WHERE ID = '" + ID + "'";
                    }
                    else
                    {
                        query = "UPDATE Aptekarze SET " + type.replace(" ", "") + " = '" + old + "' WHERE ID = '" + ID + "'";
                    }
                    Task m_Task = new Task(query, 0);
                    m_Task.execute((Void) null);
                }
            }
        });
        dialogBuilder.setNegativeButton("Porzuć zmiany", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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
                Toast.makeText(MojeKonto.this,"Błąd podczas pobierania lub aktualizacji danych danych!", Toast.LENGTH_LONG).show();
            }
            else
            {
                setDane();
                if(TypOperacji == 2)
                {
                    Toast.makeText(MojeKonto.this,"eMail z linkiem aktywacyjnym został wysłany na podany adress mailowy!", Toast.LENGTH_LONG).show();
                    Email.setText(Query);
                }
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
                if(TypOperacji == 0)
                {
                    new ConnectToDataBase().Execute(Query);
                    Thread.sleep(300);
                    UpdateDate();
                }
                if (TypOperacji == 1)
                {
                    UpdateDate();
                }
                if(TypOperacji == 2)
                {
                    if(new ConnectToDataBase().SendEMail(Integer.toString(ID), m_Login))
                    {
                        Finish = true;
                    }
                    else
                    {
                        Toast.makeText(MojeKonto.this,"Wystąpił problem z wysyłaniem eMaila!", Toast.LENGTH_LONG).show();
                    }
                }
                return Finish;
            }
            catch (Exception e)
            {
                DaneOsobiste = null;
                Finish = false;
            }

            return false;
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
        }

        private void UpdateDate()
        {
            JSONArray dane = new ConnectToDataBase().Execute("SELECT * FROM Klienci WHERE ID = " + ID);
            if(dane != null && dane.length() > 0)
            {
                Klient = true;
                DaneOsobiste = dane;
            }
            else
            {
                Klient = false;
                DaneOsobiste = new ConnectToDataBase().Execute("SELECT * FROM Aptekarze WHERE ID = " + ID);
            }
            Finish = true;
        }
    }
}
