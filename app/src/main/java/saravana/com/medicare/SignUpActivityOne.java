package saravana.com.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);
        Button doc=(Button) findViewById(R.id.button4);
        Button pat=(Button) findViewById(R.id.button);


        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(SignUpActivityOne.this,SignUpActivityDr.class);
                startActivity(n);
            }
        });
        pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(SignUpActivityOne.this,SignUpActivityPatient.class);
                startActivity(n);
            }
        });




    }
}
