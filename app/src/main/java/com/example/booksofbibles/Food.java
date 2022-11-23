package com.example.booksofbibles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksofbibles.classes.Deal;
import com.example.booksofbibles.classes.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Food extends AppCompatActivity {
    ExpandableListView expandableListView;
    String required_t, value,user_token;
    TextView token, tokenIncentive;
    private List<Deal> dealName;
    CustomExpandableAdapter adapter;
    JSONObject jsonObject;
    Button redeem;
    String deal_token;
    int deal_id;
    String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_food);
        token = findViewById(R.id.token_food);
        tokenIncentive = findViewById(R.id.food_number_tView);
        Intent in = getIntent();
        value = in.getStringExtra("value");
        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        id = settings.getString("ID", "nothing");

        BackGround backGround = new BackGround();
        backGround.execute(id);

        expandableListView = findViewById(R.id.expandable_list_food);

        BackGroundForDeals backGroundForDeals = new BackGroundForDeals();
        backGroundForDeals.execute(value);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                deal_token = String.valueOf(dealName.get(groupPosition).getItemList().get(childPosition).getRequired_token());
                deal_id = dealName.get(groupPosition).getId();
                final String d_id = String.valueOf(deal_id);

                if(Integer.valueOf(user_token) < Integer.valueOf(deal_token)){
                    int num = Integer.valueOf(deal_token) - Integer.valueOf(user_token);
                    Toast.makeText(getBaseContext(), "This item can not be redeem", Toast.LENGTH_LONG).show();
                    tokenIncentive.setText(String.valueOf(num));

                }
                else {

                LayoutInflater factory = LayoutInflater.from(Food.this);
                final View deleteDialogView = factory.inflate(R.layout.congratulation_dialog_box, null);
                redeem = deleteDialogView.findViewById(R.id.redeem);
                redeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences settings = getBaseContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
                        String u_id = settings.getString("ID", "nothing");
                        int minus_token = Integer.valueOf(user_token) - Integer.valueOf(deal_token);
                        user_token = String.valueOf(minus_token);
                        BackGroundForRedeemToken backGroundForRedeemToken = new BackGroundForRedeemToken();
                        backGroundForRedeemToken.execute(u_id,d_id,user_token);
                        LayoutInflater factory = LayoutInflater.from(Food.this);
                        final View viewRedeem = factory.inflate(R.layout.congratulation_barcode, null);
                        ImageView cross = view.findViewById(R.id.cross);
                        cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Food.this,Food.class));
                            }
                        });
                        final AlertDialog deleteDialog = new AlertDialog.Builder(Food.this).create();
                        deleteDialog.setView(viewRedeem);
                        deleteDialog.setCancelable(false);
                        deleteDialog.show();
                    }
                });
                final AlertDialog deleteDialog = new AlertDialog.Builder(Food.this).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialog.setCancelable(false);
                deleteDialog.show();
                }
                return true;
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
                jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error){
                    user_token = jsonObject.getString("token");
                    token.setText(user_token);
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
    private Deal createDeal(String name, int id) {
        return new Deal(id, name);
    }
    private List<Token> createItems(String required_token) {
        List<Token> result = new ArrayList<Token>();

        for (int i=0; i < 10; i++) {
            Token item = new Token(required_token);
            result.add(item);
        }

        return result;
    }
    class BackGroundForDeals extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/getFamilyDeal.php");
                String urlParams = "category=" + name;

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
                   // Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                JSONArray jObj = new JSONArray(response);
                dealName = new ArrayList<Deal>();
                for (int i = 0; i < jObj.length(); i++) {
                    required_t = jObj.getJSONObject(i).getString("required_token");
                    Deal deal = createDeal(jObj.getJSONObject(i).getString("name"), jObj.getJSONObject(i).getInt("deal_id"));
                    deal.setItemList(createItems(required_t));
                    dealName.add(deal);
                    adapter = new CustomExpandableAdapter(dealName, getBaseContext());
                    expandableListView.setAdapter(adapter);
                    //Toast.makeText(getBaseContext(), childData[i],Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {

                e.printStackTrace();
                Log.e("Json expection", e.getMessage());
            }
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
    class BackGroundForRedeemToken extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String u_id = params[1];
            String user_token = params[2];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://bible.logichouse.com.pk/redeemToken.php");
                String urlParams = "u_id=" + name + "&d_id=" + u_id + "&token=" + user_token;

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
//           if (response.equals("Data Inserted")){
//               Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
//           }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }
    }

}
