package saravana.com.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DiagoniseResult extends AppCompatActivity {
    UsersAdapter3 adapter1,adapter2;
    TextView cric;
    Button sdoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagonise_result);
        cric=(TextView) findViewById(R.id.criclevel);
        sdoc=(Button) findViewById(R.id.sdoc);


        adapter1=new UsersAdapter3(DiagoniseResult.this,QuestionActivity.audi2);
        adapter2=new UsersAdapter3(DiagoniseResult.this,QuestionActivity.audi3);

        ListView dis=(ListView) findViewById(R.id.disease);
        dis.setAdapter(adapter1);
        switch (QuestionActivity.critical)
        {
            case 0:
                cric.setText("Mild Critical");
                break;
            case 1:
                cric.setText("May be critical");
                break;
            case 3:
            case 2:
            case 4:cric.setText("Highly critical");
                break;
            default: break;

        }
        sdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nk=new Intent(DiagoniseResult.this,MapsActivity.class);
                startActivity(nk);
            }
        });




    }
}
