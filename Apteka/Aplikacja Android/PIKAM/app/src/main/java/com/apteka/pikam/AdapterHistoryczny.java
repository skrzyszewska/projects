package com.apteka.pikam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class AdapterHistoryczny extends BaseAdapter
{
    private HashMap<String, ObiektHistoryczny> Lista;

    private Context context;

    AdapterHistoryczny(Context context, HashMap<String, ObiektHistoryczny> lista)
    {
        super();
        this.Lista = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Lista.size();
    }

    @Override
    public Object getItem(int position)
    {
        int index = 0;
        for(Map.Entry<String, ObiektHistoryczny> entry : Lista.entrySet())
        {
            if(index == position)
            {
                return entry;
            }
            index++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"}) @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.historia_list_element, parent, false);

        TextView status = rowView.findViewById(R.id.status_text);
        TextView data =  rowView.findViewById(R.id.data_text);
        TextView razem =  rowView.findViewById(R.id.razemcena_lista_element);

        int index = 0;
        for(Map.Entry<String, ObiektHistoryczny> entry : Lista.entrySet())
        {
            if(index == position)
            {
                ObiektHistoryczny obiekt = entry.getValue();
                ListView lista = rowView.findViewById(R.id.list_history);
                AdapterObiektuHistorycznego adapter = new AdapterObiektuHistorycznego(context, obiekt);
                lista.setAdapter(adapter);

                status.setText(obiekt.Status);
                if(obiekt.Status.equals("Oczekuje na akceptacje"))
                {
                    setColor(rowView, context, R.color.colorGrey);
                }
                else if(obiekt.Status.equals("Zrealizowane"))
                {
                    status.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    setColor(rowView, context, R.color.colorGreen);
                }
                else if(obiekt.Status.equals("Odrzucone"))
                {
                    status.setTextColor(context.getResources().getColor(R.color.colorRed));
                    setColor(rowView, context, R.color.colorRed);
                }

                data.setText(obiekt.Data);
                razem.setText(obiekt.Razem.toString());
            }
            index++;
        }

        return rowView;
    }

    private void setColor(View row, Context context, int color)
    {
        RelativeLayout nr = row.findViewById(R.id.Nr);
        RelativeLayout zdjecie = row.findViewById(R.id.Zdjecie);
        RelativeLayout nazwa = row.findViewById(R.id.Nazwa);
        RelativeLayout ilosc = row.findViewById(R.id.Ilosc);
        RelativeLayout cena = row.findViewById(R.id.CenaZaSztuke);
        RelativeLayout razem = row.findViewById(R.id.CenaRazem);
        RelativeLayout recepta = row.findViewById(R.id.Recepta);

        nr.setBackgroundColor(context.getResources().getColor(color));
        zdjecie.setBackgroundColor(context.getResources().getColor(color));
        nazwa.setBackgroundColor(context.getResources().getColor(color));
        ilosc.setBackgroundColor(context.getResources().getColor(color));
        cena.setBackgroundColor(context.getResources().getColor(color));
        razem.setBackgroundColor(context.getResources().getColor(color));
        recepta.setBackgroundColor(context.getResources().getColor(color));
    }
}

