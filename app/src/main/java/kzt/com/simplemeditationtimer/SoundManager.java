package kzt.com.simplemeditationtimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kazuto-seito on 2016/11/01.
 */

public class SoundManager {
    public static List<String> getSoundList() {
        List<String> list = new ArrayList<>();
        list.add("鐘の音");
        list.add("和風の鐘の音");
        list.add("デジタル音");
        return list;
    }

    public static int findSoundById(int position) {
        switch (position) {
            case 0:
                return R.raw.japanese_temple_bell_small;
            case 1:
                return R.raw.temple_bell;
            case 2:
                return R.raw.store_door_chime;
        }

        throw new IllegalArgumentException("position " +  position + " not found");
    }
}
