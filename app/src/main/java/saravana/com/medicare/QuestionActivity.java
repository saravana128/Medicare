package saravana.com.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    public static ArrayList<User3> audi2,audi3;
    UsersAdapter3 adapter;
    public int count=0;
    public static int critical=0;
    TextView t1;
    Button yes,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        String s=getIntent().getExtras().getString("val");
        t1=(TextView) findViewById(R.id.ques) ;
        yes=(Button) findViewById(R.id.yes);
        no=(Button) findViewById(R.id.no);


        JSONArray a1 = null;
        try {
            JSONObject j1= new JSONObject(s);
            a1 = j1.getJSONArray("Disease");
            JSONArray a2 = j1.getJSONArray("Critical Disease");

            audi2= User3.fromJson(a1);
            audi3=User3.fromJson(a2);

            adapter=new UsersAdapter3(QuestionActivity.this,audi3);

            if(adapter.getCount()==0)
            {
                Intent na=new Intent(QuestionActivity.this,DiagoniseResult.class);
                startActivity(na);
            }
            else
            {
                User3 u=adapter.getItem(0);
                t1.setText("Do You suffer from "+u.syname+" more than "+ u.min + " days?");
                count=count+1;
            }

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    critical=critical+1;
                    if(count<adapter.getCount())
                    {
                        User3 u=adapter.getItem(count);
                        t1.setText("Do You suffer from "+u.syname+" more than "+ u.min + " days?");
                        count=count+1;

                    }
                    else
                    {
                        Intent no=new Intent(QuestionActivity.this,DiagoniseResult.class);
                        startActivity(no);
                    }





                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count<adapter.getCount())
                    {
                        User3 u=adapter.getItem(count);
                        t1.setText("Do you suffer from "+u.syname+" more than "+ u.min + "days?");
                        count=count+1;
                    }
                    else
                    {
                        Intent no=new Intent(QuestionActivity.this,DiagoniseResult.class);
                        startActivity(no);
                    }





                }
            });








        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
