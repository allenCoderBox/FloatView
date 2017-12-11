package floatview.coder.allen.com.floatview.shot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;

import floatview.coder.allen.com.floatview.FloatViewService;
import floatview.coder.allen.com.floatview.view.UIFloatViewHandler;

/**
 * Created by husongzhen on 17/12/11.
 */

public class ShotManager {
    private static int REQUEST_MEDIA_PROJECTION = 1;

    private static class Clazz {
        private static final ShotManager shotManger = new ShotManager();
    }

    private ShotManager() {
    }

    public static final ShotManager news() {
        return Clazz.shotManger;
    }


    private int resultCode = 0;

    private Intent intent = null;

    private MediaProjectionManager mMediaProjectionManager;
    private UIFloatViewHandler floatViewHandler;
    private OnCutResutHandler resutHandler;


    public ShotManager requestPermiss(Activity activity) {
        mMediaProjectionManager = (MediaProjectionManager) activity.getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (intent != null && resultCode != 0) {
            ShotManager.news().setIntent(intent).setResultCode(resultCode);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
                setmMediaProjectionManager(mMediaProjectionManager);
            }
        }

        return this;
    }


    public void bindRsult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            } else if (data != null && resultCode != 0) {
                this.resultCode = resultCode;
                this.intent = data;
                Intent intent = new Intent(activity.getApplicationContext(), FloatViewService.class);
                activity.startService(intent);
            }
        }
    }


    /**
     * @ set/get
     */
    public UIFloatViewHandler getFloatViewHandler() {
        return floatViewHandler;
    }

    public ShotManager setFloatViewHandler(UIFloatViewHandler floatViewHandler) {
        this.floatViewHandler = floatViewHandler;
        return this;
    }

    public int getResultCode() {
        return resultCode;
    }

    public ShotManager setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    public ShotManager setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public MediaProjectionManager getmMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public ShotManager setmMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
        return this;
    }


    public OnCutResutHandler getResutHandler() {
        return resutHandler;
    }

    public ShotManager setResutHandler(OnCutResutHandler resutHandler) {
        this.resutHandler = resutHandler;
        return this;
    }
}
