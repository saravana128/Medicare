package saravana.com.medicare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PatientRecord extends AppCompatActivity {
    public String id,token;
    public static String rend;
    Base64 d;
    public static ArrayList<User2> audi1;
    UsersAdapter2 adapter;
    public ListView l2;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        token = getIntent().getExtras().getString("token");
        id = getIntent().getExtras().getString("id");
        String ds = id + ":" + token;
        final String ens = d.encodeToString(ds.getBytes(), Base64.NO_WRAP);
        Log.i("encoded", ens);
        rend = "Basic " + ens;
        done = (Button) findViewById(R.id.done31);
        l2=(ListView) findViewById(R.id.tgh);
        
    }
@Override
protected void onStart()
{
    super.onStart();
    new LoadEvent().execute();
    
    done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sa = null;
            for(int i=0;i<adapter.getCount();i++)
            {
                User2 ua = adapter.getItem(i);
                if(ua.check){
                    if(sa==null)
                    {sa=ua.id;}
                    else {
                        sa = sa + "," + ua.id;
                    }
                    
                }
            }
            Log.i("st",sa);

            new Symptom().execute(sa);

        }
    });
    
    
}



    class LoadEvent extends AsyncTask<String, Void, String> {
        int rs2;


        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String otp = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://" + LoginActivity.base + "/symptoms");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", PatientRecord.rend);
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();
                rs2 = urlConnection.getResponseCode();
                Log.i("Response", String.valueOf(rs2));

                // Read the input stream into a String

                InputStream error;
                if (rs2 == 422) {
                    error = urlConnection.getErrorStream();
                    StringBuilder buffer = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    otp = buffer.toString();
                    Log.i("json", "hiiiiiiiiiiiiiiiiiiii");
                    return otp;

                } else {
                    error = urlConnection.getInputStream();
                    StringBuilder buffer = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    otp = buffer.toString();
                    return otp;
                }

            } catch (IOException e) {

                e.printStackTrace();
                return otp;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //pb6.setVisibility(View.INVISIBLE);
            //ls.setVisibility(View.VISIBLE);
            super.onPostExecute(s);


            switch (rs2) {
                case 422:
                    JSONObject err = null;
                    try {
                        err = new JSONObject(s);
                        String str = err.getString("message");
                        fun(str);
                        rs2 = 0;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


                case 200:
                case 201:
                    super.onPostExecute(s);
                    try {
                        Log.i("string",s);
                        //JSONObject j1= new JSONObject(s);
                        JSONArray a1 = new JSONArray(s);

                        audi1= User2.fromJson(a1);

                        adapter=new UsersAdapter2(PatientRecord.this,audi1);
                        l2.setAdapter(adapter);
                        
                        
                        
                        
                       /* viewPager = (ViewPager) findViewById(R.id.viewpager);
                        setupViewPager(viewPager);
                        tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                        toolbar = (Toolbar) findViewById(R.id.toolbar3);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setTitle("SaecAudi");
                        */




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.i("json", s);
                    rs2 = 0;
                    break;
                default:
                    fun("OOPS ! Something went Wrong Try Again");
                    break;

            }
        }
    }

    private void fun(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientRecord.this);
        builder.setTitle(msg);
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private class Symptom extends AsyncTask<String, Void, String> {
        int rs2;


        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String stud = params[0];

            // Will contain the raw JSON response as a string.
            String otp = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://" + LoginActivity.base + "/diseases");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization",PatientRecord.rend);
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();
                JSONObject cred = new JSONObject();
                try {
                    cred.put("symptom_id", stud);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cred.toString());
                wr.flush();
                rs2 = urlConnection.getResponseCode();
                Log.i("Response", String.valueOf(rs2));

                // Read the input stream into a String

                InputStream error ;
                if (rs2 == 422){
                    error=urlConnection.getErrorStream();
                    StringBuilder buffer = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    otp = buffer.toString();
                    Log.i("json", "hiiiiiiiiiiiiiiiiiiii");
                    return otp;

                } else {
                    error=urlConnection.getInputStream();
                    StringBuilder buffer = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    otp = buffer.toString();
                    Log.i("json", "hiiiiiiiiiiiiiiiiiiii");
                    return otp;
                }

            } catch (IOException e) {

                e.printStackTrace();
                return otp;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {

            switch (rs2)
            {
                case 422:   Log.i("won",s);
                    JSONObject err= null;
                    try {
                        err = new JSONObject(s);
                        String str = err.getString("message");
                        fun(str);
                        rs2=0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case 200:
                case 201:
                    Log.i("won",s);
                    super.onPostExecute(s);
                    Intent nk=new Intent(PatientRecord.this,QuestionActivity.class );
                    nk.putExtra("val",s);
                    startActivity(nk);
                    rs2=0;
                    break;

                default:
                    break;

            }}
    }

}
