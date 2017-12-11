package floatview.coder.allen.com.floatview;

import android.app.Application;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.view.WindowManager;

import floatview.coder.allen.com.floatview.orc.OrcClient;
import floatview.coder.allen.com.floatview.utils.DeviceInfor;


/**
 * Created by husongzhen on 17/12/8.
 */

public class GlobalContext extends Application {


    private int result;
    private Intent intent;
    private MediaProjectionManager mMediaProjectionManager;


    public interface OnScreenshotListener {
        void shot(String bitmap);
    }


    private static GlobalContext context;

    public static GlobalContext getContext() {
        return context;
    }


    private OnScreenshotListener onScreenshotListener;

    public GlobalContext setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        this.onScreenshotListener = onScreenshotListener;
        return this;
    }

    public OnScreenshotListener getOnScreenshotListener() {
        return onScreenshotListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OrcClient.newInstance().init(this);
        DeviceInfor.init(this);
    }

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();


    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }


    public int getResult() {
        return result;
    }

    public GlobalContext setResult(int result) {
        this.result = result;
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    public GlobalContext setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public MediaProjectionManager getmMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public GlobalContext setmMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
        return this;
    }
}
