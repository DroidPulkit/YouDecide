package hackathon.digitalocean.youdecide.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
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
    Timer timer;
    TimerTask timerTask;
    String textUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        myDecoderView = (QRCodeReaderView) findViewById(R.id.qrCodeView);
        myDecoderView.setOnQRCodeReadListener(this);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent startSurveyActivity = new Intent(mContext, GetQuestions.class);
                startSurveyActivity.putExtra("url", textUrl);
                startActivity(startSurveyActivity);
            }
        };
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (!text.equals("")) {
            myDecoderView.getCameraManager().stopPreview();
            textUrl = text;
            timer.schedule(timerTask, 1000);
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
