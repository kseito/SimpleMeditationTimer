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
        int second = 600;
        String actual = CommonUtil.convertTime(second);
        Assert.assertThat(actual, is("10:00"));
    }
}
