package com.enbecko.modcreator.test;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;

/**
 * Created by enbec on 01.03.2017.
 */
public class negativeInf {
    public static void main(String[] args) {
        double min = Double.NEGATIVE_INFINITY;
        Log.d(LogEnums.ETC, 2 > min);
    }
}
