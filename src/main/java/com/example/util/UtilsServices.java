package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsServices {
    private UtilsServices() {
    }

    public static String covertDateStr(long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date(timeMillis));
    }
}
