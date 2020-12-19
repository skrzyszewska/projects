package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.apteka.pikam.R.color.colorPrimaryDark;

public class RejestracjaActivity extends AppCompatActivity
{
    private EditText Login;
    private EditText Email;
    private EditText Password;
    private EditText RepetPass;
    private EditText FirstName;
    private EditText LastName;
    private EditText Adress;
    private EditText Kod;
    private EditText City;
    private EditText Code;
    private EditText Date;
    private EditText Number;

    private View Progress;
    private View Form;

    private Task m_Task = null;
    private TaskCheck mTask = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Login = findViewById(R.id.login);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        RepetPass = findViewById(R.id.repet_password);
        FirstName = findViewById(R.id.first_name);
        LastName = findViewById(R.id.last_name);
        Adress = findViewById(R.id.adress);
        Kod = findViewById(R.id.kod_pocztowy);
        City = findViewById(R.id.city);
        Code = findViewById(R.id.code);
        Number = findViewById(R.id.number);
        Date = findViewById(R.id.date);

        Progress = findViewById(R.id.rejestracja_progress);
        Form = findViewById(R.id.rejestracja_form);

        Button button = findViewById(R.id.rejestracion_button);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                attemptRejestr();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    public void onBackPressed()
    {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void attemptRejestr()
    {
        if (m_Task != null)
        {
            return;
        }

        Login.setError(null);
        Email.setError(null);
        Password.setError(null);
        RepetPass.setError(null);
        FirstName.setError(null);
        LastName.setError(null);
        Date.setError(null);
        Number.setError(null);
        Code.setError(null);

        boolean cancel = false;
        String login = "", email = "", pass = "", code = "", firstName = "", lastName = "", date = "", number = "";
        String adress = Adress.getText().toString();
        String kod = Kod.getText().toString();
        String cidy = City.getText().toString();

        if(Login.getText().length() > 0)
        {
            login = Login.getText().toString();
        }
        else
        {
            Login.setError("Empty login");
            Login.requestFocus();
            cancel = true;
        }

        if(isEmailValid(Email.getText().toString()))
        {
            email = Email.getText().toString();
        }
        else
        {
            Email.setError("Incorect email address");
            Email.requestFocus();
            cancel = true;
        }

        if(isPasswordValid(Password.getText().toString()))
        {
            if(Password.getText().toString().equals(RepetPass.getText().toString()))
            {
                pass = Password.getText().toString();
            }
            else
            {
                RepetPass.setError("Passwords are not identical");
                RepetPass.requestFocus();
                cancel = true;
            }
        }
        else
        {
            Password.setError("Too short password");
            Password.requestFocus();
            cancel = true;
        }

        if(FirstName.getText().toString().length() > 0 && validName(FirstName.getText().toString()))
        {
            firstName = FirstName.getText().toString();
        }
        else
        {
            FirstName.setError("The name does not begin with a capital letter");
            FirstName.requestFocus();
            cancel = true;
        }

        if(LastName.getText().toString().length() > 0 && validName(LastName.getText().toString()))
        {
            lastName = LastName.getText().toString();
        }
        else
        {
            LastName.setError("The name does not begin with a capital letter");
            LastName.requestFocus();
            cancel = true;
        }

        if(Code.getText().toString().length() == 0 || validCode(Code.getText().toString()))
        {
            code = Code.getText().toString();
        }
        else
        {
            Code.setError("Incorect code");
            Code.requestFocus();
            cancel = true;
        }

        if(validDate(Date.getText().toString()))
        {
            date = Date.getText().toString();
        }
        else
        {
            Date.setError("Incorect date");
            Date.requestFocus();
            cancel = true;
        }

        if(Number.getText().toString().length() == 9 || Number.getText().toString().length() == 0)
        {
            number = Number.getText().toString();
        }
        else
        {
            Number.setError("Incorect number");
            Number.requestFocus();
            cancel = true;
        }

        if(!cancel)
        {
            showProgress(true);

            mTask = new TaskCheck(login, email, code);
            mTask.execute((Void) null);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int i = 0;
            while(!mTask.Finish)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("MSG", String.valueOf(i));
                i++;
            }

            showProgress(true);

            m_Task = new Task(login, email, pass, adress, kod, cidy, code, date, number, firstName, lastName);
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

            showProgress(true);

            TaskSend m_TaskS;
            if(Code.length() == 0)
            {
                m_TaskS = new TaskSend(0);
            }
            else
            {
                m_TaskS = new TaskSend(1);
            }
            m_TaskS.execute((Void) null);
        }
    }

    private boolean validDate(String date)
    {
        return date.length() == 0 || date.charAt(2) == '-';
    }

