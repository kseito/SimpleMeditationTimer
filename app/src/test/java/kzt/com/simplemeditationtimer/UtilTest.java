package kzt.com.simplemeditationtimer;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by k-seito on 2016/08/27.
 */
public class UtilTest {

    @Test
    public void checkConvertTime() {
        int minute = 10;
        String actual = CommonUtil.convertTime(minute);
        Assert.assertThat(actual, is("10:00"));
    }

    @Test
    public void changeTextToTime() {
        String time = "10:25";
        int actual = CommonUtil.convertTextToTime(time);
        Assert.assertThat(actual, is(625));
    }
}
