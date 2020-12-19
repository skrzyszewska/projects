package com.apteka.pikam;

import android.content.Context;

import java.util.ArrayList;

public class ObiektHistoryczny
{
    public ArrayList<String> Nazwa;
    public ArrayList<Boolean> Recepta;
    public ArrayList<Double> Cena;
    public ArrayList<Double> CenaRazem;
    public ArrayList<Integer> Ilosc;
    public ArrayList<Integer> Ikonka;
    public ArrayList<Integer> ID;

    public String Status;
    public String Data;
    public Double Razem;

    public ObiektHistoryczny()
    {
        Nazwa = new ArrayList<>();
        Recepta = new ArrayList<>();
        Cena = new ArrayList<>();
        CenaRazem = new ArrayList<>();
        Ilosc = new ArrayList<>();
        Ikonka = new ArrayList<>();
        ID = new ArrayList<>();
        Razem = 0d;
    }
}
