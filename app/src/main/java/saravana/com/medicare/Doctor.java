package saravana.com.medicare;

/**
 * Created by saravana on 14/4/17.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Doctor extends AppCompatActivity {
    public String id, token;
    public static String rend;
    Base64 d;

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
    }
}
