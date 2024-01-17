package com.example;

import com.example.JDBCConnection.JDBCConnection;
import com.example.model.SubTaskB;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        JDBCConnection jdbcConnection = new JDBCConnection();
        // System.out.println(jdbcConnection.getStartEndGlobalYearlyLandTempYear("Min"));
        // System.out.println(jdbcConnection.getStartEndGlobalYearlyTempYear("Max"));
        // System.out.println(jdbcConnection.getStartEndYearPopulation("Min"));
        // System.out.println(jdbcConnection.getCityAndTemp("Vietnam", "City", 1990, 2000).get(2).getAvgTemp());
        //Write the system.out.print to test here

        ArrayList<SubTaskB> test = new ArrayList<SubTaskB>();
        test = jdbcConnection.subTaskBTask1(1990, 10, "Vietnam", 1, 3, 1);
        if (!test.isEmpty()) {
            for(int i = 0; i < 10; i++){
                System.out.println(test.get(i).toString());
            }
        } else {
            System.out.println("The list is empty");
        }
    }
}