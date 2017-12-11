package floatview.coder.allen.com.floatview.orc;

import android.app.Application;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;

import java.io.File;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by husongzhen on 17/12/11.
 */

public class OrcClient {

    private static final OrcClient client = new OrcClient();

    public OrcClient() {
    }


    public static final OrcClient newInstance() {
        return client;
    }


    public void init(Application application) {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {

            }

            @Override
            public void onError(OCRError ocrError) {

            }
        }, application);
    }


    public Observable<String> generalFile(final String filePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                sendGeneral(subscriber, filePath);
            }
        });
    }

    private void sendGeneral(final Subscriber<? super String> subscriber, String filePath) {
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));
// 调用通用文字识别服务
        OCR.getInstance().recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                // 调用成功，返回GeneralResult对象
                StringBuffer sb = new StringBuffer();

                for (WordSimple wordSimple : result.getWordList()) {
                    // wordSimple不包含位置信息
                    WordSimple word = wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                // json格式返回字符串
                subscriber.onNext(sb.toString());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                subscriber.onError(error);
            }
        });
    }

}
