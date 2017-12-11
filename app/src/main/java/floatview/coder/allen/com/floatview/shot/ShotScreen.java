package floatview.coder.allen.com.floatview.shot;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import floatview.coder.allen.com.floatview.GlobalContext;
import floatview.coder.allen.com.floatview.ResultActivity;
import floatview.coder.allen.com.floatview.utils.DeviceInfor;

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
    private String filePath;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void preShot() {
        createVirtualEnvironment();
    }


    public void startShotScreen() {
        startVirtual();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startCapture();
            }
        }, 500);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createVirtualEnvironment() {
        filePath = getCutPath();
        mMediaProjectionManager1 = (MediaProjectionManager) GlobalContext.getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //ImageFormat.RGB_565
        mImageReader = ImageReader.newInstance(DeviceInfor.getSW(), DeviceInfor.getSH(), 0x1, 2);
        Log.i(TAG, "prepared the virtual environment");
    }


    private String getCutPath() {
        String pathImage = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
        String nameImage = pathImage + "shot_" + System.currentTimeMillis() + ".png";
        return nameImage;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mMediaProjection != null) {
            Log.i(TAG, "want to display virtual");
            virtualDisplay();
        } else {
            Log.i(TAG, "start screen capture intent");
            Log.i(TAG, "want to build mediaprojection and display virtual");
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection() {
        mResultData = GlobalContext.getContext().getIntent();
        mResultCode = GlobalContext.getContext().getResult();
        mMediaProjectionManager1 = GlobalContext.getContext().getmMediaProjectionManager();
        mMediaProjection = mMediaProjectionManager1.getMediaProjection(mResultCode, mResultData);
        Log.i(TAG, "mMediaProjection defined");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                DeviceInfor.getSW(), DeviceInfor.getSH(), DeviceInfor.getDensityDpi(), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        Log.i(TAG, "virtual displayed");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startCapture() {
        Bitmap bitmap = readImageToBitmap();
        Log.i(TAG, "image data captured");
        if (bitmap != null) {
            try {
                File fileImage = new File(filePath);
                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                    Log.i(TAG, "image file created");
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                if (out != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileImage);
                    media.setData(contentUri);
                    GlobalContext.getContext().sendBroadcast(media);
                    Intent intent = new Intent(GlobalContext.getContext(), ResultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("file", filePath);
                    GlobalContext.getContext().startActivity(intent);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        Log.i(TAG, "virtual display stopped");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Bitmap readImageToBitmap() {
        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        stopVirtual();
        return bitmap;
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
