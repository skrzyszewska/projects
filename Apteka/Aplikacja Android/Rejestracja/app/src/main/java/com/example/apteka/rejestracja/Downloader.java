package com.example.apteka.rejestracja;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class Downloader extends AsyncTask<Void,Integer,String>{
    Context c;
    String address;
    ListView lv;
    ProgressDialog pd;

    public Downloader(Context c, String address, ListView lv){
        this.c = c;
        this.address = address;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Fetch data");
        pd.setMessage("Fetching data");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids){
        String data=download_data();
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        pd.dismiss();

        if(s !=null){
        Parser p=new Parser(c,lv,s);
        p.execute();
        }
        else{
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();
        }
    }

    private String download_data(){
    //connect and get a stream
        InputStream is= null;
        String line = null;
        try{
        URL url=new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(con.getInputStream());

            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuffer sb=new StringBuffer();

            if(br != null){
                while((line=br.readLine()) != null){
                    sb.append(line+"\n");
                }
            }
            else{
                return null;
            }
            return sb.toString();

        } catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
            if(is != null){
                is.close();
            }
        }catch(IOException e){
            e.printStackTrace();
            }
        }
    return null;
    }
}
