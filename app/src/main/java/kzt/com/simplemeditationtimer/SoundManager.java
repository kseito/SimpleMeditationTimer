package kzt.com.simplemeditationtimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kazuto-seito on 2016/11/01.
 */

public class SoundManager {
    public static List<String> getSoundList() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        return list;
    }

    public static int findSoundById(int id) {
        switch (id) {
            case 1:
                return R.raw.piano;
            case 2:
                return R.raw.piano;
            case 3:
                return R.raw.piano;
        }

        throw new IllegalArgumentException("Id not found");
    }
}
