package com.leadinsource.dailyweightlog;

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
}
