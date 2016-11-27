package hackathon.digitalocean.youdecide.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.IoUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import developer.shivam.perfecto.OnNetworkRequest;
import developer.shivam.perfecto.Perfecto;
import hackathon.digitalocean.youdecide.Models.Question;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.StaticData;

import static android.R.attr.id;

public class ConductSurvey extends AppCompatActivity {

    LinearLayout optionsRoot;
    List<TextInputLayout> optionsLayout;
    List<Question> questions;
    List<String> options;
    Toolbar toolbar;
    EditText question;
    boolean finish;
    Button newQuestion;
    String url;
    private boolean permi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conduct_survey);
        optionsRoot = (LinearLayout) findViewById(R.id.options_root);
        optionsLayout = new ArrayList<>();
        questions = new ArrayList<>();
        addOption();
        addOption();
        question = (EditText) findViewById(R.id.question);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Question " + (questions.size() + 1));

        findViewById(R.id.add_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOption();
            }
        });

        newQuestion = (Button) findViewById(R.id.new_question);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (question.getText().toString().length() < 1)
                    ((TextInputLayout) findViewById(R.id.question_layout)).setError("Invalid question");
                else {
                    int i = 0;
                    options = new ArrayList<String>();
                    for (i = 0; i < optionsLayout.size(); i++) {
                        if (optionsLayout.get(i).getEditText().getText().length() < 1) {
                            optionsLayout.get(i).setError("Invalid Option");
                            break;
                        }
                        options.add(optionsLayout.get(i).getEditText().getText().toString());
                    }
                    if (i == optionsLayout.size()) {
                        finish = true;
                        questions.add(new Question(question.getText().toString(), options));
                        getSupportActionBar().setTitle("Question " + (questions.size() + 1));
                        setUpNewQuestion();
                    }
                }
            }
        });
    }

    public void onFinish(View v) {
        try {
            finish = false;
            newQuestion.performClick();
                if (questions.size() > 1) {
                    JSONObject data = new JSONObject();
                    JSONObject object = new JSONObject();
                    JSONArray question = new JSONArray();
                    for (int i = 0; i < questions.size(); i++) {
                        JSONObject ques = new JSONObject();
                        ques.put("statement", questions.get(i).getQuestion());
                        String option = questions.get(i).getOptions().get(0);
                        for (int j = 1; j < questions.get(i).getOptions().size(); j++)
                            option = option.concat("|").concat(questions.get(i).getOptions().get(j));
                        ques.put("options", option);
                        question.put(ques);
                    }
                    object.put("questions", question);
                    data.put("data", object);
                    data.put("userName", getSharedPreferences(StaticData.USER_INFO, MODE_PRIVATE).getString("userName", ""));
                    data.put("serverPath", StaticData.SERVER_PATH);
                    Log.d("QUESTION", data.toString());
                    Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show();
                    Perfecto.with(this).fromUrl(StaticData.POST_QUESTION).ofTypePost(data).connect(new OnNetworkRequest() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(String s) {
                            Log.d("Response", s);
                            try {
                                JSONObject object = new JSONObject(s);
                                if (object.getString("status").equals("1")) {
                                    Toast.makeText(ConductSurvey.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                    showImage(object.getString("QRcode"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, String s, String s1) {
                            Log.d("Fail", i + "  \n" + s + "  \n " + s1);
                        }
                    });
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveQRCodeToGallery(String qRcode) {

    }

    public void addOption() {
        if (optionsLayout.size() < 5) {
            TextInputLayout option = new TextInputLayout(ConductSurvey.this);
            EditText optionText = new EditText(ConductSurvey.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 16, 0, 0);
            option.setLayoutParams(layoutParams);
            optionText.setLayoutParams(layoutParams);
            option.addView(optionText);
            optionsLayout.add(option);
            option.setHint("Option " + optionsLayout.size());
            optionsRoot.addView(option);
        } else
            Toast.makeText(this, "Maximum Options 5", Toast.LENGTH_SHORT).show();
    }

    private void setUpNewQuestion() {
        question.setText("");
        optionsRoot.removeAllViews();
        options = new ArrayList<>();
        optionsLayout = new ArrayList<>();
        addOption();
        addOption();
    }

    private void showImage(String url) {
        if (ActivityCompat.checkSelfPermission(ConductSurvey.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            System.out.println("Request not granted");
            this.url = url;
            permi = false;
        } else {
            permi = true;
            System.out.println("Request granted");
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Success Gold");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("directory not created");
                }
            }

            final File outputFile = saveImage(url);
            AlertDialog.Builder builder = new AlertDialog.Builder(ConductSurvey.this);
            View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
            builder.setView(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.qr_code_image);
            ImageLoader.getInstance().displayImage(url,imageView);
            final Dialog dialog = builder.create();
            Button share = (Button) view.findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "- via Success Gold");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
                    intent.setType("*/*");
                    startActivity(Intent.createChooser(intent, "Share Image"));
                }
            });
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public File saveImage(String url) {
        if (ActivityCompat.checkSelfPermission(ConductSurvey.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            System.out.println("Request not granted");
            this.url = url;
            permi = false;
        } else {
            permi = true;
            System.out.println("Request granted");
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Success Gold");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("directory not created");
                }
            }
            InputStream sourceStream = null;
            File cachedImage = ImageLoader.getInstance().getDiscCache().get(url);
            if (cachedImage != null && cachedImage.exists()) { // if image was cached by UIL
                try {
                    sourceStream = new FileInputStream(cachedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (sourceStream != null) {
                File outputFile = new File(directory + File.separator + "Success" + id + ".jpg");
                try {
                    OutputStream targetStream = new FileOutputStream(outputFile);
                    try {
                        IoUtils.copyStream(sourceStream, targetStream, null);
                    } finally {
                        targetStream.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        sourceStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
                return outputFile;
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ConductSurvey.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            (ConductSurvey.this).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2002);

        } else {
            ActivityCompat.requestPermissions(ConductSurvey.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2002);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2002 && url != null) {
            saveImage(url);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
