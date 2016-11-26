package hackathon.digitalocean.youdecide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.pojo.Question;

public class RecyclerQuestionAdapter extends RecyclerView.Adapter<RecyclerQuestionAdapter.QuestionHolder> {

    private List<Question> questionList;
    private Context mContext;

    public RecyclerQuestionAdapter (Context context, List<Question> list) {
        mContext = context;
        questionList = list;
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_row_question, null));
    }

    @Override
    public void onBindViewHolder(QuestionHolder holder, int position) {
        holder.tvQuestion.setText(questionList.get(position).getQuestion());
        String[] answers = questionList.get(position).getAnswers().split("\\|");
        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(answer);
            radioButton.setTag(answer);
            holder.rdOptions.addView(radioButton);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        RadioGroup rdOptions;

        QuestionHolder(View itemView) {
            super(itemView);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            rdOptions = (RadioGroup) itemView.findViewById(R.id.radioGroupOptions);
        }
    }
}
