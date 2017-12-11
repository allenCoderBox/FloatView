package floatview.coder.allen.com.floatview;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import floatview.coder.allen.com.floatview.shot.ShotManager;
import floatview.coder.allen.com.floatview.view.FloatViewHandler;

/**
 * Created by husongzhen on 17/12/9.
 */
public class Service1 extends Service {
    private FloatViewHandler handler;

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
        handler.onCreateFloatView(getApplication());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeFloatView();
    }
}