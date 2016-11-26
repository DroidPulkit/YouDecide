package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import developer.shivam.perfecto.ConvertInputStream;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.fragment.QuestionFragment;
import hackathon.digitalocean.youdecide.pojo.Question;

public class GetQuestions extends AppCompatActivity {

    private Context mContext = GetQuestions.this;
    private ViewPager questionViewPager;
    private TabLayout mTabLayout;
    private List<Question> questionList = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_questions);

        questionViewPager = (ViewPager) findViewById(R.id.questionViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(questionViewPager);

        if (!getQuestionsString().equals("")) {
            questionList = parseQuestionsJson(getQuestionsString());
            if (questionList.size() != 0) {
                questionViewPager.setAdapter(new QuestionsFragmentAdapter(getSupportFragmentManager()));
            }
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

    public class QuestionsFragmentAdapter extends FragmentPagerAdapter {

        QuestionsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString("statement", questionList.get(position).getQuestion());
            mBundle.putString("options", questionList.get(position).getAnswers());
            questionFragment.setArguments(mBundle);
            return questionFragment;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Question " + (position + 1);
        }
    }
}
