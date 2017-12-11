package floatview.coder.allen.com.floatview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by husongzhen on 17/12/11.
 */

public class FloatView extends LinearLayout implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    public interface OnFloatAction {
        void onClickListener();

        void onDragListener(MotionEvent e2);
    }


    private OnFloatAction floatAction;

    public FloatView setFloatAction(OnFloatAction floatAction) {
        this.floatAction = floatAction;
        return this;
    }

    private GestureDetector mGestureDetector;

    public FloatView(Context context) {
        super(context);
        initSet();
    }


    public FloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSet();
    }

    public FloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSet();
    }


    private void initSet() {
        mGestureDetector = new GestureDetector(getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = mGestureDetector.onTouchEvent(event);
        return b;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {


        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        floatAction.onDragListener(e2);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        floatAction.onClickListener();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
