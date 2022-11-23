package com.example.booksofbibles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Challenge extends AppCompatActivity {

    Button redeem;
    String id,days,final_mins,streak_value;
    int streak,total_mins,mins;
    TextView min, day, token, streak_date, total_min_text;
    String counterKey = "Counter Key";
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_challenge);

        min = findViewById(R.id.min_challenge);
        day = findViewById(R.id.days_challenge);
        token = findViewById(R.id.token_challenge);
        streak_date = findViewById(R.id.streak_challenge);
        total_min_text = findViewById(R.id.total_mins);
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");
        SharedPreferences streakDate = getBaseContext().getSharedPreferences("Streak", Context.MODE_PRIVATE);
        streak = streakDate.getInt(counterKey, 0);
        streak_value = String.valueOf(streak);
        streak_date.setText(streak_value);

        BackGround backGround = new BackGround();
        backGround.execute(id);
        redeem = findViewById(R.id.redeem_button);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Challenge.this, FoodFamilyFun.class));
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
                URL url = new URL("http://bible.logichouse.com.pk/getdaysAndMin.php");
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
                        mins = jObj.getInt("mins");
                    days = jObj.getString("days");
                    total_mins = jObj.getInt("total_min");
                    total = ((double) mins / total_mins);
                    total = total*100;
                    final_mins = String.valueOf(total);
                  //  Toast.makeText(getBaseContext(), final_mins, Toast.LENGTH_LONG).show();
                    total_min_text.setText(final_mins + "%");
                    token.setText(jObj.getString("token"));
                    day.setText(days);
                    min.setText(String.valueOf(mins));
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
