package com.example.booksofbibles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import static com.example.booksofbibles.SignUp.BackGround.mypreference;

public class Open_book extends AppCompatActivity {

    CircularView circularView;
    Toolbar toolbar;
    String sharedName = "Streak";
    String dateKey = "Date Key";
    String counterKey = "Counter Key";
    String checkDate = "Check date";
    String[] book_name;
    TextView verses;
    String verse_number ;
    ImageView forward, cross;
    int number = 0;
    int token = 1;
    String id,streak_value, verseNumber;
    TextView challengerCountTxt;
    int streak;
    Button continue_btn;
    SharedPreferences sharedpreferences;
    public static final String verse = "verse";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_open_book);
        challengerCountTxt = findViewById(R.id.challenger_count);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        verses = findViewById(R.id.verse);
        forward = findViewById(R.id.forward);
        SharedPreferences streakDate = getBaseContext().getSharedPreferences("Streak", Context.MODE_PRIVATE);
        streak = streakDate.getInt(counterKey, 0);
        streak_value = String.valueOf(streak);
        challengerCountTxt.setText(streak_value);
        checkDate();
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");
        Intent i = getIntent();
        final String result = i.getStringExtra("chapter_id");
        SharedPreferences sharedForVerse = getBaseContext().getSharedPreferences(verse, Context.MODE_PRIVATE);
        verseNumber = sharedForVerse.getString("verse_number", "0");
        BackGround backGround = new BackGround();
        backGround.execute(verseNumber);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.insight)
                {
                    startActivity(new Intent(Open_book.this, Challenge.class));
                    circularView.pauseTimer();
                }
                return false;
            }
        });
        CircularView.OptionsBuilder builderWithTimer =
                new CircularView.OptionsBuilder()
                        .shouldDisplayText(true)
                        .setCounterInSeconds(20)
                        .setCircularViewCallback(new CircularViewCallback() {
                            @Override
                            public void onTimerFinish() {
                                LayoutInflater factory = LayoutInflater.from(Open_book.this);
                                final View deleteDialogView = factory.inflate(R.layout.congratulation_custom_dialog, null);
                                cross = deleteDialogView.findViewById(R.id.cross);
                                continue_btn = deleteDialogView.findViewById(R.id.continue_btn);
                                continue_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(Open_book.this, Open_book.class));
                                    }
                                });
                                cross.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(Open_book.this, Open_book.class));
                                    }
                                });
                                final AlertDialog deleteDialog = new AlertDialog.Builder(Open_book.this).create();
                                deleteDialog.setView(deleteDialogView);
                                deleteDialog.setCancelable(false);
                                deleteDialog.show();
                                streak();
                                insertToken();
                                insertUserReading(result);
                            }

                            @Override
                            public void onTimerCancelled() {

                                // Will be called if stopTimer is called
                                Toast.makeText(Open_book.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });

        circularView.setOptions(builderWithTimer);

        circularView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circularView.startTimer();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public void insertToken(){
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");
        BackGroundForInsertion backGroundForInsertion = new BackGroundForInsertion();
        backGroundForInsertion.execute(String.valueOf(id),String.valueOf(token));

    }
    public void insertUserReading(String ids){
//        Intent i = getIntent();
//        String result = i.getStringExtra("value");
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        String u_id = settings.getString("ID", "nothing");
        BackGroundForUserreading backGroundForUserreading = new BackGroundForUserreading();
        backGroundForUserreading.execute(u_id,ids);

    }
    public void streak() {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Calendar c = Calendar.getInstance();

        int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR

        int lastDay = sharedPreferences.getInt(dateKey, 0); //If we don't have a saved value, use 0.

        int counterOfConsecutiveDays = sharedPreferences.getInt(counterKey, 0); //If we don't have a saved value, use 0.

        if (lastDay == thisDay - 1) {
            // CONSECUTIVE DAYS
            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;

            editor.putInt(dateKey, thisDay);

            editor.putInt(counterKey, counterOfConsecutiveDays);
            editor.apply();
            //Toast.makeText(Open_book.this, "Streak Saved ", Toast.LENGTH_SHORT).show();

        } else {

            editor.putInt(dateKey, thisDay);

            editor.putInt(counterKey, 1);
            editor.apply();
            Toast.makeText(Open_book.this, "Streak Saved ", Toast.LENGTH_SHORT).show();

        }
    }
    public void checkDate(){
        circularView = findViewById(R.id.circular_view);
        Calendar calendar = Calendar.getInstance();
            SharedPreferences settings = getBaseContext().getSharedPreferences(checkDate, Context.MODE_PRIVATE);
            int lastDate = Integer.valueOf(settings.getString("lastDate", "0"));
            if (lastDate == calendar.get(Calendar.DAY_OF_YEAR)){
                circularView.setEnabled(false);
                Toast.makeText(getBaseContext(),"You have already earned today's reward.", Toast.LENGTH_LONG).show();
            }
            else if(String.valueOf(lastDate) == "0") {
                SharedPreferences sPreferences = getSharedPreferences(checkDate, Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sPreferences.edit();
                lastDate = calendar.get(Calendar.DAY_OF_YEAR);
                sharedEditor.putString("lastDate",String.valueOf(lastDate));
                sharedEditor.apply();
                circularView.setEnabled(true);
                circularView.startTimer();

            }
            else {
                circularView.setEnabled(true);
                circularView.startTimer();
                SharedPreferences sPreferences = getSharedPreferences(checkDate, Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sPreferences.edit();
                lastDate = calendar.get(Calendar.DAY_OF_YEAR);
                sharedEditor.putString("lastDate",String.valueOf(lastDate));
                sharedEditor.apply();

            }
}
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String number = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/getVerses.php");
                String urlParams = "paragraph=" + number;

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
                final JSONArray jsonObject = new JSONArray(response);
                book_name = new String[jsonObject.length()];
                    for (int i = 0; i < jsonObject.length(); i++ ) {
                        book_name[i] =  jsonObject.getJSONObject(i).getString("verse");
                    }
                verse_number = book_name[number];
                verses.setText(verse_number);
                forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        number = number + 1;
                        verse_number = book_name[number];
                        verses.setText(verse_number);
                        if (number +1 == book_name.length) {
                            forward.setEnabled(false);
                            Toast.makeText(getBaseContext(), "Chapter Ended", Toast.LENGTH_LONG).show();
                        }

                    }
                });
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
    class BackGroundForInsertion extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String number = params[0];
            String token_number = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/updateToken.php");
                String urlParams = "u_id=" + number + "&token=" + token_number;

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

//            if (response.equals("Data Inserted")){
//                //Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
//            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }
    class BackGroundForUserreading extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String number = params[0];
            String token_number = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/insertUserReading.php");
                String urlParams = "u_id=" + number + "&b_id=" + token_number;

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

//            if (response.equals("Data Inserted")){
//                Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
//            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(Open_book.this, Open_book.class));
    }
}
