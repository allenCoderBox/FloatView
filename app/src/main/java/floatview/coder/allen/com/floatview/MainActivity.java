package floatview.coder.allen.com.floatview;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import floatview.coder.allen.com.floatview.shot.ShotManager;

public class MainActivity extends AppCompatActivity implements GlobalContext.OnScreenshotListener {
    private String TAG = "Service";
    private int result = 0;
    private Intent intent = null;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startIntent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startIntent() {
        ShotManager.news().setIntent(intent).setResultCode(result).requestPermiss(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShotManager.news().bindRsult(this, requestCode, resultCode, data);
    }

    @Override
    public void shot(String path) {
        ImageView imageView = findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
    }
}
