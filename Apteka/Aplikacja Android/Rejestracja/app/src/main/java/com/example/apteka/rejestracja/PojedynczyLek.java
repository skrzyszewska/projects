package com.example.apteka.rejestracja;


public class PojedynczyLek{
    private String nazwa;
    private String cena;

    public PojedynczyLek(String nazwa,String cena){
        this.nazwa = nazwa;
        this.cena = cena;
    }

    public String getNazwa(){
        return nazwa;
    }

    public String getCena(){
        return cena;
    }

}
