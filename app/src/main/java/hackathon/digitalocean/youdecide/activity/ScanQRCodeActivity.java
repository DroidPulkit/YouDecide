package hackathon.digitalocean.youdecide.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

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
            System.out.println(text);
            String[] tempArray = text.split("/");

            String name = tempArray[4];
            String surveyNumber = tempArray[5].substring(6);
            String server = tempArray[0] + "/" + tempArray[1] + "/" + tempArray[2] + "/";

            myDecoderView.getCameraManager().stopPreview();

            startSurveyActivity.putExtra("URL", text);
            startSurveyActivity.putExtra("userName", name);
            startSurveyActivity.putExtra("surveyNumber", surveyNumber);
            startSurveyActivity.putExtra("server", server);
            startActivity(startSurveyActivity);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                myDecoderView.getCameraManager().startPreview();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 200);
            }
        } else {
            myDecoderView.getCameraManager().startPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myDecoderView.getCameraManager().startPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDecoderView.getCameraManager().stopPreview();
    }
}
