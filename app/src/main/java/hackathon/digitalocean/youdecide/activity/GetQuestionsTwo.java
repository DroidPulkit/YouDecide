package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import developer.shivam.perfecto.ConvertInputStream;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.adapter.RecyclerQuestionAdapter;
import hackathon.digitalocean.youdecide.pojo.Question;

public class GetQuestionsTwo extends AppCompatActivity {

    Context mContext = GetQuestionsTwo.this;

    List<Question> questionList;

    RecyclerView rvQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_get_questions_two);

        rvQuestionList = (RecyclerView) findViewById(R.id.rvQuestionList);

        if (!getQuestionsString().equals("")) {
            questionList = parseQuestionsJson(getQuestionsString());
            rvQuestionList.setLayoutManager(new LinearLayoutManager(mContext));
            rvQuestionList.setAdapter(new RecyclerQuestionAdapter(mContext, questionList));
        }
    }

    public String getQuestionsString() {
        InputStream mInputStream = null;
        try {
            mInputStream = getAssets().open("sample.json");
            return ConvertInputStream.toString(mInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Question> parseQuestionsJson(String jsonString) {
        List<Question> mList = new ArrayList<>();
        try {
            JSONObject parentObject = new JSONObject(jsonString);
            JSONArray questionsArray = parentObject.getJSONArray("questions");
            for (int i = 0; i < questionsArray.length(); i++) {
                Question question = new Question();
                question.setQuestion(questionsArray.getJSONObject(i).getString("statement"));
                question.setAnswers(questionsArray.getJSONObject(i).getString("options"));
                mList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }
}
