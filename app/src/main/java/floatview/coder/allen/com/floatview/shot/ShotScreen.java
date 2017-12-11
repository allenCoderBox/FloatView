package floatview.coder.allen.com.floatview.shot;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.File;

import floatview.coder.allen.com.floatview.GlobalContext;
import floatview.coder.allen.com.floatview.utils.CheckUtils;
import floatview.coder.allen.com.floatview.utils.DeviceInfor;
import floatview.coder.allen.com.floatview.utils.ShotUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by husongzhen on 17/12/11.
 */

public class ShotScreen {


    private MediaProjectionManager mMediaProjectionManager1;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private Intent mResultData;
    private int mResultCode;
    private VirtualDisplay mVirtualDisplay;


    public ShotScreen() {
        createVirtualEnvironment();
    }


    public void startShotScreen() {
        startVirtual();
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String filePath = startCapture();
                        if (!CheckUtils.isNullString(filePath)) {
                            sendMediaFile(filePath);
                            ShotManager.news().getResutHandler().onCutResutHandler(filePath);
                        }
                    }
                }, 500);
    }


    private void createVirtualEnvironment() {
        mMediaProjectionManager1 = (MediaProjectionManager) GlobalContext.getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //ImageFormat.RGB_565
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImageReader = ImageReader.newInstance(DeviceInfor.getSW(), DeviceInfor.getSH(), 0x1, 2);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection() {
        mResultData = ShotManager.news().getIntent();
        mResultCode = ShotManager.news().getResultCode();
        mMediaProjectionManager1 = ShotManager.news().getmMediaProjectionManager();
        mMediaProjection = mMediaProjectionManager1.getMediaProjection(mResultCode, mResultData);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                DeviceInfor.getSW(), DeviceInfor.getSH(), DeviceInfor.getDensityDpi(), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    private String startCapture() {
        Bitmap bitmap = null;
        String filePath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            bitmap = ShotUtils.readImageToBitmap(mImageReader);
            filePath = ShotUtils.saveBitmap(bitmap);
            stopVirtual();
        }

        return filePath;
    }


    private void sendMediaFile(String filePath) {
        File fileImage = new File(filePath);
        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(fileImage);
        media.setData(contentUri);
        GlobalContext.getContext().sendBroadcast(media);
    }


    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVirtualDisplay.release();
        }
        mVirtualDisplay = null;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void finishShot() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "mMediaProjection undefined");
    }


}
