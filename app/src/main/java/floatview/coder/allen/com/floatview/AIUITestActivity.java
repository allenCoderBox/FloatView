package floatview.coder.allen.com.floatview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by husongzhen on 17/12/14.
 */

public class AIUITestActivity extends AppCompatActivity {


    private AIUIAgent mAIUIAgent;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aiui_layout);
        requestPermissions();
        init();
        editText = findViewById(R.id.edit_query);
        editText.setText("今天天气如何");
        findViewById(R.id.submit).setOnClickListener(v -> {
            String text = editText.getText().toString();
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
            AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, "data_type=text", text.getBytes());
            mAIUIAgent.sendMessage(msg);
        });


    }

    private void init() {
        //创建AIUIAgent
        mAIUIAgent = AIUIAgent.createAgent(this, getAIUIParams(), event -> {
            eventResult(event);
        });

//发送`CMD_START`消息，使AIUI处于工作状态
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(startMsg);
    }

    private void eventResult(AIUIEvent event) {
        switch (event.eventType) {
            //唤醒事件
            case AIUIConstant.EVENT_WAKEUP: {
                break;
            }
            //结果事件（包含听写，语义，离线语法结果）
            case AIUIConstant.EVENT_RESULT: {


                StringBuffer mNlpText = new StringBuffer();

                try {
                    JSONObject bizParamJson = new JSONObject(event.info);
                    JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                    JSONObject params = data.getJSONObject("params");
                    JSONObject content = data.getJSONArray("content").getJSONObject(0);

                    if (content.has("cnt_id")) {
                        String cnt_id = content.getString("cnt_id");
                        JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));
                        mNlpText.append("\n");
                        mNlpText.append(cntJson.toString());
                        String sub = params.optString("sub");
                        if ("nlp".equals(sub)) {
                            // 解析得到语义结果
                            String resultStr = cntJson.optString("intent");
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    mNlpText.append("\n");
                    mNlpText.append(e.getLocalizedMessage());
                }
                mNlpText.append("\n");
                TextView textView = findViewById(R.id.result);
                textView.setText(mNlpText.toString());
                Log.e("nlpResult", mNlpText.toString());

                break;
            }
            //休眠事件
            case AIUIConstant.EVENT_SLEEP: {
                break;
            }
            //错误事件
            case AIUIConstant.EVENT_ERROR: {
                break;
            }
        }
    }

    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }


    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
