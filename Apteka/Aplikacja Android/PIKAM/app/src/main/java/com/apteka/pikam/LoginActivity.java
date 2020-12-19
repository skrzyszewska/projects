package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    private UserLoginTask m_Task = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEeditor;
    EditText m_LoginView;
    EditText m_PasswordView;
    private View m_ProgressView;
    private View m_LoginFormView;
    private String NazwaLeku;
    Button m_LoginButton;
    private CheckBox mCheckBox;

    public static String UserID = null, UserType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_LoginView = (EditText) findViewById(R.id.login);

        m_PasswordView = (EditText) findViewById(R.id.password);

        mCheckBox = (CheckBox) findViewById(R.id.checkbox);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEeditor = mPreferences.edit();

        checkSharedPreferences();



        m_PasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



        m_LoginButton = findViewById(R.id.sign_in_button);
        m_LoginButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mCheckBox.isChecked()){
                    mEeditor.putString(getString(R.string.checkbox),"True");
                    mEeditor.commit();

                    String name = m_LoginView.getText().toString();
                    mEeditor.putString(getString(R.string.name), name);
                    mEeditor.commit();

                    String pass = m_PasswordView.getText().toString();
                    mEeditor.putString(getString(R.string.password), pass);
                    mEeditor.commit();

                }
                else{
                    mEeditor.putString(getString(R.string.checkbox),"False");
                    mEeditor.commit();

                    mEeditor.putString(getString(R.string.name), "");
                    mEeditor.commit();

                    mEeditor.putString(getString(R.string.password), "");
                    mEeditor.commit();

                }
                attemptLogin();
            }
        });

        Button m_RejestrButton = findViewById(R.id.rejestr_button);
        m_RejestrButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, RejestracjaActivity.class);
                startActivity(intent);
            }
        });

        m_LoginFormView = findViewById(R.id.login_form);
        m_ProgressView = findViewById(R.id.login_progress);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        NazwaLeku = intent.getStringExtra("Name");
    }
    private void checkSharedPreferences(){
        String checkbox = mPreferences.getString(getString(R.string.checkbox),"False");
        String name = mPreferences.getString(getString(R.string.name),"");
        String password = mPreferences.getString(getString(R.string.password), "");

        m_LoginView.setText(name);
        m_PasswordView.setText(password);

        if(checkbox.equals("True")){
        mCheckBox.setChecked(true);

        }else{
            mCheckBox.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AnonimMode.class);
        startActivity(intent);
        return true;
    }

    public void onBackPressed()
    {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AnonimMode.class);
        startActivity(intent);
    }

    private void attemptLogin()
    {
        if (m_Task != null)
        {
            return;
        }

        // Reset errors.
        m_LoginView.setError(null);
        m_PasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = m_LoginView.getText().toString();
        String password = m_PasswordView.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            m_PasswordView.setError(getString(R.string.error_invalid_password));
            m_PasswordView.requestFocus();
            cancel = true;
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login))
        {
            m_LoginView.setError(getString(R.string.error_field_required));
            m_LoginView.requestFocus();
            cancel = true;
        }

        if (!cancel)
        {
            showProgress(true);
            m_Task = new UserLoginTask(login, password);
            m_Task.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() >= 8;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        m_LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        m_LoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                m_LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        m_ProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
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
        return "";
    }

    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {
        private String m_Login, m_Password, m_Imie, m_Nazwisko;
        private boolean loginCorrect = false, passwordCorrenct = false, isActive = false;
        private int Type = 0, ID;

        UserLoginTask(String login, String password)
        {
            m_Login = login;
            m_Password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                JSONArray tabelaKlienci = new ConnectToDataBase().Execute("SELECT * FROM Klienci ORDER BY Login");
                JSONArray tabelaAptekarze = new ConnectToDataBase().Execute("SELECT * FROM Aptekarze ORDER BY Login");

                for(int i = 0; i < tabelaKlienci.length(); i++)
                {
                    JSONObject json_data = tabelaKlienci.getJSONObject(i);

                    String login = json_data.getString("Login");
                    if(m_Login.equals(login) && !loginCorrect)
                    {
                        loginCorrect = true;
                    }

                    String haslo = json_data.getString("Haslo");
                    if(haslo.contains("\n"))
                    {
                        haslo = haslo.replace("\n", "");
                    }

                    String h = MD5(m_Password);
                    if(h.contains("\n"))
                    {
                        h = h.replace("\n", "");
                    }

                    if(Objects.equals(h, haslo) && !passwordCorrenct)
                    {
                        passwordCorrenct = true;
                    }

                    int active = json_data.getInt("Aktywny");
                    if(active == 1 && !isActive)
                    {
                        isActive = true;
                    }

                    if(loginCorrect && passwordCorrenct && isActive)
                    {
                        ID = json_data.getInt("ID");
                        m_Imie = json_data.getString("Imie");
                        m_Nazwisko = json_data.getString("Nazwisko");
                        Type = 1;

                        UserID = Integer.toString(ID);
                        UserType = "Klient";

                        return true;
                    }
                    loginCorrect = false;
                    passwordCorrenct = false;
                    isActive = false;
                }

                for(int i = 0; i < tabelaAptekarze.length(); i++)
                {
                    JSONObject json_data = tabelaAptekarze.getJSONObject(i);

                    String login = json_data.getString("Login");
                    if(m_Login.equals(login) && !loginCorrect)
                    {
                        loginCorrect = true;
                    }

                    String haslo = json_data.getString("Haslo");
                    haslo += "\n";
                    String h = MD5(m_Password);
                    if(Objects.equals(h, haslo) && !passwordCorrenct)
                    {
                        passwordCorrenct = true;
                    }

                    int active = json_data.getInt("Aktywny");
                    if(active == 1 && !isActive)
                    {
                        isActive = true;
                    }
                    if(loginCorrect && passwordCorrenct && isActive)
                    {
                        ID = json_data.getInt("ID");
                        m_Imie = json_data.getString("Imie");
                        m_Nazwisko = json_data.getString("Nazwisko");
                        Type = 2;

                        UserID = Integer.toString(ID);
                        UserType = "Aptekarz";

                        return true;
                    }

                    loginCorrect = false;
                    passwordCorrenct = false;
                    isActive = false;
                }
            }
            catch (Exception e)
            {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            m_Task = null;
            showProgress(false);

            if (success)
            {
                Intent intent;
                Context context;
                context= getApplicationContext();
                if(NazwaLeku != null && NazwaLeku.length() > 0)
                {
                    intent = new Intent(context, PojedynczyLekActivity.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("Imie", m_Imie);
                    intent.putExtra("Nazwisko", m_Nazwisko);
                    intent.putExtra("Name", NazwaLeku);
                    intent.putExtra("Typ", Type);
                    startActivity(intent);
                }
                else
                {
                    if(Type == 1)
                    {
                        AnonimMode.m_Koszyk = new Koszyk(ID, m_Imie, m_Nazwisko);

                        intent = new Intent(context, KlientMode.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Imie", m_Imie);
                        intent.putExtra("Nazwisko", m_Nazwisko);
                        startActivity(intent);
                    }
                    if(Type == 2)
                    {
                        intent = new Intent(context, AptekarzMode.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Imie", m_Imie);
                        intent.putExtra("Nazwisko", m_Nazwisko);
                        startActivity(intent);
                    }
                }
            }
            else
            {
                m_LoginView.setError("Błędny login lub konto nie zostało aktwowane");
                m_PasswordView.setError("Błędne hasło");
            }
        }

        @Override
        protected void onCancelled()
        {
            m_Task = null;
            showProgress(false);
        }

    }



}

