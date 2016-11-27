package hackathon.digitalocean.youdecide.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hackathon.digitalocean.youdecide.R;

public class SurveyResultFragment extends Fragment {

    Context mContext;

    public SurveyResultFragment () {
        /*required public constructor*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        String question = getArguments().getString("question");
        String answer = getArguments().getString("answer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_result, container, false);
    }
}
