package com.apteka.pikam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter
{
    private String[] Nazwa;
    private boolean[] Recepta;
    private int[] Cena;
    private int[] Ilosc;
    private int[] Ikonka;
    private Context context;

    Adapter(Context context, String[] data1, int[] data2)
    {
        super();
        this.Nazwa = data1;
        this.Ikonka = data2;
        this.context = context;

        this.Cena = null;
        this.Ilosc = null;
        this.Recepta = null;
    }

    Adapter(Context context, int[] ikona, String[] nazwa, int[] cena, int[] ilosc, boolean[] recepta)
    {
        super();
        this.Ikonka = ikona;
        this.Nazwa = nazwa;
        this.Cena = cena;
        this.Ilosc = ilosc;
        this.Recepta = recepta;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Nazwa.length;
    }

    @Override
    public Object getItem(int position) {
        return Nazwa[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n") @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.list_element, parent, false);

        TextView text =  rowView.findViewById(R.id.text);
        TextView cena =  rowView.findViewById(R.id.cena);
        TextView ilosc =  rowView.findViewById(R.id.ilosc);
        TextView recepta =  rowView.findViewById(R.id.recepta);
        ImageView icon =  rowView.findViewById(R.id.icon);

        text.setText(Nazwa[position]);
        icon.setImageResource(Ikonka[position]);

        if(Cena != null)
        {
            cena.setText("Cena: " + Cena[position] + " zl");
        }
        if(Ilosc != null)
        {
            ilosc.setText("Ilosc: " + Ilosc[position] + " szt.");
        }
        if(Recepta != null && Recepta[position])
        {
            recepta.setText("Na Recepte!");
        }

        return rowView;
    }
}

