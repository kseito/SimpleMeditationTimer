package kzt.com.simplemeditationtimer;

/**
 * Created by k-seito on 2016/08/27.
 */
public class CommonUtil {

    public static String convertTime(int minute) {
        return String.format("%1$02d:00", minute);
    }

}
