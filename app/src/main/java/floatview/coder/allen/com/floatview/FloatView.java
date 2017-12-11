package floatview.coder.allen.com.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cj.ScreenShotUtil.ScreentShotUtil;

/**
 * Created by husongzhen on 17/12/8.
 */

public class FloatView extends RelativeLayout implements View.OnClickListener {


    private float mTouchStartX;
    private float mTouchStartY;
    private int x;
    private int y;

    private View layout;
    private ImageView imageLogo;
    @SuppressLint("WrongConstant")
    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService("window");
    private WindowManager.LayoutParams wmParams = ((GlobalContext) getContext().getApplicationContext()).getMywmParams();

    private Context context;

    private Boolean showFlag = false;

    private Boolean updateFlag = false;

    int i = 0;

    public FloatView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_floatview, null);
        imageLogo = (ImageView) layout.findViewById(R.id.image_logo);


        imageLogo.setOnClickListener(this);
        addView(layout);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mTouchStartX = event.getRawX();
                mTouchStartY = event.getRawY();
                x = wmParams.x;
                y = wmParams.y;
                updateFlag = true;

                break;
            case MotionEvent.ACTION_MOVE:
                int xDistance = (int) (event.getRawX() - mTouchStartX);
                wmParams.x = x + xDistance;
                int yDistance = (int) (event.getRawY() - mTouchStartY);
                wmParams.y = y + yDistance;
                updateViewPosition();

                break;

            case MotionEvent.ACTION_UP:
                int xUpDistance = (int) (event.getRawX() - mTouchStartX);
                wmParams.x = x + xUpDistance;
                int yUpDistance = (int) (event.getRawY() - mTouchStartY);
                wmParams.y = y + yUpDistance;

                updateViewPosition();
                updateFlag = false;
                break;
        }
        return true;
    }

    public void removeView() {
        if (showFlag) {
            wm.removeView(this);
            showFlag = false;
        }

    }


    public void updateViewPosition() {
        wm.updateViewLayout(this, wmParams);
        showFlag = true;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//        context.startActivity(new Intent(context, ScreenShotActivity.class));


        String imgurl = context.getExternalCacheDir().getAbsolutePath();
        String fullPath = imgurl + "/" + SystemClock.currentThreadTimeMillis() + ".png";
        ScreentShotUtil.getInstance().takeScreenshot(context, fullPath);


        GlobalContext.getContext().getOnScreenshotListener().shot(fullPath);
    }
}
