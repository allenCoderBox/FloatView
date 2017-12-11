package floatview.coder.allen.com.floatview;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import floatview.coder.allen.com.floatview.shot.ShotManager;
import floatview.coder.allen.com.floatview.utils.CheckUtils;
import floatview.coder.allen.com.floatview.view.UIFloatViewHandler;

/**
 * Created by husongzhen on 17/12/9.
 */
public class FloatViewService extends Service {
    private UIFloatViewHandler handler;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initFloatView();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initFloatView() {
        handler = ShotManager.news().getFloatViewHandler();
        if (!CheckUtils.isNull(handler)) {
            handler.onCreateFloatView(getApplication());
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!CheckUtils.isNull(handler)) {
            handler.removeFloatView();
        }

    }
}