package com.example.apteka.rejestracja;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apteka.rejestracja.PojedynczyLek;
import com.example.apteka.rejestracja.R;

import java.util.List;


public class LekiAdapter extends ArrayAdapter<PojedynczyLek>{
    int resource;
    Context context;

    public LekiAdapter(Context context, int resource, List<PojedynczyLek> items){
        super(context, resource, items);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final LinearLayout contactsView;
        PojedynczyLek pojedynczyLek = getItem(position);

        if(convertView==null){
            contactsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, contactsView, true);
        }
        else{
            contactsView = (LinearLayout) convertView;
        }
        TextView nazwa =(TextView)contactsView.findViewById(R.id.NazwaLeku);
        TextView cena=(TextView)contactsView.findViewById(R.id.Cena);
        ImageView plus = (ImageView) contactsView.findViewById(R.id.plusik);

        nazwa.setText(pojedynczyLek.getNazwa());
        cena.setText(pojedynczyLek.getCena());
        return contactsView;
    }
}
