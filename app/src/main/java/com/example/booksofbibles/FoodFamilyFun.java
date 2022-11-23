package com.example.booksofbibles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FoodFamilyFun extends AppCompatActivity {
    LinearLayout food, family, fun;
    String id;
    TextView token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_food_family_fun);

        food = findViewById(R.id.food_layout);
        fun = findViewById(R.id.fun_layout);
        family = findViewById(R.id.family_layout);
        token = findViewById(R.id.token_fff);
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");
        BackGround backGround = new BackGround();
        backGround.execute(id);


        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodFamilyFun.this,Food.class);
                intent.putExtra("value","1");
                startActivity(intent);
            }
        });
        fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodFamilyFun.this,Food.class);
                intent.putExtra("value","3");
                startActivity(intent);
            }
        });
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodFamilyFun.this,Food.class);
                intent.putExtra("value","2");
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
                URL url = new URL("http://bible.logichouse.com.pk/gettokens.php");
                String urlParams = "id=" + number;

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
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error){
                    token.setText(jObj.getString("token"));
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
