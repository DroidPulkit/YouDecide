package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import developer.shivam.perfecto.OnNetworkRequest;
import developer.shivam.perfecto.Perfecto;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.pojo.Survey;

public class CheckResultActivity extends AppCompatActivity {

    String CHECK_RESULT_URL = "http://139.59.46.237/listSurvey.php";
    Context mContext = CheckResultActivity.this;
    List<Survey> surveyList = new ArrayList<>();
    ListView lvSurveyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);

        lvSurveyList = (ListView) findViewById(R.id.lvSurveyList);
        lvSurveyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent surveyResult = new Intent(mContext, SurveyResultActivity.class);
                surveyResult.putExtra("survey", surveyList.get(i));
                startActivity(surveyResult);
            }
        });

        getSurvey();
    }

    public void getSurvey() {
        SharedPreferences mSharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mSharedPreferences.getString("userName", "AakashJain"));
            jsonObject.put("serverName", "http://139.59.46.237");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Perfecto.with(mContext).fromUrl(CHECK_RESULT_URL)
                .ofTypePost(jsonObject)
                .connect(new OnNetworkRequest() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(String response) {
                        surveyList = parseJsonOfTypeSurvey(response);
                        lvSurveyList.setAdapter(new SurveyListAdapter());
                    }

                    @Override
                    public void onFailure(int i, String s, String s1) {

                    }
                });
    }

    private List<Survey> parseJsonOfTypeSurvey(String response) {
        List<Survey> surveyList = new ArrayList<>();
        try {
            System.out.println(response);
            JSONArray surveyJSONArray = new JSONArray(response);
            for (int i = 0; i < surveyJSONArray.length(); i++) {
                Survey survey = new Survey();
                survey.setQuestion(surveyJSONArray.getJSONObject(i).getString("questions").replace("\\", ""));
                survey.setAnswers(surveyJSONArray.getJSONObject(i).getString("answers"));
                surveyList.add(survey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return surveyList;
    }

    public class SurveyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return surveyList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(CheckResultActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            TextView tvSurvey = (TextView) view.findViewById(android.R.id.text1);
            tvSurvey.setText("Survey " + (i + 1));
            return view;
        }
    }
}
