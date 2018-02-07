package com.leadinsource.dailyweightlog.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides utility methods related to unit conversion and display
 */

public class Units {
    public static String getHeightTextWithUnits(String height) {
        if(height.isEmpty()) {
            return "";
        } else {
            return height + " cm";
        }
    }

    private static float getMetricBMI(float height, float weight) {
        return round(weight / ((height/100) * (height/100)),1);
    }

    public static String getMetricBMIString(float height, float weight) {
        if(height>0) {
            return getMetricBMI(height, weight) + "";
        } else {
            return "-";
        }
    }

    /*static float getImperialBMI(float heightInInches, float weightInLbs) {
        return round((weightInLbs / (heightInInches*heightInInches))*703,2);
    }*/

    private static float round(float value, int precision) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_UP);

        return bigDecimal.floatValue();
    }

    public static String getWeightTextWithUnits(float weight) {

        return weight + " kg";
    }

    public static Date stripTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal.getTime();
    }

    public static String getFatPcString(float fatPc) {
        return fatPc + " %";
    }
}
