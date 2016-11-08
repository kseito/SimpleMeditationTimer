package kzt.com.simplemeditationtimer;

import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by kazuto-seito on 2016/11/08.
 */

public final class Utils {

    public static String convertTime(int minute) {
        return String.format("%02d:00", minute);
    }

    public static int convertTextToTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        try {
            Date date = dateFormat.parse(time);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new IllegalArgumentException("time format not correct");
        }
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
