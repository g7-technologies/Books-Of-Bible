package com.example.booksofbibles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.booksofbibles.classes.Book;
import com.example.booksofbibles.classes.UserReading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class List_of_books extends AppCompatActivity {
    String[] book_name;
    String[] path;
    Integer[] completion;
    Integer[] id;
    String urladdress = "http://bible.logichouse.com.pk/listOfBooks.php";
    BufferedInputStream is;
    String line = null;
    String result = null;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list_of_books);

        listView = findViewById(R.id.list_book);

        Intent i = getIntent();
        final String result = i.getStringExtra("book_type");


        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView = new CustomListView(this, book_name);
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String number = String.valueOf(id[i]);
                Intent in = new Intent(List_of_books.this,Chapters.class);
                in.putExtra("chapter_id", number);
                in.putExtra("book_type", result);
                startActivity(in);
               // Toast.makeText(List_of_books.this, "id: " +id[i], Toast.LENGTH_LONG).show();
            }
        });
    }

    private void collectData() {
//Connection

        URL url = null;
        try {
            url = new URL(urladdress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            is = new BufferedInputStream(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //content
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line + "\n");
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = sb.toString();
        try {
            JSONArray jsonObject = new JSONArray(result);
            book_name = new String[jsonObject.length()];
            path = new String[jsonObject.length()];
            completion = new Integer[jsonObject.length()];
            id = new Integer[jsonObject.length()];
            for (int i = 0; i < jsonObject.length(); i++ ) {
                book_name[i] =  jsonObject.getJSONObject(i).getString("Book Name");
                path[i] =  jsonObject.getJSONObject(i).getString("image_path");
                completion[i] =  jsonObject.getJSONObject(i).getInt("total_min");
                id[i]=jsonObject.getJSONObject(i).getInt("id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}


