package hackathon.digitalocean.youdecide.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        survey = (Survey) getIntent().getSerializableExtra("survey");
        parseSurveyResult();
    }

    public void parseSurveyResult() {
        try {
            JSONObject jsonObject = new JSONObject(survey.getQuestion());
            JSONArray questionArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < questionArray.length(); i++) {
                Question question = new Question();
                question.setQuestion(questionArray.getJSONObject(i).getString("statement"));
                question.setAnswers(questionArray.getJSONObject(i).getString("options"));
                questionList.add(question);
            }

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
            mFragment.setArguments(bundle);
            return null;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }
}
