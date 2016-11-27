package hackathon.digitalocean.youdecide.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.fragment.SurveyResultFragment;
import hackathon.digitalocean.youdecide.pojo.Question;
import hackathon.digitalocean.youdecide.pojo.Survey;

public class SurveyResultActivity extends AppCompatActivity {

    Survey survey;
    List<Question> questionList = new ArrayList<>();
    ViewPager surveyResultContainer;
    String[] answers;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        surveyResultContainer = (ViewPager) findViewById(R.id.resultsViewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Survey Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        survey = new Survey();
        survey.setQuestion(getIntent().getStringExtra("survey_question"));
        survey.setAnswers(getIntent().getStringExtra("survey_answer"));
        parseSurveyResult();
    }

    public void parseSurveyResult() {
        try {
            String jsonString = survey.getQuestion().replaceAll("%92", "");
            System.out.println(jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray questionArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < questionArray.length(); i++) {
                Question question = new Question();
                question.setQuestion(questionArray.getJSONObject(i).getString("statement"));
                question.setAnswers(questionArray.getJSONObject(i).getString("options"));
                questionList.add(question);
            }

            answers = new String[questionList.size()];
            answers = survey.getAnswers().split(",");
            surveyResultContainer.setAdapter(new SurveyResultAdapter(getSupportFragmentManager()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class SurveyResultAdapter extends FragmentPagerAdapter {

        public SurveyResultAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SurveyResultFragment mFragment = new SurveyResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString("question", questionList.get(position).getQuestion());
            bundle.putString("answer", questionList.get(position).getAnswers());
            bundle.putString("user_answer", answers[position]);
            mFragment.setArguments(bundle);
            return mFragment;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
