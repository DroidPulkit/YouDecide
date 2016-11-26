package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hackathon.digitalocean.youdecide.R;

public class MainActivity extends AppCompatActivity {

    Button btnAnswerQuestion;
    Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAnswerQuestion = (Button) findViewById(R.id.btnAnswerQuestion);
        btnAnswerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent answerQuestionIntent = new Intent(mContext, GetQuestionsTwo.class);
                startActivity(answerQuestionIntent);
            }
        });
    }
}
