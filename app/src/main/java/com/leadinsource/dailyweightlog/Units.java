package com.leadinsource.dailyweightlog;

import java.math.BigDecimal;

/**
 * Provides utility methods related to unit conversion and display
 */

class Units {
    static String getHeightTextWithUnits(String height) {
        if(height.isEmpty()) {
            return "";
        } else {
            return height + " cm";
        }
    }

    static float getMetricBMI(float height, float weight) {
        return round(weight / (height * height),2);
    }

    /*static float getImperialBMI(float heightInInches, float weightInLbs) {
        return round((weightInLbs / (heightInInches*heightInInches))*703,2);
    }*/

    private static float round(float value, int precision) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_UP);

        return bigDecimal.floatValue();
    }
}
