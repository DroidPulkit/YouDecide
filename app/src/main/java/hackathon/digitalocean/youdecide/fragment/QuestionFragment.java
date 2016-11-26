package hackathon.digitalocean.youdecide.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hackathon.digitalocean.youdecide.R;

public class QuestionFragment extends Fragment {

    Context mContext;

    String questionString = "";
    String answerString = "";

    String[] answers;

    public QuestionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        questionString = getArguments().getString("statement");
        answerString = getArguments().getString("options");

        answers = answerString.split("\\|");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_question, null);

        TextView tvQuestion = (TextView) fragmentView.findViewById(R.id.tvQuestions);
        if (!questionString.equals("")) {
            tvQuestion.setText(questionString + "\n" + answerString);
        }

        RadioGroup mRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.answerRadioGroup);
        for (int i = 0; i < answers.length; i++) {
            RadioButton mRadioButton = new RadioButton(mContext);
            mRadioButton.setText(answers[i]);
            mRadioButton.setTag(i);
            mRadioGroup.addView(mRadioButton);
        }

        return fragmentView;
    }

}
