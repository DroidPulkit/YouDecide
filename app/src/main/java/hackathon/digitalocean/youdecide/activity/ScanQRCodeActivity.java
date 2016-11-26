package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.Timer;
import java.util.TimerTask;

import hackathon.digitalocean.youdecide.R;

public class ScanQRCodeActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView myDecoderView;

    Context mContext = ScanQRCodeActivity.this;

    private Intent startSurveyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        startSurveyActivity = new Intent(mContext, GetQuestions.class);
        myDecoderView = (QRCodeReaderView) findViewById(R.id.qrCodeView);
        myDecoderView.setOnQRCodeReadListener(this);

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (!text.equals("")) {
            myDecoderView.getCameraManager().stopPreview();

            String[] tempArray=text.split("/");

            String name=tempArray[4];
            String surveyNumber=tempArray[5].substring(6);
            String server=tempArray[0]+"/"+tempArray[1]+"/"+tempArray[2]+"/";

            startSurveyActivity.putExtra("URL",text);
            startSurveyActivity.putExtra("userName",name);
            startSurveyActivity.putExtra("surveyNumber",surveyNumber);
            startSurveyActivity.putExtra("server",server);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(startSurveyActivity);
                }
            }, 1000);
        } else {
            Toast.makeText(mContext, "Scanning failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        myDecoderView.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDecoderView.getCameraManager().stopPreview();
    }
}
