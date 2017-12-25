package floatview.coder.allen.com.floatview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.List;

import floatview.coder.allen.com.floatview.orc.OrcClient;
import floatview.coder.allen.com.floatview.utils.Sender;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResultActivity extends AppCompatActivity {


    private String nplUrl = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_35910535/nlp/api/WordPos/general";
    private String key = "LTAIEbLD8lmkkkPN";
    private String secret = "pBxXl1A66OkcmHyoIAP4KqbHQRsj6X";
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setVisibility(View.GONE);
        String path = getIntent().getStringExtra("file");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView iamge = findViewById(R.id.image);
        iamge.setImageBitmap(bitmap);
        genericFile(path);
    }

    private void genericFile(String path) {
        final TextView textView = findViewById(R.id.text);
        resultText = findViewById(R.id.result);
        OrcClient.newInstance()
                .generalFile(path)
                .subscribe(new Observer<List<? extends String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(final List<? extends String> results) {
                        textView.setText(results.toString());
                        npl(results);
                    }
                });

    }

    private void npl(List<? extends String> result) {
        Iterator<String> iterator = (Iterator<String>) result.iterator();
        nplAction(iterator);
    }

    private void nplAction(Iterator<String> iterator) {
        if (iterator.hasNext()) {
            String text = iterator.next();
            Logger.e(text);
            nplItem(iterator, text);
        }
    }

    private void nplItem(Iterator<String> iterator, String text) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> subscriber.onNext(nplText(text)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        onResult(s);
                        nplAction(iterator);
                    }
                });
    }

    private String nplText(String s) {
        // String postBody = "{\"text\":\"真丝韩都衣舍连衣裙\"}";
        com.alibaba.fastjson.JSONObject postBodyJson = new com.alibaba.fastjson.JSONObject();
        postBodyJson.put("text", s);
        // Sender代码参考 https://help.aliyun.com/document_detail/shujia/OCR/ocr-api/sender.html
        // String result = Sender.sendPost(serviceURL, postBody, akID, akSecret);
        String result = Sender.sendPost(nplUrl, postBodyJson.toString(), key, secret);
        return result;


    }

    private void onResult(String result) {
        if (result.contains("NT") && result.contains("NR")) {
            resultText.append(result + "\n");
        }
//
//        System.out.println(result);
//        try {
//            com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
//            com.alibaba.fastjson.JSONArray wordObjs = resultJson.getObject("data", com.alibaba.fastjson.JSONArray.class);
//            for (Object wordObj : wordObjs) {
//                com.alibaba.fastjson.JSONObject wordJson = JSON.parseObject(wordObj.toString());
//                String pos = wordJson.getString("pos"); // 词性
//                String word = wordJson.getString("word"); // 词
//                System.out.printf("pos: %s, word: %s\n", pos, word);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
