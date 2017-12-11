package floatview.coder.allen.com.floatview.utils;

/**
 * Created by husongzhen on 17/12/11.
 */

public class CheckUtils {


    public static boolean isNull(Object handler) {
        return handler == null;
    }

    public static boolean isNullString(String filePath) {
        return filePath == null || "".equals(filePath);
    }
}
