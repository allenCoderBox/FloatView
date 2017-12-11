package floatview.coder.allen.com.floatview;

import android.app.Application;

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
        ShotManager.news().setFloatViewHandler(new FloatViewHandler());
    }


}
