package com.example.booksofbibles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Chapters extends AppCompatActivity {


int totalCount;
String chapter, verse,book;
    GridView gridLayout;
     String[] chapterNumber;
     String[] paragraph_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chapters);
        gridLayout = findViewById(R.id.grid);
        Intent intent = getIntent();
        chapter = intent.getStringExtra("chapter_id");
        book = intent.getStringExtra("book_type");
        BackGround backGround = new BackGround();
        backGround.execute(chapter);


        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String p_id = String.valueOf(paragraph_id[i]);
               // Toast.makeText(getBaseContext(),p_id,Toast.LENGTH_LONG).show();
                Intent in = new Intent(Chapters.this,Verses.class);
                in.putExtra("paragraph_id", p_id);
                in.putExtra("book_type",book);
                startActivity(in);

            }
        });
    }

    class BackGround extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String number = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/getParagraphCount.php");
                String urlParams = "number=" + number;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
//
                JSONArray jObj = new JSONArray(response);
//                boolean error = jObj.getJSONObject().getBoolean("error");
//                if (!error){
//                    totalCount = jObj.getInt("number");
//                    verse = jObj.getString("id");
                    chapterNumber = new String[jObj.length()];
                    paragraph_id = new String[jObj.length()];
                    for (int i=0; i<jObj.length(); i++){
    //                    chapterNumber[i] = jObj.getJSONObject(i).getString("number");//String.valueOf(i + 1);
                        chapterNumber[i] = String.valueOf(i + 1);
                        paragraph_id[i] = jObj.getJSONObject(i).getString("id");
                    }

                    final AdapterForGrid adapter = new AdapterForGrid(chapterNumber,getBaseContext());
                    gridLayout.setAdapter(adapter);
 //               }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Json exception",e.getMessage());
            }

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

}
