package saravana.com.medicare;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by saravana perumal on 04-03-2017.
 */

public class User2 {
    public String id, title;
    public boolean check;

    //done
    public User2(String id, String title, String despcr123,String dept123,String start123,String end123) {
        this.id = id;
        this.title = title;

        this.check=false;
    }

    // Constructor to convert JSON object into a Java class instance
    public User2(JSONObject object) {

        try {
            this.id = object.getString("id");
            this.title= object.getString("symptom_name");
            this.check=false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getTime(String start123, String end123) {
        String from=start123.substring(11,16);
        String to=end123.substring(11,16);

       String dff=from+" to "+to;
        return dff;

    }

    public String getTitle() {
        return title;
    }
    public boolean getcheck() {return check; }

    public String getId() {
        return id;
    }


    public static ArrayList<User2> fromJson(JSONArray jsonObjects) {
        ArrayList<User2> users = new ArrayList<User2>();
        users.clear();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                Log.i("Working","part");
                users.add(new User2(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }



    private String getDate(String word){
        String timeStampStr=word.substring(0,10);

        try{
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(Long.parseLong(timeStampStr)));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return timeStampStr;
        }
    }
}
