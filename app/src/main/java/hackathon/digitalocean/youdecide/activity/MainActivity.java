package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import hackathon.digitalocean.youdecide.R;

public class MainActivity extends AppCompatActivity {

    Button btnCheckResults;
    Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
    }

    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, ConductSurvey.class));

        btnCheckResults = (Button) findViewById(R.id.check_result);
        btnCheckResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkResult = new Intent(mContext, CheckResultActivity.class);
                startActivity(checkResult);
            }
        });
    }
}
