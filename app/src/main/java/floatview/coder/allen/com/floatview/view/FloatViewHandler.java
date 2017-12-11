package floatview.coder.allen.com.floatview.view;

import android.app.Application;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import floatview.coder.allen.com.floatview.R;
import floatview.coder.allen.com.floatview.shot.ShotScreen;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by husongzhen on 17/12/11.
 */

public class FloatViewHandler {


    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private LinearLayout mFloatLayout;
    private ShotScreen shotScreen;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreateFloatView(Application application) {
        shotScreen = new ShotScreen();
        shotScreen.preShot();
        addFloatView(application);

    }

    private void addFloatView(Application application) {
        wmParams = setWmParams();
        mWindowManager = (WindowManager) application.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(application);
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.layout_floatview, null);
        final ImageView mFloatView = mFloatLayout.findViewById(R.id.image_logo);
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
                mFloatLayout.setVisibility(View.GONE);
                shotScreen.startShotScreen();
                mFloatLayout.setVisibility(View.VISIBLE);
            }
        });
        mWindowManager.addView(mFloatLayout, wmParams);
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


    public void removeFloatView() {
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        shotScreen.finishShot();
    }
}

