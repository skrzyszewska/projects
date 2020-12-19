package com.example.apteka.rejestracja;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;



public class Serializacja{
    private final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pliki/";
    private final static String name_of_file = "ListaLekow";
    private static PrintWriter file = null;
    private static GsonBuilder gsonBuilder = null;
    private static Gson gson = null;
    private static String text = "";
    private static int size = 0;
    private static Scanner scanner = null;

    Serializacja(int size){
        this.size = size;
        utworzFolder();
    }

    @Override
    public String toString(){
        return this.text;
    }

    public void zapisz(Object list){
        gsonBuilder = new GsonBuilder();
        //gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        text = gson.toJson(list);
        Log.d("Message: ", text);

        try{
            file = new PrintWriter(path + name_of_file + ".json");
            Log.d("Message", "Utworzono plik");
        } catch (FileNotFoundException e){
            Log.d("Error", e.getMessage());
        }
        file.println(text);
        file.close();
    }

    public ObiektyTestowe[] odczyt(){
        try{
            File file = new File(path + name_of_file + ".json");
            scanner = new Scanner(file);
            //Usuwanie pliku po odczycie danych z niego
            Boolean deleted = false;
            //deleted = file.delete();

            if(deleted)
                Log.d("Message: ", "Ussunięto plik");
            else
                Log.d("Error", "Nie mozna usunąć pliku");

        } catch (FileNotFoundException e){
            Log.d("Error", e.getMessage());
        }
        text = scanner.nextLine();

        ObiektyTestowe[] lista = null;
        gson = new Gson();

        lista = gson.fromJson(text, ObiektyTestowe[].class);

        return lista;
    }

    private void utworzFolder(){
        File folder11 = new File(path);
        if(!folder11.exists()){
            try{
                folder11.mkdir();
                Log.d("Message", "Success");
            } catch(Exception e){
                Log.d("Error", e.getMessage());
            }
        }
        else
            Log.d("Message", "Folder Exists");
    }
}
