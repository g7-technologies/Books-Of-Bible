package com.example.booksofbibles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUp extends AppCompatActivity {

    EditText name, phone, email;
    TextView total_min, challengerCount;
    Button signup;
    Context ctx = this;
    ProgressDialog progressDialog;
    String urladdress = "http://bible.logichouse.com.pk/getNumberOfUsers.php";
    BufferedInputStream is;
    String line = null;
    String result = null;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.ed_name);
        email = findViewById(R.id.ed_email);
        phone = findViewById(R.id.ed_phone);
        signup = findViewById(R.id.button);
        total_min = findViewById(R.id.total_mins);
        challengerCount = findViewById(R.id.challenger_count);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        getData();

    }
    private void getData() {
//Connection

        URL url = null;
        try {
            url = new URL(urladdress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            if (url != null) {
                con = (HttpURLConnection) url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
                con.setRequestMethod("GET");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
                is = new BufferedInputStream(con.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line).append("\n");
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = sb.toString();
        try {
            JSONObject jObj = new JSONObject(result);
            boolean error = jObj.getBoolean("error");
            if (!error){
                total_min.setText(jObj.getString("mins"));
                challengerCount.setText(jObj.getString("numbers"));

            } else {

                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    public void editText_saveinfo(View view) {
        String n, e, p;
        String method = "register";
        n = name.getText().toString();
        e = email.getText().toString();
        p = phone.getText().toString();

        BackGround backGround = new BackGround();
        backGround.execute(n, e, p);

    }

    class BackGround extends AsyncTask<String, String, String> {
        SharedPreferences sharedpreferences;
        public static final String mypreference = "mypref";
        ProgressDialog pDialog;
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String phone = params[2];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/Signup.php");
                String urlParams = "name=" + name + "&email=" + email + "&phone=" + phone;

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

                    sharedpreferences = getSharedPreferences(mypreference,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("ID",jObj.getString("id"));
                    editor.putString("Name",jObj.getString("name"));
                    editor.putString("Email",jObj.getString("email"));
                    editor.putString("Phone",jObj.getString("phone"));
                    editor.putString("Status",jObj.getString("status"));


                    editor.apply();
                   // Toast.makeText(getBaseContext(),"Data Saved", Toast.LENGTH_LONG).show();
                    // Launch main activity
                    Intent intent = new Intent(SignUp.this,AskGuidedMethod.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login. Get the error message

                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Json expection",e.getMessage());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
//            pDialog = new ProgressDialog(getBaseContext());
//            pDialog.setMessage("Loading");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }
    }

}
