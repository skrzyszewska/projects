package com.apteka.pikam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import static com.apteka.pikam.R.color.colorPrimaryDark;

public class PojedynczyLekActivity extends AppCompatActivity
{
    private int ID, NrZamowienia, IDLeku, Typ;
    private String Imie, Nazwisko;
    private boolean IsAptekarz;

    private View m_Form;
    private View m_ProgressBar;
    private String ilosc2, cena2, opis, sklad, naRecepte;
    private static String nazwa;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pojedynczy_lek);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(colorPrimaryDark));

        m_Form = findViewById(R.id.pojedynczy_lek_form);
        m_ProgressBar = findViewById(R.id.pojedynczy_lek_progress);
        showProgress(true);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        final String label = intent.getStringExtra("Name");
        Imie = intent.getStringExtra("Imie");
        Nazwisko = intent.getStringExtra("Nazwisko");
        try
        {
            ID = intent.getIntExtra("ID", 0);
            NrZamowienia = intent.getIntExtra("NR", 0);
            Typ = intent.getIntExtra("Typ", 0);
            IsAptekarz = intent.getBooleanExtra("AptekarzMode", false);

            AnonimMode.m_Koszyk = new Koszyk(ID, Imie, Nazwisko);
        }
        catch (Exception e)
        {
            Log.e("log_error", e.toString());
        }

        if(Typ == 2 || (ID != 0 && NrZamowienia > 0))
        {
            RelativeLayout IloscTextBox = findViewById(R.id.edit_count);
            IloscTextBox.setVisibility(View.GONE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        final EditText iloscLeku = findViewById(R.id.edit_text_count);
        if((ID != 0 && NrZamowienia == 0 && IsAptekarz) || Typ == 2)
        {
            fab.setImageResource(R.drawable.ic_edit_black_24dp);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showProgress(true);

                    AlertDialog.Builder builder = new AlertDialog.Builder(PojedynczyLekActivity.this);
                    builder.setMessage("Co chcesz zrobić?").setPositiveButton("Edytuj Lek", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Context context = getApplicationContext();
                            Intent intent = new Intent(context, EdytujLek.class);
                            intent.putExtra("ID", IDLeku);
                            intent.putExtra("Nazwa", nazwa);
                            intent.putExtra("Ilosc", ilosc2);
                            intent.putExtra("Cena", cena2);
                            intent.putExtra("Opis", opis);
                            intent.putExtra("Sklad", sklad);
                            intent.putExtra("NaRecepte", naRecepte);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    }).setNegativeButton("Usun Lek", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Task task = new Task("DELETE FROM Leki WHERE ID = '" + IDLeku + "'");
                            task.execute((Void) null);
                            onBackPressed();
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    showProgress(false);
                }
            });
        }
        else if(ID != 0 && NrZamowienia == 0)
        {
            fab.setImageResource(R.drawable.plus);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PojedynczyLekActivity.this);
                    builder.setMessage("Czy chcesz dodać lek do koszyka?").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //DODAWANIE DO KOSZYKA
                            // potrójna proba dodania leku do koszyka
                            showProgress(true);
                            int ilosc = 1;
                            try
                            {
                                ilosc = Integer.parseInt(String.valueOf(iloscLeku.getText()));
                            }
                            catch (Exception ignored)
                            {

                            }

                            AnonimMode.m_Koszyk = new Koszyk(ID, Imie, Nazwisko);
                            if(AnonimMode.m_Koszyk.DodajDoKoszyka(IDLeku, ilosc))
                            {
                                Snackbar.make(view, "Dodano poprawenie lek " + label + " do koszyka.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                showProgress(false);

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                onBackPressed();
                            }
                            else
                            {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if(AnonimMode.m_Koszyk.DodajDoKoszyka(IDLeku, ilosc))
                                {
                                    Snackbar.make(view, "Dodano poprawenie lek " + label + " do koszyka.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                    showProgress(false);

                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    onBackPressed();
                                }
                                else
                                {
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if(AnonimMode.m_Koszyk.DodajDoKoszyka(IDLeku, ilosc))
                                    {
                                        Snackbar.make(view, "Dodano poprawenie lek " + label + " do koszyka.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                        showProgress(false);

                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        onBackPressed();
                                    }
                                    else
                                    {
                                        Snackbar.make(view, "Błąd podczas dodawania leku " + label + " do koszyka.\n" +
                                                "Brak leku w magazynie!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        showProgress(false);
                                    }
                                }
                            }
                            dialog.cancel();
                        }
                    }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        else if(ID != 0 && NrZamowienia > 0)
        {
            fab.setImageResource(R.drawable.ic_remove_black_24dp);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PojedynczyLekActivity.this);
                    builder.setMessage("Czy chcesz usunąć lek z koszyka?").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //usuwanie z koszyka DO KOSZYKA
                            showProgress(true);
                            AnonimMode.m_Koszyk = new Koszyk(ID, Imie, Nazwisko);
                            if(AnonimMode.m_Koszyk.UsunLekZKoszyka(IDLeku))
                            {
                                Snackbar.make(view, "Usunieto poprawenie lek " + label + " z koszyka.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                                Intent intent;
                                Context context;
                                context = getApplicationContext();
                                intent = new Intent(context,Koszyk.class);
                                intent.putExtra("ID", ID);
                                intent.putExtra("Imie", Imie);
                                intent.putExtra("Nazwisko", Nazwisko);
                                AnonimMode.m_Koszyk = new Koszyk(ID, Imie, Nazwisko);
                                startActivity(intent);
                            }
                            else
                            {
                                if(Koszyk.m_Koszyk.size() == 0)
                                {
                                    Snackbar.make(view, "Koszyk jest pusty! Nie usunięto leku " + label + " z koszyka.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else
                                {
                                    Snackbar.make(view, "Błąd podczas usuwania leku " + label + " z koszyka.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                            showProgress(false);
                            dialog.cancel();
                        }
                    }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        else
        {
            RelativeLayout IloscTextBox = findViewById(R.id.edit_count);
            IloscTextBox.setVisibility(View.GONE);

            fab.setImageResource(R.drawable.ic_login_24dp);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,LoginActivity.class);
                    intent.putExtra("Name", label);
                    startActivity(intent);
                }
            });
        }

        setActionBar(label);
        ImageView image = findViewById(R.id.imageView);
        Setters.setImageRes(image, label);
        setTextDesc(label);
        showProgress(false);
    }

    @Override
    public void onBackPressed()
    {
        if(Typ > 0)
        {
            Intent intent;
            Context context = getApplicationContext();
            if(Typ == 1)
            {
                intent = new Intent(context, KlientMode.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", Imie);
                intent.putExtra("Nazwisko", Nazwisko);
                startActivity(intent);
            }
            if(Typ == 2)
            {
                intent = new Intent(context, AptekarzMode.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", Imie);
                intent.putExtra("Nazwisko", Nazwisko);
                startActivity(intent);
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(Typ > 0)
        {
            Intent intent;
            Context context = getApplicationContext();
            if(Typ == 1)
            {
                intent = new Intent(context, KlientMode.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", Imie);
                intent.putExtra("Nazwisko", Nazwisko);
                startActivity(intent);
                return true;
            }
            if(Typ == 2)
            {
                intent = new Intent(context, AptekarzMode.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Imie", Imie);
                intent.putExtra("Nazwisko", Nazwisko);
                startActivity(intent);
                return true;
            }
        }
        super.onBackPressed();
        return true;
    }

//HASZTAG: Dla potomnści :-D
//        int id = item.getItemId();
//        if (id == android.R.id.home)
//        {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            startActivity(new Intent(this, AnonimMode.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);

    private void setActionBar(String heading)
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show)
//    {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//        final View m_ScrollView = findViewById(R.id.ScrollView);
//        final View m_ProgressView = findViewById(R.id.progressView);
//
//        m_ScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//        m_ScrollView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
//        {
//            @Override
//            public void onAnimationEnd(Animator animation)
//            {
//                m_ScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        });
//
//        m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        m_ProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
//        {
//            @Override
//            public void onAnimationEnd(Animator animation)
//            {
//                m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        });
//    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void setTextDesc(String label)
    {
        final String szt = " Szt.";
        final String zl = " zł";
        TextView count = findViewById(R.id.count_text);
        TextView cost = findViewById(R.id.cost_text);
        TextView description = findViewById(R.id.description_text);
        TextView composition = findViewById(R.id.composition_text);
        TextView onRecept = findViewById(R.id.OnRecept);
        try
        {
            showProgress(true);
            JSONArray tab;
            if(AnonimMode.TabelaLekow != null)
            {
                tab = AnonimMode.TabelaLekow;
            }
            else
            {
                tab = new ConnectToDataBase().Execute("SELECT * FROM Leki");
            }

            int index = 0;
            for (int i = 0; i < tab.length(); i++)
            {
                JSONObject json_data = tab.getJSONObject(i);

                nazwa = json_data.getString("Nazwa");
                if(nazwa.equals(label))
                {
                    index = i;
                    break;
                }
            }

            JSONObject json_data = tab.getJSONObject(index);

            IDLeku = json_data.getInt("ID");

            Integer ilosc = json_data.getInt("Ilosc");
            if(ilosc > 0)
            {
                ilosc2 = Integer.toString(ilosc);
                count.setText(ilosc2 + szt);
            }
            else
            {
                count.setText("Produkt tymczasowo niedostępny");
            }

            Double cena = json_data.getDouble("Cena");
            cena2 = Double.toString(cena);
            cost.setText(cena2 + zl);

            naRecepte = json_data.getString("NaRecepte");
            if(naRecepte.equals("TAK"))
                onRecept.setVisibility(View.VISIBLE);
            else
                onRecept.setVisibility(View.GONE);

            opis = json_data.getString("Opis");
            description.setText(opis);

            sklad = json_data.getString("Sklad");
            composition.setText(sklad);

            showProgress(false);
        }
        catch (Exception e)
        {
            showProgress(false);

            final String emptyDes = "Brak opisu...";
            final String emptyCom = "Brak informacji o składzie...";
            count.setText("0" + szt);
            cost.setText("-" + zl);
            description.setText(emptyDes);
            composition.setText(emptyCom);
            onRecept.setVisibility(View.GONE);

            Log.e("log_tag", "Error parsing data " + e.toString());
        }
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
                Toast.makeText(PojedynczyLekActivity.this,"Błąd podczas pobierania lub aktualizacji danych danych!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(PojedynczyLekActivity.this,"Pomyślnie usunięto lek z bazy banych!", Toast.LENGTH_LONG).show();
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
                new ConnectToDataBase().Execute(Query);

                Thread.sleep(200);
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
