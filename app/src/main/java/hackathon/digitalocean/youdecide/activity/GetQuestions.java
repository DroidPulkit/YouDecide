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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.intrusoft.indicator.Flare;

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

public class GetQuestions extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = GetQuestions.this;

    private ViewPager questionViewPager;

    Flare flare;

    private List<Question> questionList = new Vector<>();

    private Button btnPrevious, btnNext;

    Toolbar mToolbar;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_questions);

        url = getIntent().getStringExtra("url");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Servey");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        flare = (Flare) findViewById(R.id.indicator);

        questionViewPager = (ViewPager) findViewById(R.id.questionViewPager);

        flare.setUpWithViewPager(questionViewPager);

        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPrevious) {
            decrementViewPagerPosition();
        } else if (view.getId() == R.id.btnNext) {
            incrementViewPagerPosition();
        }
    }

    public class QuestionsFragmentAdapter extends FragmentPagerAdapter {

        QuestionsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString("position", "Question " + ( position + 1) + " : ");
            mBundle.putString("statement", questionList.get(position).getQuestion());
            mBundle.putString("options", questionList.get(position).getAnswers());
            questionFragment.setArguments(mBundle);
            return questionFragment;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    public void decrementViewPagerPosition() {
        if (questionViewPager.getCurrentItem() != 0) {
            questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() - 1);
        }
    }

    public void incrementViewPagerPosition() {
        if (questionViewPager.getCurrentItem() != questionList.size() - 1) {
            questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1);
        }
    }
}
