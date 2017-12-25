package floatview.coder.allen.com.floatview;

import android.app.Application;
import android.content.Intent;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import floatview.coder.allen.com.floatview.orc.OrcClient;
import floatview.coder.allen.com.floatview.shot.ShotManager;
import floatview.coder.allen.com.floatview.utils.DeviceInfor;
import floatview.coder.allen.com.floatview.view.FloatViewHandler;


/**
 * Created by husongzhen on 17/12/8.
 */

public class GlobalContext extends Application {


    private static GlobalContext context;

    public static GlobalContext getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OrcClient.newInstance().init(this);
        DeviceInfor.init(this);
        ShotManager.news().setFloatViewHandler(new FloatViewHandler())
                .setResutHandler(filePath -> OpenFile(filePath));
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=5a2b4206");
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }


    private void OpenFile(String filePath) {
        Intent intent = new Intent(GlobalContext.getContext(), ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("file", filePath);
        GlobalContext.getContext().startActivity(intent);
    }

}
