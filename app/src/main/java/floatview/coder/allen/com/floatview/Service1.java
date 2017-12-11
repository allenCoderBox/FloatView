package floatview.coder.allen.com.floatview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;

import floatview.coder.allen.com.floatview.shot.ShotScreen;

/**
 * Created by husongzhen on 17/12/9.
 */
public class Service1 extends Service {
    private WindowManager.LayoutParams wmParams = null;
    private WindowManager mWindowManager = null;
    private LayoutInflater inflater = null;
    private ImageButton mFloatView = null;

    private static final String TAG = "MainActivity";

    private SimpleDateFormat dateFormat = null;
    private String strDate = null;
    private String pathImage = null;
    private String nameImage = null;

    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;

    public static int mResultCode = 0;
    public static Intent mResultData = null;
    public static MediaProjectionManager mMediaProjectionManager1 = null;

    private WindowManager mWindowManager1 = null;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private ImageReader mImageReader = null;
    private DisplayMetrics metrics = null;
    private int mScreenDensity = 0;
    private LinearLayout mFloatLayout;
    private ShotScreen shotScreen;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        createFloatView();
        shotScreen = new ShotScreen();
        shotScreen.preShot();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView() {
        mWindowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        wmParams = setWmParams();
        initFloatView();
        mWindowManager.addView(mFloatLayout, wmParams);
    }

    private void initFloatView() {
        inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.layout_floatview, null);
        mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.image_logo);
        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // hide the button
                mFloatView.setVisibility(View.INVISIBLE);

                shotScreen.startShotScreen();

                mFloatView.setVisibility(View.VISIBLE);
            }

        });
    }


    private WindowManager.LayoutParams setWmParams() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }


    @Override
    public void onDestroy() {
        // to remove mFloatLayout from windowManager
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        shotScreen.finishShot();
    }
}