package saravana.com.medicare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

import static saravana.com.medicare.QuestionActivity.audi3;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double lat;
    public double lon;

    public String did="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new Doctor().execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng vellore = new LatLng(12.972643,79.158170);
        mMap.addMarker(new MarkerOptions().position(vellore).title("Doctor1"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(12.972652,79.158177)).title("Doctor2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vellore));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, AppointmentActivity.class);
                i.putExtra("title",marker.getTitle());
                startActivity(i);

            }
        });


    }






    private void fun(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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



    private class Doctor extends AsyncTask<String, Void, String> {
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
                    cred.put("location", "12.972652:79.158177");
                    cred.put("disease",did);


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

                    try {
                        JSONObject jk=new JSONObject(s);
                        JSONArray j1=jk.getJSONArray("Doctors");
                        for(int i=0;i<j1.length();i++)
                        {
                            JSONObject j6=j1.getJSONObject(i);
                            lat=Double.parseDouble(j6.getString("lat"));
                            lon=Double.parseDouble(j6.getString("lon"));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(j6.getString("name")));




                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    rs2=0;
                    break;

                default:
                    break;

            }}
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
