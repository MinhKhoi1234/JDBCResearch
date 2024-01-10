package com.example;

import com.example.JDBCConnection.JDBCConnection;

public class Main {
    public static void main(String[] args) {
        JDBCConnection jdbcConnection = new JDBCConnection();
        System.out.println(jdbcConnection.getStartEndGlobalYearlyLandTempYear("Min"));
        System.out.println(jdbcConnection.getStartEndGlobalYearlyTempYear("Max"));
        System.out.println(jdbcConnection.getStartEndYearPopulation("Min"));
        System.out.println(jdbcConnection.getCityAndTemp("Vietnam", "City", 1990, 2000).get(2).getAvgTemp());
        //Write the system.out.print to test here
    }
}