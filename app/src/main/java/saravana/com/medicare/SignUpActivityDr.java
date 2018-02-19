package saravana.com.medicare;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivityDr extends AppCompatActivity {
    EditText name, username, pswd, qual, certNum, hospName;
    Spinner gen, spec;
    public String dname, duser, dpswd, dqual, dcertNum, dhospName, dgen, dspec;
    Button sign;
    public String latlon,id,token;
    public static String rend;
    Base64 d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dr);
        sign = (Button) findViewById(R.id.signupdrbtn);
        name = (EditText) findViewById(R.id.dname);
        username = (EditText) findViewById(R.id.dusername);
        pswd = (EditText) findViewById(R.id.dpswd);
        qual = (EditText) findViewById(R.id.dqual);
        certNum = (EditText) findViewById(R.id.dcert);
        hospName = (EditText) findViewById(R.id.dhosp);
        gen = (Spinner) findViewById(R.id.spinner2);
        spec = (Spinner) findViewById(R.id.speciality);



        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    dname = name.getText().toString();
                    duser = username.getText().toString();
                    dpswd = pswd.getText().toString();
                    dqual = qual.getText().toString();
                    dcertNum = certNum.getText().toString();
                    dhospName = hospName.getText().toString();
                    dgen = gen.getSelectedItem().toString();
                    dspec = String.valueOf(spec.getSelectedItemId());
                    //latlon = getLocation();
                    new Auth().execute();





            }
        });


    }

    private class Auth extends AsyncTask<String, String, String> {
        int rs2;


        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            String resp = null;
            HttpURLConnection urlConnection2 = null;
            BufferedReader reader2 = null;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url2 = new URL("http://" + LoginActivity.base + "/auth/doc_reg");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.setRequestMethod("POST");
                urlConnection2.setDoInput(true);
                urlConnection2.setDoOutput(true);
                urlConnection2.setRequestProperty("Content-Type", "application/json");
                urlConnection2.setRequestProperty("Accept", "application/json");
                urlConnection2.setReadTimeout(10000);
                urlConnection2.connect();
                // Read the input stream into a String
                JSONObject cred = new JSONObject();
                try {
                    cred.put("lat", "hj");
                    cred.put("lon", "ll");
                    cred.put("name", dname);
                    cred.put("user_name", duser);
                    cred.put("password", dpswd);
                    cred.put("speciality_id", dspec);
                    cred.put("gender", dgen);
                    cred.put("qualification", dqual);
                    cred.put("cert_id", dcertNum);
                    cred.put("hospital_name", dhospName);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection2.getOutputStream());
                wr.write(cred.toString());
                wr.flush();


                rs2 = urlConnection2.getResponseCode();
                Log.i("response", String.valueOf(rs2));
                InputStream error;
                if (rs2 == 422) {
                    error = urlConnection2.getErrorStream();
                    StringBuilder buffer = new StringBuilder();
                    reader2 = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader2.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    resp = buffer.toString();
                    Log.i("json", "hiiiiiiiiiiiiiiiiiiii");
                    return resp;

                } else {
                    error = urlConnection2.getInputStream();
                    StringBuilder buffer = new StringBuilder();
                    reader2 = new BufferedReader(new InputStreamReader(error));
                    String line;
                    while ((line = reader2.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    resp = buffer.toString();
                    Log.i("json", "hiiiiiiiiiiiiiiiiiiii");
                    return resp;
                }
            } catch (IOException e) {
                e.printStackTrace();
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection2 != null) {
                    urlConnection2.disconnect();
                }
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String mm) {
            super.onPostExecute(mm);
            switch (rs2) {
                case 200:
                case 201:
                    try {

                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            try{
                                Log.i("word",mm);
                                JSONObject j2 = new JSONObject(mm);
                                String token = j2.getString("token");
                                JSONObject jk = j2.getJSONObject("user");


                                String id = jk.getString("id");
                                String role=jk.getString("role_id");
                                int result = Integer.parseInt(role);
                                super.onPostExecute(mm);
                                switch(result) {
                                    case 2:Intent ik = new Intent(SignUpActivityDr.this, PatientRecord.class);
                                        ik.putExtra("token", token);
                                        ik.putExtra("id", id);
                                        startActivity(ik);
                                        break;
                                    case 1:Intent it = new Intent(SignUpActivityDr.this, Doctor.class);
                                        it.putExtra("token", token);
                                        it.putExtra("id", id);
                                        startActivity(it);
                                        break;
                                    default:fun("You are not Authorized to Use this Application");
                                        break;
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch(NullPointerException m)
                            {
                                //here to handle time out
                                fun("something went wrong! Try again");
                                m.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
                        }

                    } catch (NullPointerException m) {
                        m.printStackTrace();
                    }
                    break;
                case 422:
                    JSONObject err = null;
                    try {
                        err = new JSONObject(mm);
                        String str = err.getString("message");
                        fun(str);
                        Log.i("word", mm);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default: //startActivity(new Intent(Welcome.this,ServerError.class));
                    break;


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "Fetching data", Toast.LENGTH_SHORT).show();
            new Auth().execute();
        } else {
            fun("You cannot proceed Without Location Service ");

        }
    }

    private void fun(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivityDr.this);
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
    @SuppressWarnings("MissingPermission")

    public String getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                String lat= String.valueOf(lastKnownLocationGPS.getLatitude());
                String lon= String.valueOf(lastKnownLocationGPS.getLongitude());
                String locat=lat+":"+lon;
                return locat;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if(loc !=null) {
                    String lat = String.valueOf(loc.getLatitude());
                    String lon = String.valueOf(loc.getLongitude());
                    String locat = lat + ":" + lon;
                    return locat;
                }else{return "null";}

            }
        } else {
            return "null";
        }
    }




}
