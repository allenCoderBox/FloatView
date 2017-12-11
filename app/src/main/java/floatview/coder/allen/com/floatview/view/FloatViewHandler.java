package floatview.coder.allen.com.floatview.view;

import android.app.Application;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import floatview.coder.allen.com.floatview.R;
import floatview.coder.allen.com.floatview.shot.ShotScreen;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by husongzhen on 17/12/11.
 */

public class FloatViewHandler implements UIFloatViewHandler {


    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private FloatView mFloatLayout;
    private ShotScreen shotScreen;


    public void onCreateFloatView(Application application) {
        shotScreen = new ShotScreen();
        addFloatView(application);
    }


    private void addFloatView(Application application) {
        wmParams = setWmParams();
        mWindowManager = (WindowManager) application.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(application);
        mFloatLayout = (FloatView) inflater.inflate(R.layout.layout_floatview, null);
        mFloatLayout.setFloatAction(new FloatView.OnFloatAction() {
            @Override
            public void onClickListener() {
                mFloatLayout.setVisibility(View.GONE);
                shotScreen.startShotScreen();
                mFloatLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDragListener(MotionEvent event) {
                resetFloatViewPos(event);
            }
        });
        mWindowManager.addView(mFloatLayout, wmParams);
    }

    private void resetFloatViewPos(MotionEvent event) {
        wmParams.x = (int) event.getRawX() - mFloatLayout.getMeasuredWidth() / 2;
        wmParams.y = (int) event.getRawY() - mFloatLayout.getMeasuredHeight() / 2 - 25;
        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
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
    public void removeFloatView() {
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        shotScreen.finishShot();
    }
}

