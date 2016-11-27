package hackathon.digitalocean.youdecide.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.activity.GetQuestions;

public class QuestionFragment extends Fragment implements RadioButton.OnCheckedChangeListener {

    Context mContext;

    String questionString = "";
    String answerString = "";
    int position;

    String[] answers;

    public QuestionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        position = getArguments().getInt("position");
        questionString = "Question " + (position + 1) + ": " + getArguments().getString("statement");
        answerString = getArguments().getString("options");

        answers = answerString.split("\\|");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_question, null);

        TextView tvQuestion = (TextView) fragmentView.findViewById(R.id.tvQuestions);
        if (!questionString.equals("")) {
            tvQuestion.setText(questionString);
        }

        RadioGroup mRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.answerRadioGroup);
        for (int i = 0; i < answers.length; i++) {
            RadioButton mRadioButton = new RadioButton(mContext);
            mRadioButton.setText(answers[i]);
            mRadioButton.setTag(i);
            mRadioButton.setTextSize(16);
            mRadioButton.setOnCheckedChangeListener(this);
            mRadioGroup.addView(mRadioButton);
        }

        return fragmentView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
        if (status) {
            ((GetQuestions) getActivity()).saveAnswer(position, (int) compoundButton.getTag() + 1);
            Log.d("Answer", position + " " + ((int) compoundButton.getTag()));
        }
    }
}
