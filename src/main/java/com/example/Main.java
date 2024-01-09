package com.example;

import com.example.JDBCConnection.JDBCConnection;

public class Main {
    public static void main(String[] args) {
        JDBCConnection jdbcConnection = new JDBCConnection();
        System.out.println(jdbcConnection.getResultByCountryName());
        System.out.println(jdbcConnection.getStartEndGlobalYearlyLandTempYear("MIN"));
        System.out.println(jdbcConnection.getStartEndGlobalYearlyTempYear("MAX"));
        System.out.println(jdbcConnection.getStartEndYearPopulation("MIN"));
        System.out.println("Hello world!");
    }
}