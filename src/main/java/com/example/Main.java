package com.example;

import com.example.JDBCConnection.JDBCConnection;
import com.example.model.SubTaskA;
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

        
        // Task 2

        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        // mostSimilar true -> most similar, false -> least similar
        // omit true -> only take one time period of all regions into account, false -> take all time period of all regions into account

        long startTime = System.currentTimeMillis();

        SubTaskB test1 = new SubTaskB();
        test1.setNumberOfSimResults(20);

        ArrayList<SubTaskB> test = new ArrayList<SubTaskB>();
        test = jdbcConnection.subTaskBTask2(1990, 10, "Elbasan", 3, 1, 2, true, true);
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
        
        
        

        /*
        // Task 1
        long startTime = System.currentTimeMillis();

        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        // mostSimilar true -> most similar, false -> least similar

        ArrayList<SubTaskB>  test = new ArrayList<SubTaskB>();
        test = jdbcConnection.subTaskBTask1(1990, 10, "Vietnam", 1, 3, 1, false);
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
        
        */

        /* 
        // Sub Task A

        long startTime = System.currentTimeMillis();

        // region 1  = country, 2 = state, 3 = city
        int[] startingYears = {1985, 1990, 1995, 2000};
        String[] country = {"Vietnam", "Thailand", "Singapore", "Malaysia"};


        ArrayList<SubTaskA> ArrayListD = jdbcConnection.SubTaskATask4WithFilter(startingYears, 10, country, 1, 27 , 9.0E7, 1, true, true);
        System.out.println("Task 4");

        if(ArrayListD.isEmpty()){
            System.out.println("The list is empty");
        }
        for (int i = 0; i < ArrayListD.size(); i++) {
            System.out.println(ArrayListD.get(i).toString());
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution Time: " + executionTime + " milliseconds");
        
        */
    }
}