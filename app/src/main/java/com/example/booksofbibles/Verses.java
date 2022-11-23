package com.example.booksofbibles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Verses extends AppCompatActivity {
    int totalCount;
    String verse,book;
    GridView gridLayout;
    SharedPreferences sharedpreferences;
    public static final String verses = "verse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verses);

        gridLayout = findViewById(R.id.grid_verses);
        Intent in = getIntent();
        verse = in.getStringExtra("paragraph_id");
        book = in.getStringExtra("book_type");
        BackGround backGround = new BackGround();
        backGround.execute(verse);

        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(Verses.this,Open_book.class);
                intent.putExtra("book_type",book);
                intent.putExtra("chapter_id",verse);
                sharedpreferences = getSharedPreferences(verses,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("verse_number",verse);
                editor.apply();
                startActivity(intent);
            }
        });
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String number = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/getVerseCount.php");
                String urlParams = "verse=" + number;

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
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error){
                    totalCount = jObj.getInt("verse");
                    final String[] chapterNumber = new String[totalCount];
                    for (int i=0; i<totalCount; i++){
                        chapterNumber[i] = String.valueOf(i + 1);
                    }

                    final AdapterForVerses adapter = new AdapterForVerses(chapterNumber,getBaseContext());
                    gridLayout.setAdapter(adapter);

                }
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
