package com.apteka.pikam;

import android.text.format.Formatter;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class ConnectToDataBase
{
    public JSONArray Execute(String query)
    {
        if(!query.contains("SELECT"))
        {
            sendLegToDB(query);
        }
        return sendToDB(query);
    }

    public boolean SendEMail(String id, String login)
    {
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://212.182.24.105:8010/?page_id=1307&id="+id+"&login="+login);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            entity.getContent();
            return true;
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        return false;
    }

    private String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                    {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLocalIpAddress2()
    {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.d("***** IP=", ip);
                        return ip;
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            Log.e("Error", ex.toString());
        }
        return null;
    }

    private JSONArray sendToDB(String query)
    {
        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("query", query));
        InputStream is = null;

        Log.d("QUERY", query);

//http post
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://212.182.24.105:8010/db_connect/get.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
//convert response to string
        try
        {
            assert is != null;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
            is.close();
            result=sb.toString();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        try
        {
            return new JSONArray(result);
        }
        catch(JSONException e)
        {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
        return null;
    }

    private void sendLegToDB(String query)
    {
        query = query.replace("'", "");
        query += " ";

        String accountType = "Gosc", userID = "";
        if(LoginActivity.UserID != null && LoginActivity.UserID.length() > 0 &&
                LoginActivity.UserType != null && LoginActivity.UserType.length() > 0)
        {
            accountType = LoginActivity.UserType;
            userID = LoginActivity.UserID;
        }

        SimpleDateFormat FormatDaty = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.GERMANY);
        Date AktualnaData = Calendar.getInstance().getTime();
        String data = FormatDaty.format(AktualnaData);
        String ipAdress = getLocalIpAddress2();
        String user = userID.length() > 0 ? accountType + "_" + userID : accountType;

        String logQuery = "INSERT INTO Logi (Logi.Query, Logi.Platforma, Logi.Uzytkownik, Logi.IP, Logi.Data) VALUES ('" + query + "','Andorid','" + user + "','" + ipAdress + "','" + data + "')";

        sendToDB(logQuery);
    }
}

