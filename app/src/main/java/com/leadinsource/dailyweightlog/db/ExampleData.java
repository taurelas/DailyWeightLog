package com.leadinsource.dailyweightlog.db;

/**
 * Provider of example data for HistoryActivity testing
 */

public class ExampleData {
    private static String[] dates = {"01/10/2015", "02/10/2015", "03/10/2015", "04/10/2015", "05/10/2015",
            "06/10/2015", "07/10/2015", "08/10/2015", "09/10/2015", "10/10/2015", "11/10/2015"
            , "12/10/2015", "13/10/2015", "14/10/2015", "15/10/2015", "16/10/2015", "17/10/2015"
            , "18/10/2015", "19/10/2015", "20/10/2015" };

    private static String[] weights = {"72.0", "71.9", "71.8", "71.7", "71.6"
            , "71.5", "71.4", "71.3", "71.2", "71.1", "71.0", "70.9"
            , "70.8", "70.7", "70.6", "70.5", "70.4", "70.3", "70.2", "70.0"};

    private static String[] fatPcs = {"20.0%", "19.9%", "19.8%", "19.7%", "19.6%", "19.5%", "19.4%"
            , "19.3%", "19.2%", "19.1%", "19.0%", "18.9%", "18.8%", "18.7%", "18.6%", "18.5%"
            , "18.4%", "18.3%", "18.2%", "18.1%"};

    public static String getDate(int i) {
        return dates[i % 20];
    }

    public static String getWeight(int i) {
        return weights[i % 20];
    }

    public static String getFatPc(int i) {
        return fatPcs[i % 20];
    }
}
