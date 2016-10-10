package kzt.com.simplemeditationtimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by k-seito on 2016/08/27.
 */
public class CommonUtil {

    public static String convertTime(int minute) {
        return String.format("%1$02d:00", minute);
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

}
