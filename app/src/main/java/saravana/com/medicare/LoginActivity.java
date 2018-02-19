package saravana.com.medicare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText name2,pswd2;
    Button login,ntmem;
    String lname,lpswd;
    public static String base="139.59.67.71/healthcare/public";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name2=(EditText) findViewById(R.id.luser);
        pswd2=(EditText) findViewById(R.id.lpswd);
        login=(Button) findViewById(R.id.login);
        ntmem=(Button) findViewById(R.id.ntmem);



        ntmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k=new Intent(LoginActivity.this,SignUpActivityOne.class);
                startActivity(k);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



               lname=name2.getText().toString();
                lpswd=pswd2.getText().toString();
                new Login().execute(lname,lpswd);
            }
        });





    }


    private class Login extends AsyncTask<String, Void, String> {
        int rs2;


        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String phon = params[0];
            String otps=params[1];

            // Will contain the raw JSON response as a string.
            String otp = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://" + LoginActivity.base + "/auth/login");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();
                JSONObject cred = new JSONObject();
                try {
                    cred.put("user_name", phon);
                    cred.put("password",otps);
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
        protected void onPostExecute(String mm) {

            Log.i("response", String.valueOf(rs2));
            if(rs2==0||rs2>400)
            {
                if(rs2==422)
                {
                    JSONObject err= null;
                    try {
                        err = new JSONObject(mm);
                        String str = err.getString("message");
                        fun(str);
                        Log.i("word",mm);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    //startActivity(new Intent(Next.this,ServerError.class));
                }
            }
            else
            {
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
                        case 2:Intent ik = new Intent(LoginActivity.this, PatientRecord.class);
                            ik.putExtra("token", token);
                            ik.putExtra("id", id);
                            startActivity(ik);
                            break;
                        case 1:Intent it = new Intent(LoginActivity.this, Doctor.class);
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




            }
        }
    }

    private void fun(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }










}
