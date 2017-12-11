package floatview.coder.allen.com.floatview.utils;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.content.ContentValues.TAG;

/**
 * Created by husongzhen on 17/12/11.
 */

public class ShotUtils {


    public static final String getCutPath() {
        String pathImage = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
        String nameImage = pathImage + "shot_" + System.currentTimeMillis() + ".png";
        return nameImage;
    }


    public static final String saveBitmap(Bitmap bitmap) {
        String filePath = ShotUtils.getCutPath();
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


    public static Bitmap readImageToBitmap(ImageReader imageReader) {
        Bitmap bitmap = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Image image = imageReader.acquireLatestImage();
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
        }

        return bitmap;
    }

}
