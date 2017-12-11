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
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void preShot() {
        createVirtualEnvironment();
    }


    public void startShotScreen() {
        startVirtual();
        startCapture()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        sendMediaFile(s);
                        OpenFile(s);
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createVirtualEnvironment() {
        mMediaProjectionManager1 = (MediaProjectionManager) GlobalContext.getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //ImageFormat.RGB_565
        mImageReader = ImageReader.newInstance(DeviceInfor.getSW(), DeviceInfor.getSH(), 0x1, 2);
    }


    private String getCutPath() {
        String pathImage = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
        String nameImage = pathImage + "shot_" + System.currentTimeMillis() + ".png";
        return nameImage;
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
    private Observable<String> startCapture() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Bitmap bitmap = readImageToBitmap();
                String filePath = saveBitmap(bitmap);
                if (filePath != null && !filePath.equals("")) {
                    subscriber.onNext(filePath);
                }
            }
        });
    }

    private String saveBitmap(Bitmap bitmap) {
        String filePath = getCutPath();
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
                }
                return filePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void OpenFile(String filePath) {
        Intent intent = new Intent(GlobalContext.getContext(), ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("file", filePath);
        GlobalContext.getContext().startActivity(intent);
    }

    private void sendMediaFile(String filePath) {
        File fileImage = new File(filePath);
        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(fileImage);
        media.setData(contentUri);
        GlobalContext.getContext().sendBroadcast(media);
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
