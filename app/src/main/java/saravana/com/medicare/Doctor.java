package saravana.com.medicare;

/**
 * Created by saravana on 14/4/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

public class Doctor extends AppCompatActivity {
    public String id, token;
    public static String rend;
    Base64 d;
    UsersAdapter4 adapter;
    public static ArrayList<User4> list23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);
        token = getIntent().getExtras().getString("token");
        id = getIntent().getExtras().getString("id");
        String ds = id + ":" + token;
        final String ens = d.encodeToString(ds.getBytes(), Base64.NO_WRAP);
        Log.i("encoded", ens);
        rend = "Basic " + ens;
        new Doctor23().execute();






    }
    private void fun(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Doctor.this);
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



    private class Doctor23 extends AsyncTask<String, Void, String> {
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
                URL url = new URL("http://" + LoginActivity.base + "/show");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization",Doctor.rend);
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();
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
                    JSONObject j1= null;
                    try {
                        j1 = new JSONObject(s);
                        JSONArray jk=j1.getJSONArray("Appointments");
                        list23=User4.fromJson(jk);
                        ListView l3=(ListView) findViewById(R.id.patientdetails);
                        adapter=new UsersAdapter4(Doctor.this,list23);
                        l3.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    rs2=0;
                    break;

                default:
                    break;

            }}
    }


}
