package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intrusoft.indicator.Flare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import developer.shivam.perfecto.OnNetworkRequest;
import developer.shivam.perfecto.Perfecto;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.fragment.QuestionFragment;
import hackathon.digitalocean.youdecide.pojo.Question;

public class GetQuestions extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = GetQuestions.this;

    private ViewPager questionViewPager;

    Flare flare;

    private List<Question> questionList = new Vector<>();

    Button btnPrevious, btnNext;

    Toolbar mToolbar;

    String URL, userName, surveyNumber;

    int[] selectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_questions);

        URL = getIntent().getStringExtra("URL");
        userName = getIntent().getStringExtra("userName");
        surveyNumber = getIntent().getStringExtra("surveyNumber");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Survey");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        flare = (Flare) findViewById(R.id.indicator);

        questionViewPager = (ViewPager) findViewById(R.id.questionViewPager);

        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        getQuestionsString(URL);
    }

    public void getQuestionsString(String url) {
        Perfecto.with(mContext).fromUrl(url).ofTypeGet().connect(new OnNetworkRequest() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    questionList = parseQuestionsJson(response);
                    if (questionList.size() != 0) {
                        questionViewPager.setAdapter(new QuestionsFragmentAdapter(getSupportFragmentManager()));
                        questionViewPager.setOffscreenPageLimit(questionList.size());
                        selectedAnswers = new int[questionList.size()];
                        for (int i = 0; i < selectedAnswers.length; i++) {
                            selectedAnswers[i] = -1;
                        }
                        flare.setUpWithViewPager(questionViewPager);
                        flare.requestLayout();
                    }
                }
            }

            @Override
            public void onFailure(int responseCode, String s, String s1) {

            }
        });
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
            mBundle.putInt("position", position);
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
        if (questionViewPager.getCurrentItem() == questionList.size() - 2) {
            questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1);
            btnNext.setText("Finish");
        } else if (questionViewPager.getCurrentItem() < questionList.size() - 1) {
            questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1);
            btnNext.setText("Next");
        } else if (questionViewPager.getCurrentItem() == questionList.size() - 1) {
            JSONObject parentObject = new JSONObject();
            try {
                parentObject.put("UserName", userName);
                parentObject.put("SurveyNumber", surveyNumber);
                parentObject.put("Answer", getAnswers());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(parentObject);

            Perfecto.with(mContext).fromUrl("http://139.59.46.237/setAnswers.php")
                    .ofTypePost(parentObject)
                    .connect(new OnNetworkRequest() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(String response) {
                            Log.d("Response", response);
                        }

                        @Override
                        public void onFailure(int i, String s, String s1) {

                        }
                    });

        }
    }

    public void saveAnswer(int position, int answer) {
        selectedAnswers[position] = answer;
    }

    public String getAnswers() {
        String answers = "";
        for (int i = 0; i < selectedAnswers.length; i++) {
            if (selectedAnswers[i] != -1) {
                answers += selectedAnswers[i] + ",";
            } else {
                answers = "Question " + (i + 1) + " not answered";
            }
        }
        return answers;
    }
}
