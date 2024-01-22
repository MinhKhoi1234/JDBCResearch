package com.example;

import com.example.JDBCConnection.JDBCConnection;
import com.example.model.SubTaskB;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        JDBCConnection jdbcConnection = new JDBCConnection();
        // System.out.println(jdbcConnection.getStartEndGlobalYearlyLandTempYear("Min"));
        // System.out.println(jdbcConnection.getStartEndGlobalYearlyTempYear("Max"));
        // System.out.println(jdbcConnection.getStartEndYearPopulation("Min"));
        // System.out.println(jdbcConnection.getCityAndTemp("Vietnam", "City", 1990, 2000).get(2).getAvgTemp());
        //Write the system.out.print to test here

        /** */
        long startTime = System.currentTimeMillis();

        // Task 2

        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        // mostSimilar true -> most similar, false -> least similar
        // omit true -> only take one region into account, false -> take all regions into account
        ArrayList<SubTaskB> test = new ArrayList<SubTaskB>();
        test = jdbcConnection.subTaskBTask2(1990, 10, "Vietnam", 1, 1, 1, true, true);
        if (!test.isEmpty()) {
            for(int i = 0; i < 10; i++){
                System.out.println(test.get(i).toString());
            }
        } else {
            System.out.println("The list is empty");
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution Time: " + executionTime + " milliseconds");

        /**
         * 
        // Task 1
        startTime = System.currentTimeMillis();

        test = new ArrayList<SubTaskB>();
        test = jdbcConnection.subTaskBTask1(1990, 10, "Vietnam", 1, 3, 1, true);
        if (!test.isEmpty()) {
            for(int i = 0; i < 10; i++){
                System.out.println(test.get(i).toString());
            }
        } else {
            System.out.println("The list is empty");
        }

        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        System.out.println("Execution Time: " + executionTime + " milliseconds");
        **/
    }
}