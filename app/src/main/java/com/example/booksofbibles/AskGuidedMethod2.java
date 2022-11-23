package com.example.booksofbibles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AskGuidedMethod2 extends AppCompatActivity {

    Button niv, nkjv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ask_guided_methoed2);

        niv = findViewById(R.id.niv);
        nkjv = findViewById(R.id.nkjv);


        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AskGuidedMethod2.this, List_of_books.class);
                i.putExtra("book_type","1");
                startActivity(i);
            }
        });

        nkjv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AskGuidedMethod2.this, List_of_books.class);
                i.putExtra("book_type","2");
                startActivity(i);
            }
        });
    }

}
