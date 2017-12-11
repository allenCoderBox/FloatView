package floatview.coder.allen.com.floatview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import floatview.coder.allen.com.floatview.orc.OrcClient;
import rx.Observer;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = getIntent().getStringExtra("file");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView iamge = findViewById(R.id.image);
        iamge.setImageBitmap(bitmap);
        genericFile(path);
    }

    private void genericFile(String path) {
        final TextView textView = findViewById(R.id.text);
        OrcClient.newInstance().generalFile(path)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        textView.setText(s);
                    }
                });

    }


}
