package hackathon.digitalocean.youdecide.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;

import java.util.ArrayList;
import java.util.List;

import hackathon.digitalocean.youdecide.R;

public class SurveyResultFragment extends Fragment {

    Context mContext;
    String question = "";
    String answerString = "";
    String answer[];
    String answers[];
    private int totalUser = 0;

    public SurveyResultFragment () {
        /*required public constructor*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        question = getArguments().getString("question");
        answerString = getArguments().getString("answer").replaceAll("\\|", "\n");

        answer = getArguments().getString("answer").split("\\|");
        System.out.println(answer.length);
        answers = getArguments().getString("user_answer").split("-");
        System.out.println(answers.length);
        System.out.println(getArguments().getString("user_answer"));
        for (int i = 0; i < answers.length; i++) {
            totalUser = totalUser + Integer.parseInt(answers[i]);
        }
        System.out.println(totalUser);

        for (int i = 0; i < answers.length; i++) {
            System.out.println(Integer.parseInt(answers[i]) * 100 / totalUser);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_result, container, false);
        TextView tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        TextView tvAnswer = (TextView) view.findViewById(R.id.tvAnswer);

        tvQuestion.setText("Question : " + question);
        tvAnswer.setText("Answers :" + "\n" + answerString);

        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
        List<ChartData> data = new ArrayList<>();
        int[] color = new int[5];
        color[0] = Color.BLUE;
        color[1] = Color.GREEN;
        color[2] = Color.GRAY;
        color[3] = Color.YELLOW;
        color[4] = Color.RED;

        for (int i = 0; i < answer.length; i++) {
            data.add(new ChartData(answer[i] + " " + Integer.parseInt(answers[i]) * 100 / totalUser + "%",
                    Integer.parseInt(answers[i]) * 100 / totalUser));
        }
        pieChart.setChartData(data);
        return view;
    }
}
