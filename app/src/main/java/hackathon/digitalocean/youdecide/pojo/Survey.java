package hackathon.digitalocean.youdecide.pojo;

import java.io.Serializable;

public class Survey implements Serializable {

    String question = "";
    String answers = "";

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
