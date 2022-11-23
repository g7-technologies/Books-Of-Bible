package com.example.booksofbibles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");
        if (id.equals("nothing")){
            Thread myThread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        Intent i = new Intent(getApplicationContext(),SignUp.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            myThread.start();
        }
        else {
            startActivity(new Intent(MainActivity.this, Sample.class));
        }


    }
}
