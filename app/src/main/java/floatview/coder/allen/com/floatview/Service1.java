package floatview.coder.allen.com.floatview;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import floatview.coder.allen.com.floatview.view.FloatViewHandler;

/**
 * Created by husongzhen on 17/12/9.
 */
public class Service1 extends Service {
    private FloatViewHandler handler;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Application application = getApplication();
        initFloatView(application);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initFloatView(Application application) {
        handler = GlobalContext.getContext().getHandler();
        handler.onCreateFloatView(application);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeFloatView();
    }
}