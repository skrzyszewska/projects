package com.apteka.pikam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterObiektuHistorycznego extends BaseAdapter
{
    private ArrayList<String> Nazwa;
    private ArrayList<Boolean> Recepta;
    private ArrayList<Double> Cena;
    private ArrayList<Double> CenaRazem;
    private ArrayList<Integer> Ilosc;
    private ArrayList<Integer> Ikonka;
    private ArrayList<Integer> Nr;
    private ArrayList<Integer> ID;

    private Context context;

    AdapterObiektuHistorycznego(Context context, ObiektHistoryczny obiekt)
    {
        super();
        this.context = context;

        int nr = 1;
        Nr = new ArrayList<>(obiekt.Nazwa.size());
        for(int i = 0; i < obiekt.Nazwa.size(); i++)
        {
            Nr.add(nr);
            nr++;
        }

        Ikonka = obiekt.Ikonka;
        Nazwa = obiekt.Nazwa;
        Ilosc = obiekt.Ilosc;
        Cena = obiekt.Cena;
        CenaRazem = obiekt.CenaRazem;
        Recepta = obiekt.Recepta;
        ID = obiekt.ID;
    }

    @Override
    public int getCount() {
        return Nazwa.size();
    }

    @Override
    public Object getItem(int position) {
        return Nazwa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"}) @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.content_history_list_element, parent, false);

        TextView nr = rowView.findViewById(R.id.nr_list_element);
        ImageView icon =  rowView.findViewById(R.id.zdjecie);
        Button text =  rowView.findViewById(R.id.nazwa_lista_element);
        TextView ilosc =  rowView.findViewById(R.id.ilosc_lista_element);
        TextView cena =  rowView.findViewById(R.id.cena_lista_element);
        TextView cenaRazem =  rowView.findViewById(R.id.razem_lista_element);
        TextView recepta =  rowView.findViewById(R.id.recepta_lista_element);

        nr.setText(Nr.get(position).toString());
        icon.setImageResource(Ikonka.get(position));
        text.setText(Nazwa.get(position));
        ilosc.setText(Ilosc.get(position).toString());
        cena.setText(Cena.get(position).toString() + " zl");
        cenaRazem.setText(CenaRazem.get(position).toString() + " zl");
        if(Recepta.get(position))
        {
            recepta.setText("Tak!");
            recepta.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
        else
        {
            recepta.setText("Nie");
        }

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PojedynczyLekActivity.class);
                intent.putExtra("Name", Nazwa.get(position));
                intent.putExtra("ID", ID.get(position));
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
