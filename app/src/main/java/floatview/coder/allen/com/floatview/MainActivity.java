package floatview.coder.allen.com.floatview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import floatview.coder.allen.com.floatview.shot.ShotManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startIntent();
        findViewById(R.id.button).setOnClickListener(v -> startActivity(new Intent(this, AIUITestActivity.class)));

    }

    private void startIntent() {
        ShotManager.news().requestPermiss(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShotManager.news().bindRsult(this, requestCode, resultCode, data);
    }

}
