package com.example.apteka.rejestracja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    private String[] Nazwa;
    private String[] Cena;
    private String[] Ilosc;
    private String[] Koszt;
    private boolean[] Recepta;
    private int[] Ikonka;
    private Context context;

    Adapter(Context context, String[] data1, int[] data2) {
        super();
        this.Nazwa = data1;
        this.Ikonka = data2;
        this.context = context;
    }

    Adapter(Context context, int[] ikona, String[] nazwa, String[] cena, String[] koszt, String[] Ilosc, boolean[] recepta) {
        super();
        this.Ikonka = ikona;
        this.Nazwa = nazwa;
        this.Cena = cena;
        this.Ilosc = Ilosc;
        this.Koszt = koszt;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.list_element, parent, false);

        TextView text =  rowView.findViewById(R.id.text);
        TextView cena =  rowView.findViewById(R.id.text);
        TextView koszt =  rowView.findViewById(R.id.text);
        TextView ilosc =  rowView.findViewById(R.id.text);
        TextView recepta =  rowView.findViewById(R.id.text);
        ImageView icon =  rowView.findViewById(R.id.icon);

        text.setText(Nazwa[position]);
        icon.setImageResource(Ikonka[position]);

        if(Cena[position] != null)
        {
            cena.setText(Cena[position]);
        }
        if(Koszt[position] != null)
        {
            koszt.setText(Koszt[position]);
        }
        if(Ilosc[position] != null)
        {
            ilosc.setText(Ilosc[position]);
        }
        if(Recepta[position])
        {
            recepta.setText("TAK");
        }
        else
        {
            recepta.setText("NIE");
        }

        return rowView;
    }

}