    private boolean validName(String name)
    {
        return name.charAt(0) > 64 && name.charAt(0) < 91;
    }

    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 7;
    }

    private boolean validCode(String code)
    {
        try
        {
            if(code.length() != 6) return false;

            int f = Integer.parseInt(String.valueOf(code.charAt(0)));
            int s = Integer.parseInt(String.valueOf(code.charAt(1)));
            char c = code.charAt(2);
            int fs = Integer.parseInt(String.valueOf(code.charAt(3)));
            int ss = Integer.parseInt(String.valueOf(code.charAt(4)));
            int ts = Integer.parseInt(String.valueOf(code.charAt(5)));
            if ((c > 96 && c < 123) && (f >= 0 && f <= 9) && (s >= 0 && s <= 9)
                    && (fs >= 0 && fs <= 9) && (ss >= 0 && ss <= 9) && (ts >= 0 && ts <= 9)) return true;
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
            return false;
        }
        return true;
    }

    private String randomActivateCode()
    {
        return UUID.randomUUID().toString().replace("-", "").substring(0,32);
    }

    private String MD5(String s)
    {
        byte[] data;
        try
        {
            data = s.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        Form.setVisibility(show ? View.GONE : View.VISIBLE);
        Form.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                Form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        Progress.setVisibility(show ? View.VISIBLE : View.GONE);
        Progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                Progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class Task extends AsyncTask<Void, Void, Boolean>
    {
        boolean Finish;
        private String mLogin, mEmail, mPass, Adress, Kod, City, Code, Date, Number, FirstName, LastName;

        Task(String login,String email,String pass,String adress,String kod,String cidy,String code,String date,String number,String firstName,String lastName)
        {
            mLogin = login;
            mEmail = email;
            mPass = pass;
            Adress = adress;
            Kod = kod;
            City = cidy;
            Code = code;
            Date = date;
            Number = number;
            FirstName = firstName;
            LastName = lastName;

            Finish = false;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            if(!mTask.Finish)
            {
                Finish = false;
                return false;
            }
            try
            {
                String pass = MD5(mPass);
                if(Code.length() == 0)
                {
                    new ConnectToDataBase().Execute("INSERT INTO Klienci " +
                            "(Login, Haslo, Email, Imie, Nazwisko, Telefon, Miejscowosc, KodPocztowy, Adres, DataUrodzenia, Aktywny, KodAktywacyjny) " +
                            "Values ('" + mLogin + "', '" + pass + "', '" + mEmail + "', '" + FirstName + "', '" + LastName + "', '" + Number + "', '" +
                            City + "', '" + Kod + "', '" + Adress + "', '" + Date + "', '0', '" + randomActivateCode() + "')");
                }
                else
                {
                    new ConnectToDataBase().Execute("INSERT INTO Aptekarze " +
                            "(Login, Haslo, Email, Podpis, Imie, Nazwisko, Telefon, Miejscowosc, KodPocztowy, Adres, DataUrodzenia, Aktywny, KodAktywacyjny) " +
                            "Values ('" + mLogin + "', '" + pass + "', '" + mEmail + "', '" + Code + "', '" + FirstName + "', '" + LastName + "', '" + Number + "', '" +
                            City + "', '" + Kod + "', '" + Adress + "', '" + Date + "', '0', '" + randomActivateCode() + "')");
                }
                Finish = true;
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.toString());
                Finish = false;
            }
            return Finish;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            m_Task = null;
            showProgress(false);

            if (success)
            {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled()
        {
            m_Task = null;
            showProgress(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TaskCheck extends AsyncTask<Void, Void, Boolean>
    {
        boolean Finish, loginExist, emailExist;
        private String mLogin, mEmail, Code;
        private JSONArray Klienci, Aptekarze;

        TaskCheck(String login, String email, String code)
        {
            mLogin = login;
            mEmail = email;
            Code = code;

            loginExist = false;
            emailExist = false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                showProgress(false);
            }
            else
            {
                if(loginExist)
                {
                    Login.setError("Login already exists");
                    Login.requestFocus();
                }

                if(emailExist)
                {
                    Email.setError("Email already exists");
                    Email.requestFocus();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Finish = false;
            try
            {
                try
                {
                    Klienci = new ConnectToDataBase().Execute("SELECT Login, Email FROM Klienci");
                    Aptekarze = new ConnectToDataBase().Execute("SELECT Login, Email, Podpis FROM Aptekarze");
                }
                catch (Exception e)
                {
                    Log.e("ERROR", e.toString());
                    Finish = false;
                    return false;
                }

                for(int i = 0; i < Klienci.length(); i++)
                {
                    JSONObject json = Klienci.getJSONObject(i);
                    if(json.getString("Login").equals(mLogin))
                    {
                        Finish = false;
                        loginExist = true;
                        return false;
                    }
                    if(json.getString("Email").equals(mEmail))
                    {
                        Finish = false;
                        emailExist = true;
                        return false;
                    }
                }

                for(int i = 0; i < Aptekarze.length(); i++)
                {
                    JSONObject json = Aptekarze.getJSONObject(i);
                    if(json.getString("Podpis").equals(Code))
                    {
                        Finish = false;
                        return false;
                    }
                    if(json.getString("Login").equals(mLogin))
                    {
                        Finish = false;
                        loginExist = true;
                        return false;
                    }
                    if(json.getString("Email").equals(mEmail))
                    {
                        Finish = false;
                        emailExist = true;
                        return false;
                    }
                }

                Finish = true;
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.toString());
                Finish = false;
            }

            return Finish;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TaskSend extends AsyncTask<Void, Void, Boolean>
    {
        Boolean Finish;
        private int Type;

        TaskSend(int type)
        {
            Type = type;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(!success || !Finish)
            {
                Toast.makeText(RejestracjaActivity.this,"Błąd podczas pobierania danych!", Toast.LENGTH_LONG).show();
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
                String id = "", login = "";
                if(Type == 0)
                {
                    JSONArray table =  new ConnectToDataBase().Execute("SELECT ID, Login FROM Klienci ORDER BY ID DESC LIMIT 1");

                    if(table.length() > 0)
                    {
                        JSONObject obj = table.getJSONObject(0);
                        id = obj.getString("ID");
                        login = obj.getString("Login");
                    }
                }
                else
                {
                    JSONArray table =  new ConnectToDataBase().Execute("SELECT ID, Login FROM Aptekarze ORDER BY ID DESC LIMIT 1");

                    if(table.length() > 0)
                    {
                        JSONObject obj = table.getJSONObject(0);
                        id = obj.getString("ID");
                        login = obj.getString("Login");
                    }
                }

                if(new ConnectToDataBase().SendEMail(id, login))
                {
                    Finish = true;
                    return true;
                }
            }
            catch (Exception e)
            {
                Finish = false;
            }
            return false;
        }
    }
}

