package hackathon.digitalocean.youdecide.Models;

import java.util.List;

/**
 * Created by bsntkmr18 on 26-11-2016.
 */

public class Question {
    String Question;
    List<String> options;


    public Question(String question, List<String> options) {
        Question = question;
        this.options = options;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}