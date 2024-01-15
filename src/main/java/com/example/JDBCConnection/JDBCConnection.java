package com.example.JDBCConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.model.Input;
import com.example.model.SubTaskB;

public class JDBCConnection {
    public JDBCConnection() {
    }

    private static final String DATABASE1 = "jdbc:sqlite:database/TempPopulation.db";
    // private static final String DATABASE2 = "jdbc:sqlite:database/Personas + Students.db";

    public ArrayList<String> getResultByCountryName() { //change the return type (TODO) (this method gets an ArrayList of country names)
        ArrayList<String> Countries = new ArrayList<String>(); //change the return type(TODO)

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE1); //Change database (TODO)

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCity;"; // change the query(TODO)
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String countryName     = results.getString("Country"); //Change the "Country" to the desired column (TODO)

                // Store the movieName in the ArrayList to return
                Countries.add(countryName);
            } // If the result isn't an array, then reference getStartYear() method below (TODO)

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return Countries; //change return (TODO)
    }

    public int getStartEndGlobalYearlyLandTempYear(String MINorMAX) { //the available years for yearly temperature on land globally
        int year = JDBCConnection.getYear(MINorMAX, "LAND");
        return year;
    }

    public int getStartEndGlobalYearlyTempYear(String MINorMAX) { //the available years for yearly temperature globally
        int year = JDBCConnection.getYear(MINorMAX, "GLOBAL");
        return year;
    }

    public int getStartEndYearPopulation(String MINorMAX) { //the available years for population globally
        int year = JDBCConnection.getYear(MINorMAX, "POPULATION");;

        return year;
    }

    private static int getYear(String MINorMAX, String LANDorGLOBALorPopulation) { //the above 3 methods get the year from this method
        int year = 0;
        
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);//

            Statement statement = connection.createStatement();//
            statement.setQueryTimeout(30);

            String query = "";

            if(LANDorGLOBALorPopulation.equalsIgnoreCase("LAND")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyLandTempByCountry;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("GLOBAL")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyTemp;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("Population")) {
                query = "SELECT " + MINorMAX + "(Year) FROM Population;";
            }

            ResultSet results = statement.executeQuery(query);
            year     = results.getInt(1);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return year;
    }

    public ArrayList<Input> getCityAndTemp(String countryName, String CityORState, long startYear, long endYear) { 
        //this gets cities/states, avgTemp,minTemp, maxTemp from the 4 methods below and store in an ArrayList of type Input
        int i;
        ArrayList<Input> inputs = new ArrayList<>();
        ArrayList<String> inputs1 = getCitiesStateByCountryName(countryName, CityORState);
        ArrayList<Double> inputs2 = getStartEndCityStateAvgTempChange(countryName, CityORState, startYear, endYear);
        ArrayList<Double> inputs3 = getStartEndCityStateMinTempChange(countryName, CityORState, startYear, endYear);
        ArrayList<Double> inputs4 = getStartEndCityStateMaxTempChange(countryName, CityORState, startYear, endYear);

        for(i = 0; i < inputs1.size(); ++i) {
            Input input = new Input();
            input.setCity(inputs1.get(i));
            input.setAvgTemp(inputs2.get(i));
            input.setMinTemp(inputs3.get(i));
            input.setMaxTemp(inputs4.get(i));
            inputs.add(input);
        }

        return inputs;
    }
    
    public static ArrayList<Double> getStartEndCityStateAvgTempChange(String countryName, String CityORState, long startYear, long endYear) { //avgTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> AvgTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("AverageTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("AverageTemperature");
                }


                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("AverageTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("AverageTemperature");
                }


                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                AvgTempChange.add(Math.abs(change));
            }


            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return AvgTempChange;
    }

    public static ArrayList<Double> getStartEndCityStateMinTempChange(String countryName, String CityORState, long startYear, long endYear) { //minTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> MinTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MinimumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MinimumTemperature");
                }

                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MinimumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MinimumTemperature");
                }

                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                MinTempChange.add(Math.abs(change));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return MinTempChange;
    }

    public static ArrayList<Double> getStartEndCityStateMaxTempChange(String countryName, String CityORState, long startYear, long endYear) { //maxTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> MaxTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MaximumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MaximumTemperature");
                }

                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MaximumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MaximumTemperature");
                }

                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                MaxTempChange.add(Math.abs(change));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return MaxTempChange;
    }

    public static ArrayList<String> getCitiesStateByCountryName(String countryName, String CityORState) { //cities/state of the country user typed
        ArrayList<String> CitiesState = new ArrayList<String>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '1950';";
            } else if (CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '1950';";
            }
            
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                String citiesState = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    citiesState     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    citiesState     = results.getString("State");
                }

                CitiesState.add(citiesState);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return CitiesState;
    }

    //Add more here
    public ArrayList<SubTaskB> subTaskBTask1(int startingYear, int timePeriod, String selectedRegion, int region, int option, int mode){
        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
        switch(option){
            case 1:
                result = similarTemp(startingYear, timePeriod, selectedRegion, region, mode);
                break;
            case 2:
                if (region == 1){
                    result = similarPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
            case 3:
                if (region == 1){
                    result = similarTempAndPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarTemp (int startingYear, int timePeriod, String selectedRegion, int region, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        switch(region){
            case 1:
                result = similarTempCountry(startingYear, timePeriod, selectedRegion, mode);
                break;
            case 2:
            result = similarTempState(startingYear, timePeriod, selectedRegion, mode);
                break;
            case 3:
            result = similarTempCity(startingYear, timePeriod, selectedRegion, mode);
                break;
        }
        
        Collections.sort(result, new Comparator<SubTaskB>() {
            @Override
            public int compare(SubTaskB o1, SubTaskB o2) {
                return Double.compare(o1.getSimilarRate(), o2.getSimilarRate());
            }
        });
        Collections.reverse(result);
        return result;
    }


    public static ArrayList<SubTaskB> similarPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();


        return result;
    }

    public static ArrayList<SubTaskB> similarTempAndPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        

        return result;
    }

    public static ArrayList<SubTaskB> similarTempCountry(int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> data = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "')";
            
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                boolean exists = resultSet.getBoolean(1);
                if (!exists) {
                    return data;
                }
            } 
            //Find available first and last year
            query = "SELECT MIN(Year) FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "';";

            int beginYear = 0;
            int endYear = 0;
            ResultSet results = statement.executeQuery(query);
            if (results.next()){
                beginYear = results.getInt(1);
            }

            query = "SELECT MAX(Year) FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "';";
            results = statement.executeQuery(query);
            if (results.next()){
                endYear = results.getInt(1);
            }

            query = "SELECT AverageTemperature FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year >= '" + startingYear + "' AND Year < '" + (startingYear + timePeriod) + "');";

            results = statement.executeQuery(query);
            double total = 0;

            while (results.next()) {

                total += results.getDouble("AverageTemperature");
            }
            double avg = total / timePeriod;

            statement.close();

            double average;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                average = 0;
                total = 0;
                query = "SELECT AverageTemperature FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year >= '" + i + "' AND Year < '" + (i + timePeriod) + "');";
                results = statement.executeQuery(query);
                while (results.next()) {
                    total += results.getDouble("AverageTemperature");
                }
                average = total / timePeriod;
                if (mode == 1){
                    similarRate = Math.abs(average - avg);
                }
                else{
                    similarRate = Math.abs((average - avg) / avg) * 100;
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, average, 0, similarRate);
                data.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public static ArrayList<SubTaskB> similarTempState(int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();

        return result;
    }

    public static ArrayList<SubTaskB> similarTempCity(int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();

        return result;
    }
}